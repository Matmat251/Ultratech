package dao;

import models.Ticket;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private Connection connection;

    public TicketDAO() {
        // Constructor sin parámetros
    }

    public TicketDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null) {
            return this.connection;
        } else {
            return DBConnection.getConnection();
        }
    }

    // Método para añadir ticket
    public void addTicket(Ticket t) throws SQLException {
        String sql = "INSERT INTO Tickets (codigo, cliente, dni, equipo, descripcion, estado, tecnico, prioridad, fechaCreacion, fechaFin, correo, celular, diagnosticoPagado, montoReparacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, t);
            ps.executeUpdate();
        }
    }

    // Método UPDATE corregido
    public void updateTicket(Ticket t) throws SQLException {
        String sql = "UPDATE Tickets SET cliente=?, dni=?, equipo=?, descripcion=?, estado=?, tecnico=?, prioridad=?, fechaCreacion=?, fechaFin=?, correo=?, celular=?, diagnosticoPagado=?, montoReparacion=? "
                   + "WHERE codigo=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getCliente());
            ps.setString(2, t.getDni());
            ps.setString(3, t.getEquipo());
            ps.setString(4, t.getDescripcion());
            ps.setString(5, t.getEstado());
            ps.setString(6, t.getTecnico());
            ps.setString(7, t.getPrioridad());
            ps.setString(8, t.getFechaCreacion());
            ps.setString(9, t.getFechaFin());
            ps.setString(10, t.getCorreo());
            ps.setString(11, t.getCelular());
            ps.setBoolean(12, t.isDiagnosticoPagado());
            ps.setDouble(13, t.getMontoReparacion());
            ps.setString(14, t.getId());

            ps.executeUpdate();
        }
    }

    // NUEVO MÉTODO: Actualizar nombre por DNI
    public int updateNombreByDni(String dni, String nuevoNombre) throws SQLException {
        String sql = "UPDATE Tickets SET cliente = ? WHERE dni = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, dni);
            return ps.executeUpdate();
        }
    }

    public Ticket findById(String id) throws SQLException {
        String sql = "SELECT * FROM Tickets WHERE codigo = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildTicket(rs);
            }
        }
        return null;
    }

    public List<Ticket> getTicketsByDNI(String dni) throws SQLException {
        return getTicketsByUserDni(dni);
    }

    // Método para obtener tickets por DNI
    public List<Ticket> getTicketsByUserDni(String dni) throws SQLException {
        List<Ticket> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Intentar con tabla Tickets (mayúscula)
            String sql = "SELECT * FROM Tickets WHERE dni = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(buildTicket(rs));
            }
            
            // Si no hay resultados, intentar con tabla tickets (minúscula)
            if (lista.isEmpty()) {
                if (rs != null) { rs.close(); }
                if (ps != null) { ps.close(); }
                
                sql = "SELECT * FROM tickets WHERE dni = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, dni);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    lista.add(buildTicket(rs));
                }
            }
            
            return lista;
            
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (ps != null) try { ps.close(); } catch (Exception e) {}
            if (this.connection == null && conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }

    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tickets";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(buildTicket(rs));
            }
        }
        return lista;
    }

    public String generarCodigoTicket() {
        String codigo = "TK001";
        String sql = "SELECT codigo FROM Tickets ORDER BY id_ticket DESC LIMIT 1";  
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultimoCodigo = rs.getString("codigo");
                if (ultimoCodigo != null && ultimoCodigo.startsWith("TK")) {
                    int numero = Integer.parseInt(ultimoCodigo.substring(2)) + 1;
                    codigo = String.format("TK%03d", numero);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codigo;
    }

    private void fillStatement(PreparedStatement ps, Ticket t) throws SQLException {
        ps.setString(1, t.getId());
        ps.setString(2, t.getCliente());
        ps.setString(3, t.getDni());
        ps.setString(4, t.getEquipo());
        ps.setString(5, t.getDescripcion());
        ps.setString(6, t.getEstado());
        ps.setString(7, t.getTecnico());
        ps.setString(8, t.getPrioridad());
        ps.setString(9, t.getFechaCreacion());
        ps.setString(10, t.getFechaFin());
        ps.setString(11, t.getCorreo());
        ps.setString(12, t.getCelular());
        ps.setBoolean(13, t.isDiagnosticoPagado());
        ps.setDouble(14, t.getMontoReparacion());
    }

    private Ticket buildTicket(ResultSet rs) throws SQLException {
        return new Ticket(
            rs.getString("codigo"),
            rs.getString("cliente"),
            rs.getString("dni"),
            rs.getString("equipo"),
            rs.getString("descripcion"),
            rs.getString("estado"),
            rs.getString("tecnico"),
            rs.getString("prioridad"),
            rs.getString("fechaCreacion"),
            rs.getString("fechaFin"),
            rs.getString("correo"),
            rs.getString("celular"),
            rs.getBoolean("diagnosticoPagado"),
            rs.getDouble("montoReparacion")
        );
    }
}