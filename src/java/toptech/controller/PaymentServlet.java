package toptech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/pay")
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
    
    // ✅ CORREGIDO: Manejar solicitudes GET para mostrar formularios de pago
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
        // Obtener información del ticket
        managers.TicketManager ticketManager = new managers.TicketManager(
            utils.DBConnection.getConnection()
        );
        models.Ticket ticket = ticketManager.findTicketById(ticketId);
        
        if (ticket == null) {
            response.sendRedirect("tickets?error=Ticket+no+encontrado");
            return;
        }
        
        // ✅ CORREGIDO: Crear HashMap correctamente
        java.util.HashMap<String, String> ticketInfo = new java.util.HashMap<>();
        ticketInfo.put("id", ticket.getId());
        ticketInfo.put("monto", String.format("%.2f", ticket.getMontoReparacion()));
        ticketInfo.put("descripcion", ticket.getDescripcion());
        ticketInfo.put("estado", ticket.getEstado());
        
        request.setAttribute("ticket", ticketInfo);
        request.setAttribute("method", method);
        
        // Redirigir al formulario correspondiente
        if ("card".equals(method)) {
            request.getRequestDispatcher("card-payment.jsp").forward(request, response);
        } else if ("yape".equals(method)) {
            request.getRequestDispatcher("yape-payment.jsp").forward(request, response);
        } else {
            // Para PayPal, mantener el flujo original
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
        String clientId = user.getDni();
        String ticketId = request.getParameter("ticketId");
        String method = request.getParameter("method");
        String action = request.getParameter("action"); // ✅ NUEVO: Para manejar confirmaciones
        
        // ✅ NUEVO: Manejar confirmaciones de pago desde formularios
        if ("confirm".equals(action)) {
            procesarConfirmacionPago(request, response, ticketId, method);
            return;
        }
        
        // ✅ MANTENER tu lógica original de PayPal
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
                // ✅ MODIFICADO: Redirigir al formulario de pago correspondiente
                response.sendRedirect("pay?ticketId=" + ticketId + "&method=" + method);
            } else {
                response.getWriter().write("Método no soportado");
            }
            
        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
    
    // ✅ NUEVO: Método para procesar confirmaciones de pago
    private void procesarConfirmacionPago(HttpServletRequest request, HttpServletResponse response, 
                                        String ticketId, String method) throws IOException {
        try {
            managers.TicketManager ticketManager = new managers.TicketManager(
                utils.DBConnection.getConnection()
            );
            models.Ticket ticket = ticketManager.findTicketById(ticketId);
            
            if (ticket == null) {
                response.sendRedirect("tickets?error=Ticket+no+encontrado");
                return;
            }
            
            double monto = ticket.getMontoReparacion();
            boolean pagoExitoso = false;
            
            if ("card".equals(method)) {
                // Procesar pago con tarjeta
                String cardNumber = request.getParameter("cardNumber");
                String cardHolder = request.getParameter("cardHolder");
                String expiryDate = request.getParameter("expiryDate");
                String cvv = request.getParameter("cvv");
                
                // ✅ Validar datos de tarjeta (simulado)
                if (cardNumber != null && !cardNumber.trim().isEmpty() &&
                    cardHolder != null && !cardHolder.trim().isEmpty() &&
                    expiryDate != null && !expiryDate.trim().isEmpty() &&
                    cvv != null && !cvv.trim().isEmpty()) {
                    
                    pagoExitoso = paymentManager.procesarPago(
                        Integer.parseInt(ticketId.replace("TK", "")), 
                        monto
                    );
                }
                
            } else if ("yape".equals(method)) {
                // Procesar pago con Yape (simulado)
                pagoExitoso = paymentManager.procesarPago(
                    Integer.parseInt(ticketId.replace("TK", "")), 
                    monto
                );
            }
            
            if (pagoExitoso) {
                // ✅ Actualizar estado del ticket a "Pagado" o "Solucionado"
                ticket.setEstado("SOLUCIONADO");
                ticketManager.updateTicket(ticket);
                
                response.sendRedirect("payment-success.jsp?ticketId=" + ticketId + "&method=" + method + "&monto=" + monto);
            } else {
                response.sendRedirect("tickets?error=Error+en+procesamiento+de+pago");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("tickets?error=Error+interno+del+sistema");
        }
    }
    
    // ✅ MANTENER toda tu lógica original de PayPal
    private void procesarPayPal(String amount, HttpServletResponse response) throws IOException {
        String client = com.toptech.util.DBConfig.get("paypal.client");
        String secret = com.toptech.util.DBConfig.get("paypal.secret");
        
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
    
    // ✅ MANTENER tus métodos auxiliares originales
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