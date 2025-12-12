package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/toptechbd";
    private static final String USER = "root";  
    private static final String PASS = "002513";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error cargando driver MySQL", e);
        }
    }
    
    // ✅ CORREGIDO: Método mejorado con manejo de errores
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Conexión MySQL establecida");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error conectando a la BD: " + e.getMessage());
            throw e;
        }
    }
    
    public static Connection getConnection(String url, String user, String password) 
            throws SQLException {
        if (url != null && !url.isEmpty()) {
            return DriverManager.getConnection(url, user, password);
        } else {
            return getConnection();
        }
    }
}