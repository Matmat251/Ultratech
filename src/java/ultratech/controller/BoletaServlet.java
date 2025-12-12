package ultratech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import utils.DBConnection;

@WebServlet("/boleta")
public class BoletaServlet extends HttpServlet {

    private static final String EMPRESA_NOMBRE = "TOP TECH";
    private static final String EMPRESA_RUC = "10731954351";
    private static final String EMPRESA_EMAIL = "ultratechperu@gmail.com";
    private static final String EMPRESA_DIRECCION = "Av. Grau 345, Barranco, Lima - Perú";
    private static final String EMPRESA_TELEFONOS = "906 289 945 - 924045900";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String ticketIds = request.getParameter("ids"); // Puede ser "TK001" o "TK001,TK002,TK003"
        String format = request.getParameter("format"); // "html" o "pdf"

        if (ticketIds == null || ticketIds.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se especificaron tickets");
            return;
        }

        try {
            String[] tickets = ticketIds.split(",");
            Map<String, Object> boletaData = generarBoleta(tickets);

            if ("pdf".equalsIgnoreCase(format)) {
                generarPDF(response, boletaData);
            } else {
                mostrarHTML(request, response, boletaData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error generando boleta: " + e.getMessage());
        }
    }

    private Map<String, Object> generarBoleta(String[] ticketCodes) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Constante: cargo por diagnóstico no pagado
        final double CARGO_DIAGNOSTICO = 50.0;

        try {
            conn = DBConnection.getConnection();

            // Obtener datos de los tickets
            List<Map<String, Object>> detalles = new ArrayList<>();
            double montoTotal = 0.0;
            String cliente = null;
            String dniCliente = null;

            for (String ticketCode : ticketCodes) {
                String sql = "SELECT t.*, u.nombre, u.apellido, u.dni " +
                        "FROM Tickets t " +
                        "LEFT JOIN Usuarios u ON t.dni = u.dni " +
                        "WHERE t.codigo = ?";

                ps = conn.prepareStatement(sql);
                ps.setString(1, ticketCode.trim());
                rs = ps.executeQuery();

                if (rs.next()) {
                    double montoReparacion = rs.getDouble("montoReparacion");
                    boolean diagnosticoPagado = rs.getBoolean("diagnosticoPagado");

                    // Agregar detalle de reparación
                    Map<String, Object> detalle = new HashMap<>();
                    detalle.put("codigo", rs.getString("codigo"));
                    detalle.put("descripcion", rs.getString("descripcion"));
                    detalle.put("equipo", rs.getString("equipo"));
                    detalle.put("monto", montoReparacion);
                    detalle.put("cantidad", 1);
                    detalle.put("tipo", "REPARACION");

                    detalles.add(detalle);
                    montoTotal += montoReparacion;

                    // Si diagnóstico NO está pagado, agregar cargo adicional de 50 soles
                    if (!diagnosticoPagado) {
                        Map<String, Object> detalleDiag = new HashMap<>();
                        detalleDiag.put("codigo", rs.getString("codigo"));
                        detalleDiag.put("descripcion", "Cargo por diagnóstico técnico no pagado previamente");
                        detalleDiag.put("equipo", rs.getString("equipo"));
                        detalleDiag.put("monto", CARGO_DIAGNOSTICO);
                        detalleDiag.put("cantidad", 1);
                        detalleDiag.put("tipo", "DIAGNOSTICO");

                        detalles.add(detalleDiag);
                        montoTotal += CARGO_DIAGNOSTICO;
                    }

                    if (cliente == null) {
                        cliente = rs.getString("nombre") + " " + rs.getString("apellido");
                        dniCliente = rs.getString("dni");
                    }
                }

                rs.close();
                ps.close();
            }

            if (detalles.isEmpty()) {
                throw new SQLException("No se encontraron tickets válidos");
            }

            // Generar número de boleta
            String numeroBoleta = getNextNumeroBoleta(conn);
            LocalDateTime fechaEmision = LocalDateTime.now();
            String montoLetras = convertirNumeroALetras(montoTotal);

            // Guardar boleta en BD
            String insertBoleta = "INSERT INTO Boletas (numeroBoleta, cliente, dniCliente, " +
                    "fechaEmision, montoTotal, montoLetras, tipoMoneda, estado, ticketsIncluidos) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'PEN', 'EMITIDA', ?)";

            ps = conn.prepareStatement(insertBoleta, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, numeroBoleta);
            ps.setString(2, cliente);
            ps.setString(3, dniCliente);
            ps.setTimestamp(4, Timestamp.valueOf(fechaEmision));
            ps.setDouble(5, montoTotal);
            ps.setString(6, montoLetras);
            ps.setString(7, String.join(",", ticketCodes));
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int idBoleta = 0;
            if (rs.next()) {
                idBoleta = rs.getInt(1);
            }
            rs.close();
            ps.close();

            // Guardar detalles
            String insertDetalle = "INSERT INTO DetallesBoleta (idBoleta, codigoTicket, descripcion, " +
                    "cantidad, precioUnitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(insertDetalle);
            for (Map<String, Object> detalle : detalles) {
                ps.setInt(1, idBoleta);
                ps.setString(2, (String) detalle.get("codigo"));
                String desc = "Reparación de " + detalle.get("equipo") + " - " + detalle.get("descripcion");
                ps.setString(3, desc);
                ps.setInt(4, 1);
                ps.setDouble(5, (Double) detalle.get("monto"));
                ps.setDouble(6, (Double) detalle.get("monto"));
                ps.addBatch();
            }
            ps.executeBatch();

            // Preparar datos para la vista
            Map<String, Object> result = new HashMap<>();
            result.put("numeroBoleta", numeroBoleta);
            result.put("cliente", cliente);
            result.put("dniCliente", dniCliente);
            result.put("fechaEmision", fechaEmision.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            result.put("montoTotal", montoTotal);
            result.put("montoLetras", montoLetras);
            result.put("detalles", detalles);
            result.put("empresaNombre", EMPRESA_NOMBRE);
            result.put("empresaRUC", EMPRESA_RUC);
            result.put("empresaEmail", EMPRESA_EMAIL);
            result.put("empresaDireccion", EMPRESA_DIRECCION);
            result.put("empresaTelefonos", EMPRESA_TELEFONOS);

            return result;

        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (Exception e) {
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (Exception e) {
                }
        }
    }

    private String getNextNumeroBoleta(Connection conn) throws SQLException {
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(numeroBoleta, 6) AS UNSIGNED)), 0) + 1 AS nextNum FROM Boletas";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int nextNum = rs.getInt("nextNum");
                return String.format("EB01-%04d", nextNum);
            }
        }
        return "EB01-0001";
    }

    private String convertirNumeroALetras(double monto) {
        // Implementación básica - puedes mejorarla
        int entero = (int) monto;
        int centavos = (int) Math.round((monto - entero) * 100);

        String[] unidades = { "", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE" };
        String[] decenas = { "", "", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA",
                "NOVENTA" };
        String[] especiales = { "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE",
                "DIECIOCHO", "DIECINUEVE" };
        String[] centenas = { "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS",
                "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS" };

        if (entero == 0)
            return "CERO Y " + String.format("%02d", centavos) + "/100 SOLES";

        String resultado = "";

        // Miles
        if (entero >= 1000) {
            int miles = entero / 1000;
            if (miles == 1) {
                resultado += "MIL ";
            } else {
                resultado += convertirCentenas(miles, unidades, decenas, especiales, centenas) + " MIL ";
            }
            entero = entero % 1000;
        }

        // Centenas, decenas y unidades
        if (entero > 0) {
            resultado += convertirCentenas(entero, unidades, decenas, especiales, centenas);
        }

        return resultado.trim() + " Y " + String.format("%02d", centavos) + "/100 SOLES";
    }

    private String convertirCentenas(int num, String[] unidades, String[] decenas, String[] especiales,
            String[] centenas) {
        String resultado = "";

        int c = num / 100;
        int d = (num % 100) / 10;
        int u = num % 10;

        if (c > 0) {
            if (num == 100)
                resultado += "CIEN ";
            else
                resultado += centenas[c] + " ";
        }

        if (d == 1) {
            resultado += especiales[u] + " ";
        } else {
            if (d > 0)
                resultado += decenas[d] + " ";
            if (u > 0 && d != 1)
                resultado += (d > 0 ? "Y " : "") + unidades[u] + " ";
        }

        return resultado.trim();
    }

    private void mostrarHTML(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> boletaData) throws ServletException, IOException {
        request.setAttribute("boleta", boletaData);
        request.getRequestDispatcher("boleta.jsp").forward(request, response);
    }

    private void generarPDF(HttpServletResponse response, Map<String, Object> boletaData)
            throws IOException {
        // Implementar con iText en el siguiente artefacto
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=boleta_" + boletaData.get("numeroBoleta") + ".pdf");

        // TODO: Generar PDF con iText
        response.getWriter().write("PDF generation - Implementar en siguiente paso");
    }
}