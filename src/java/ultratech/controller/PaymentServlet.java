package ultratech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Base64;
import java.util.HashMap;

@WebServlet("/pay")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5,      // 5MB
    maxRequestSize = 1024 * 1024 * 10,  // 10MB
    fileSizeThreshold = 1024 * 1024     // 1MB
)
public class PaymentServlet extends HttpServlet {
    private managers.PaymentManager paymentManager;
    
    @Override
    public void init() throws ServletException {
        try {
            java.sql.Connection connection = utils.DBConnection.getConnection();
            this.paymentManager = new managers.PaymentManager(connection);
        } catch (java.sql.SQLException e) {
            throw new ServletException("Error inicializando PaymentManager", e);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String ticketId = request.getParameter("ticketId");
        String method = request.getParameter("method");
        
        if (ticketId == null || method == null) {
            response.sendRedirect("tickets");
            return;
        }
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        try {
            managers.TicketManager ticketManager = new managers.TicketManager(
                utils.DBConnection.getConnection()
            );
            models.Ticket ticket = ticketManager.findTicketById(ticketId);
            
            if (ticket == null) {
                response.sendRedirect("tickets?error=Ticket+no+encontrado");
                return;
            }
            
            HashMap<String, String> ticketInfo = new HashMap<>();
            ticketInfo.put("id", ticket.getId());
            ticketInfo.put("monto", String.format("%.2f", ticket.getMontoReparacion()));
            ticketInfo.put("descripcion", ticket.getDescripcion());
            ticketInfo.put("estado", ticket.getEstado());
            ticketInfo.put("cliente", ticket.getCliente());
            ticketInfo.put("equipo", ticket.getEquipo());
            
            request.setAttribute("ticket", ticketInfo);
            request.setAttribute("method", method);
            
            if ("card".equals(method)) {
                request.getRequestDispatcher("card-payment.jsp").forward(request, response);
            } else if ("yape".equals(method)) {
                request.getRequestDispatcher("yape-payment.jsp").forward(request, response);
            } else {
                response.sendRedirect("tickets");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("tickets?error=Error+al+procesar+solicitud");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        models.User user = (models.User) session.getAttribute("user");
        String ticketId = request.getParameter("ticketId");
        String method = request.getParameter("method");
        String action = request.getParameter("action");
        
        //  Manejar subida de comprobantes con imagen
        if ("confirm".equals(action)) {
            procesarConfirmacionPagoConImagen(request, response, ticketId, method);
            return;
        }
        
        // Mantener lógica original de PayPal
        try {
            managers.TicketManager ticketManager = new managers.TicketManager(
                utils.DBConnection.getConnection()
            );
            models.Ticket ticket = ticketManager.findTicketById(ticketId);
            
            if (ticket == null) {
                response.getWriter().write("Ticket no encontrado");
                return;
            }
            
            String amount = String.valueOf(ticket.getMontoReparacion());
            
            if ("paypal".equals(method)) {
                procesarPayPal(amount, response);
            } else if ("yape".equals(method) || "card".equals(method)) {
                response.sendRedirect("pay?ticketId=" + ticketId + "&method=" + method);
            } else {
                response.getWriter().write("Método no soportado");
            }
            
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
    
    // ✅ NUEVO MÉTODO MEJORADO: Procesar pago con imagen
    private void procesarConfirmacionPagoConImagen(HttpServletRequest request, HttpServletResponse response, 
                                                  String ticketId, String method) throws IOException {
        Connection conn = null;
        try {
            // Obtener datos del formulario
            Part imagenPart = request.getPart("comprobante");
            String montoStr = request.getParameter("monto");
            String numeroOperacion = request.getParameter("numeroOperacion");
            
            if (imagenPart == null || imagenPart.getSize() == 0) {
                response.sendRedirect("tickets?error=Debe+subir+un+comprobante+de+pago");
                return;
            }
            
            managers.TicketManager ticketManager = new managers.TicketManager(
                utils.DBConnection.getConnection()
            );
            models.Ticket ticket = ticketManager.findTicketById(ticketId);
            
            if (ticket == null) {
                response.sendRedirect("tickets?error=Ticket+no+encontrado");
                return;
            }
            
            double monto = Double.parseDouble(montoStr);
            
            // 1. Guardar imagen en servidor
            String imagenUrl = guardarImagenComprobante(imagenPart, ticketId);
            
            // 2. Registrar pago en base de datos con la imagen
            conn = utils.DBConnection.getConnection();
            String sql = "INSERT INTO Pagos (idTicket, monto, fechaPago, metodoPago, estado, imagen_url, numero_operacion) " +
                        "VALUES (?, ?, NOW(), ?, 'PENDIENTE', ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(ticketId.replace("TK", "")));
            pstmt.setDouble(2, monto);
            pstmt.setString(3, method.toUpperCase());
            pstmt.setString(4, imagenUrl);
            pstmt.setString(5, numeroOperacion != null ? numeroOperacion : "");
            
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            
            if (filasAfectadas > 0) {
                // ✅ Éxito - redirigir a página de éxito
                response.sendRedirect("payment-success.jsp?ticketId=" + ticketId + "&method=" + method + "&monto=" + monto);
            } else {
                response.sendRedirect("tickets?error=Error+al+registrar+pago+en+BD");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("tickets?error=Error+interno+del+sistema: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }
    
   // ✅ CORREGIDO: Guardar directamente en Tomcat
private String guardarImagenComprobante(Part imagenPart, String ticketId) throws IOException {
    System.out.println("🖼️ Guardando imagen para ticket: " + ticketId);
    
    // ✅ RUTA ABSOLUTA DE TOMCAT
    String tomcatPath = "C:/apache-tomcat-11.0.11/apache-tomcat-11.0.11/webapps/toptechweb/uploads/pagos";
    File uploadDir = new File(tomcatPath);
    
    System.out.println("🏠 Ruta TOMCAT: " + tomcatPath);
    System.out.println("📁 Directorio existe: " + uploadDir.exists());
    
    // Crear directorio si no existe
    if (!uploadDir.exists()) {
        boolean created = uploadDir.mkdirs();
        System.out.println("📁 Directorio creado: " + created);
        if (!created) {
            throw new IOException("No se pudo crear el directorio: " + tomcatPath);
        }
    }
    
    // Generar nombre de archivo
    String originalFileName = imagenPart.getSubmittedFileName();
    String fileExtension = ".jpg";
    
    if (originalFileName != null && originalFileName.contains(".")) {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    
    String fileName = "pago_" + ticketId + "_" + System.currentTimeMillis() + fileExtension;
    String filePath = tomcatPath + File.separator + fileName;
    
    System.out.println("💾 Guardando como: " + fileName);
    System.out.println("📍 Ruta física: " + filePath);
    
    // Guardar archivo
    try (InputStream fileContent = imagenPart.getInputStream();
         FileOutputStream out = new FileOutputStream(filePath)) {
        
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileContent.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
    
    // Verificar que se guardó
    File savedFile = new File(filePath);
    System.out.println("✅ Archivo guardado en Tomcat: " + savedFile.exists());
    System.out.println("📊 Tamaño: " + savedFile.length() + " bytes");
    
    if (!savedFile.exists()) {
        throw new IOException("El archivo no se guardó en Tomcat: " + filePath);
    }
    
    // Ruta relativa para la base de datos
    String relativePath = "uploads/pagos/" + fileName;
    System.out.println("🔗 Ruta para BD: " + relativePath);
    
    return relativePath;
}
    
    // ✅ MANTENER métodos originales de PayPal (sin cambios)
    private void procesarPayPal(String amount, HttpServletResponse response) throws IOException {
        String client = com.ultratech.util.DBConfig.get("paypal.client");
        String secret = com.ultratech.util.DBConfig.get("paypal.secret");
        
        if (client == null || client.isEmpty()) {
            response.getWriter().write("PayPal no configurado. Edita src/resources/db.properties");
            return;
        }
        
        try {
            String creds = client + ":" + secret;
            String b64 = Base64.getEncoder().encodeToString(creds.getBytes("UTF-8"));
            
            URL url = new URL("https://api-m.sandbox.paypal.com/v1/oauth2/token");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic " + b64);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);
            
            try (OutputStream os = con.getOutputStream()) {
                os.write("grant_type=client_credentials".getBytes("UTF-8"));
            }
            
            if (con.getResponseCode() != 200) {
                response.getWriter().write("Error token PayPal: " + con.getResponseCode());
                return;
            }
            
            String tokenJson = readStream(con.getInputStream());
            String token = extract(tokenJson, "\"access_token\":\"", '\"');
            
            if (token == null) {
                response.getWriter().write("No se obtuvo token");
                return;
            }
            
            URL ord = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders");
            HttpURLConnection co = (HttpURLConnection) ord.openConnection();
            co.setRequestMethod("POST");
            co.setRequestProperty("Authorization", "Bearer " + token);
            co.setRequestProperty("Content-Type", "application/json");
            co.setDoOutput(true);
            
            String body = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":\"" + amount + "\"}}]}";
            try (OutputStream os2 = co.getOutputStream()) {
                os2.write(body.getBytes("UTF-8"));
            }
            
            String orderResp = readStream(co.getInputStream());
            String approve = null;
            int idx = orderResp.indexOf("\"rel\":\"approve\"");
            
            if (idx != -1) {
                int href = orderResp.lastIndexOf("\"href\":\"", idx);
                if (href != -1) {
                    int start = href + 8;
                    int end = orderResp.indexOf('\"', start);
                    approve = orderResp.substring(start, end);
                }
            }
            
            if (approve != null) {
                response.sendRedirect(approve);
                return;
            }
            
            response.getWriter().write("Orden: " + orderResp);
            
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
    
    private static String readStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String l;
        while ((l = br.readLine()) != null) sb.append(l);
        return sb.toString();
    }
    
    private static String extract(String src, String key, char endChar) {
        int p = src.indexOf(key);
        if (p == -1) return null;
        int s = p + key.length();
        int e = src.indexOf(endChar, s);
        if (e == -1) return null;
        return src.substring(s, e);
    }
}