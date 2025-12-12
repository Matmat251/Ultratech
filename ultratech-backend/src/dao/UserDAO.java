package dao;

import models.User;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        // Constructor sin parámetros
    }

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        if (this.connection != null) {
            return this.connection;
        } else {
            return DBConnection.getConnection();
        }
    }

    public User getUserByDni(String dni) throws SQLException {
        return findByDNI(dni);
    }

    public User findByUsuarioAndPassword(String usuario, String contrasena) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return buildUser(rs);
        }
        return null;
    }

    public void addUser(User u) throws SQLException {
        String sql = "INSERT INTO Usuarios (dni, nombre, apellido, usuario, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getDni());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getUsuario());

            // Cifrar contraseña solo para ADMIN y TECNICO
            String password = u.getContrasena();
            if (("ADMIN".equalsIgnoreCase(u.getRol()) || "TECNICO".equalsIgnoreCase(u.getRol()))
                    && password != null && !password.isEmpty()
                    && !utils.PasswordUtils.isHashed(password)) {
                password = utils.PasswordUtils.hash(password);
            }
            ps.setString(5, password);

            ps.setString(6, u.getRol());
            ps.executeUpdate();
        }
    }

    public User findByUsuario(String usuario) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE usuario = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return buildUser(rs);
        }
        return null;
    }

    public User findByDNI(String dni) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE dni = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return buildUser(rs);
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> out = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = getConnection();
                Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
                out.add(buildUser(rs));
        }
        return out;
    }

    public boolean deleteByDNI(String dni) throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE dni = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ CORREGIDO: usar idUsuario en lugar de id
    private User buildUser(ResultSet rs) throws SQLException {
        User user = new User();

        // ✅ CORREGIDO: usar idUsuario
        user.setId(rs.getInt("idUsuario"));

        user.setDni(rs.getString("dni"));
        user.setNombre(rs.getString("nombre"));
        user.setApellido(rs.getString("apellido"));
        user.setUsuario(rs.getString("usuario"));
        user.setContrasena(rs.getString("contrasena"));
        user.setRol(rs.getString("rol"));

        return user;
    }
}