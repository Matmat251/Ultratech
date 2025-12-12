package dao;

import models.Payment;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAO — adapta la app desktop a la estructura de la BD MySQL (tabla Pagos).
 * ✅ CORREGIDO: Maneja campos de imagen_url y numero_operacion correctamente
 */
public class PaymentDAO {

    private final Connection externalConnection;

    public PaymentDAO() {
        this.externalConnection = null;
    }

    public PaymentDAO(Connection connection) {
        this.externalConnection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.externalConnection != null) return this.externalConnection;
        return DBConnection.getConnection();
    }

    /**
     *  Agrega un pago con todos los campos incluyendo imagen
     */
    public void addPayment(Payment payment) throws SQLException {
        String ticketRef = null;
        Double monto = null;
        String estado = null;
        String metodo = null;
        String fechaStr = null;
        String imagenUrl = null;
        String numeroOperacion = null;

        try {
            ticketRef = invokeString(payment, "getTicketRef", "getTicket", "getCodigoTicket", "getCodigo");
            monto = invokeDouble(payment, "getMonto", "getAmount", "getAmountPaid");
            estado = invokeString(payment, "getStatus", "getEstado");
            metodo = invokeString(payment, "getMetodo", "getMetodoPago", "getMethod");
            imagenUrl = invokeString(payment, "getImagenUrl");
            numeroOperacion = invokeString(payment, "getNumeroOperacion");
            
            Object dateObj = invokeAny(payment, "getFechaPago", "getDate", "getFecha");
            if (dateObj != null) {
                fechaStr = dateObj.toString();
            }
        } catch (Exception e) {
            // ignore; use defaults
        }

        if (ticketRef == null) throw new SQLException("Payment object lacks ticket reference");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // map ticketRef to id_ticket
            String sqlFindTicket = "SELECT id_ticket FROM Tickets WHERE codigo = ?";
            ps = conn.prepareStatement(sqlFindTicket);
            ps.setString(1, ticketRef);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Ticket not found for code: " + ticketRef);
            }
            int idTicket = rs.getInt("id_ticket");
            rs.close(); ps.close();

            // ✅ CORREGIDO: Insert into Pagos con nuevos campos
            String insert = "INSERT INTO Pagos (idTicket, monto, fechaPago, metodoPago, estado, imagen_url, numero_operacion) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insert);
            ps.setInt(1, idTicket);
            ps.setDouble(2, monto == null ? 0.0 : monto);
            ps.setString(3, fechaStr == null ? LocalDate.now().toString() : fechaStr);
            ps.setString(4, metodo == null ? "YAPE" : metodo);
            ps.setString(5, estado == null ? "PENDIENTE" : estado);
            ps.setString(6, imagenUrl);
            ps.setString(7, numeroOperacion);
            
            ps.executeUpdate();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (this.externalConnection == null && conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }

public List<Payment> getPagosPendientes() throws SQLException {
    System.out.println("🔍 PaymentDAO - Buscando pagos pendientes...");
    List<Payment> pagos = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
        conn = getConnection();
        String sql = "SELECT p.*, t.codigo, t.cliente " +
                    "FROM Pagos p " +
                    "JOIN Tickets t ON p.idTicket = t.id_ticket " +
                    "WHERE p.estado = 'PENDIENTE' " +
                    "ORDER BY p.fechaPago DESC";
        
        System.out.println("📝 Ejecutando SQL: " + sql);
        
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            count++;
            String imagenUrl = rs.getString("imagen_url");
            
            System.out.println("✅ Pago " + count + ":");
            System.out.println("   - Ticket: " + rs.getString("codigo"));
            System.out.println("   - Imagen URL: '" + imagenUrl + "'");
            System.out.println("   - Longitud ruta: " + (imagenUrl != null ? imagenUrl.length() : 0));
            
            Payment pago = new Payment();
            pago.setTicketRef(rs.getString("codigo"));
            pago.setAmount(rs.getDouble("monto"));
            
            // ✅ CRÍTICO: Asignar imagenUrl sin modificar
            pago.setImagenUrl(imagenUrl != null ? imagenUrl.trim() : null);
            
            pago.setStatus(rs.getString("estado"));
            pago.setMetodoPago(rs.getString("metodoPago"));
            pago.setNumeroOperacion(rs.getString("numero_operacion"));
            
            String fechaStr = rs.getString("fechaPago");
            if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                pago.setFechaPago(fechaStr);
            } else {
                pago.setDate(LocalDate.now());
            }
            
            // ✅ VERIFICACIÓN ADICIONAL
            System.out.println("   📋 Objeto Payment creado:");
            System.out.println("      - imagenUrl en objeto: '" + pago.getImagenUrl() + "'");
            System.out.println("      - ¿Es null?: " + (pago.getImagenUrl() == null));
            System.out.println("      - ¿Está vacío?: " + (pago.getImagenUrl() != null && pago.getImagenUrl().isEmpty()));
            
            pagos.add(pago);
        }
        
        System.out.println("📊 Total pagos pendientes: " + count);
        
    } catch (SQLException e) {
        System.err.println("❌ Error SQL: " + e.getMessage());
        e.printStackTrace();
        throw e;
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception ignored) {}
        if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        if (this.externalConnection == null && conn != null) try { conn.close(); } catch (Exception ignored) {}
    }
    
    return pagos;
}

    /**
     * ✅ CORREGIDO: Actualiza el estado de un pago
     */
    public void actualizarEstadoPago(String ticketRef, String nuevoEstado) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Obtener id_ticket
            String sqlTicket = "SELECT id_ticket FROM Tickets WHERE codigo = ?";
            ps = conn.prepareStatement(sqlTicket);
            ps.setString(1, ticketRef);
            rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Ticket not found: " + ticketRef);
            int idTicket = rs.getInt("id_ticket");
            rs.close(); ps.close();

            // Actualizar estado del pago más reciente
            String update = "UPDATE Pagos SET estado = ? WHERE idTicket = ? ORDER BY idPago DESC LIMIT 1";
            ps = conn.prepareStatement(update);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTicket);
            ps.executeUpdate();
            
            System.out.println("✅ Estado actualizado: " + ticketRef + " -> " + nuevoEstado);
            
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (this.externalConnection == null && conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * Busca el pago (último) por ticketRef incluyendo imagen
     */
    public Payment findByTicketRef(String ticketRef) throws SQLException {
        if (ticketRef == null) return null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // map code to id_ticket
            String sqlTicket = "SELECT id_ticket FROM Tickets WHERE codigo = ?";
            ps = conn.prepareStatement(sqlTicket);
            ps.setString(1, ticketRef);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Ticket no encontrado: " + ticketRef);
                return null;
            }
            int idTicket = rs.getInt("id_ticket");
            rs.close(); ps.close();

            String sql = "SELECT * FROM Pagos WHERE idTicket = ? ORDER BY idPago DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTicket);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Pago no encontrado para ticket: " + ticketRef);
                return null;
            }

            double monto = rs.getDouble("monto");
            String fechaPago = rs.getString("fechaPago");
            String metodoPago = rs.getString("metodoPago");
            String estado = rs.getString("estado");
            String imagenUrl = rs.getString("imagen_url");
            String numeroOperacion = rs.getString("numero_operacion");

            System.out.println("✅ Pago encontrado para " + ticketRef + ": " + imagenUrl);

            try {
                // Intentar constructor con todos los campos
                try {
                    java.lang.reflect.Constructor<?> c = Payment.class.getConstructor(
                        String.class, double.class, LocalDate.class, String.class, 
                        String.class, String.class, String.class
                    );
                    LocalDate ld = parseToLocalDate(fechaPago);
                    Payment pago = (Payment) c.newInstance(ticketRef, monto, ld, estado, metodoPago, imagenUrl, numeroOperacion);
                    return pago;
                } catch (NoSuchMethodException ignore) {}

                // Constructor básico + setters
                Payment p = new Payment();
                p.setTicketRef(ticketRef);
                p.setAmount(monto);
                p.setFechaPago(fechaPago);
                p.setStatus(estado);
                p.setMetodoPago(metodoPago);
                p.setImagenUrl(imagenUrl);
                p.setNumeroOperacion(numeroOperacion);
                return p;

            } catch (Exception ex) {
                System.err.println("❌ Error creando objeto Payment: " + ex.getMessage());
                ex.printStackTrace();
                return null;
            }

        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (this.externalConnection == null && conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }

    // ---------- Helpers reflection/util (MANTENIDOS) ----------

    private static String invokeString(Object obj, String... methods) {
        for (String m : methods) {
            try {
                java.lang.reflect.Method method = obj.getClass().getMethod(m);
                Object res = method.invoke(obj);
                if (res instanceof String) return (String) res;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static Double invokeDouble(Object obj, String... methods) {
        for (String m : methods) {
            try {
                java.lang.reflect.Method method = obj.getClass().getMethod(m);
                Object res = method.invoke(obj);
                if (res instanceof Number) return ((Number) res).doubleValue();
                if (res instanceof String) return Double.parseDouble((String) res);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static Object invokeAny(Object obj, String... methods) {
        for (String m : methods) {
            try {
                java.lang.reflect.Method method = obj.getClass().getMethod(m);
                Object res = method.invoke(obj);
                if (res != null) return res;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static LocalDate parseToLocalDate(String s) {
        if (s == null) return LocalDate.now();
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            try {
                String datePart = s.split(" ")[0];
                return LocalDate.parse(datePart);
            } catch (Exception ex) {
                return LocalDate.now();
            }
        }
    }

    private static void tryInvokeSetter(Object obj, String setterName, Object value) {
        try {
            for (java.lang.reflect.Method m : obj.getClass().getMethods()) {
                if (m.getName().equalsIgnoreCase(setterName) && m.getParameterCount() == 1) {
                    Class<?> param = m.getParameterTypes()[0];
                    if (param == String.class) m.invoke(obj, String.valueOf(value));
                    else if (param == double.class || param == Double.class) m.invoke(obj, ((Number) value).doubleValue());
                    else m.invoke(obj, value);
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Comprueba si existe un pago para el ticketRef.
     */
    public boolean existePago(String ticketRef) throws SQLException {
        return findByTicketRef(ticketRef) != null;
    }

    /**
     * ✅ CORREGIDO: Actualiza el pago más reciente del ticket
     */
    public void updatePayment(Payment payment) throws SQLException {
        String ticketRef = invokeString(payment, "getTicketRef", "getTicket", "getCodigoTicket", "getCodigo");
        if (ticketRef == null) throw new SQLException("Payment object lacks ticket reference for update");

        Double monto = invokeDouble(payment, "getMonto", "getAmount", "getAmountPaid");
        String estado = invokeString(payment, "getStatus", "getEstado");
        String metodo = invokeString(payment, "getMetodo", "getMetodoPago", "getMethod");
        Object dateObj = invokeAny(payment, "getFechaPago", "getDate", "getFecha");
        String fechaStr = dateObj == null ? LocalDate.now().toString() : dateObj.toString();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // obtener id_ticket
            String sqlTicket = "SELECT id_ticket FROM Tickets WHERE codigo = ?";
            ps = conn.prepareStatement(sqlTicket);
            ps.setString(1, ticketRef);
            rs = ps.executeQuery();
            if (!rs.next()) throw new SQLException("Ticket not found: " + ticketRef);
            int idTicket = rs.getInt("id_ticket");
            rs.close(); ps.close();

            // obtener idPago más reciente
            String sqlFindPago = "SELECT idPago FROM Pagos WHERE idTicket = ? ORDER BY idPago DESC LIMIT 1";
            ps = conn.prepareStatement(sqlFindPago);
            ps.setInt(1, idTicket);
            rs = ps.executeQuery();
            if (!rs.next()) {
                // no hay pago para actualizar; insertar uno nuevo
                rs.close(); ps.close();
                addPayment(payment);
                return;
            }
            int idPago = rs.getInt("idPago");
            rs.close(); ps.close();

            // update Pagos set ...
            String update = "UPDATE Pagos SET monto = ?, fechaPago = ?, metodoPago = ?, estado = ? WHERE idPago = ?";
            ps = conn.prepareStatement(update);
            ps.setDouble(1, monto == null ? 0.0 : monto);
            ps.setString(2, fechaStr);
            ps.setString(3, metodo == null ? "YAPE" : metodo);
            ps.setString(4, estado == null ? "COMPLETADO" : estado);
            ps.setInt(5, idPago);
            ps.executeUpdate();

        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (this.externalConnection == null && conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}