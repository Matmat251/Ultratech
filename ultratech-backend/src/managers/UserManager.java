package managers;

import dao.UserDAO;
import models.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserManager {
    private UserDAO dao;

    // ✅ CONSTRUCTOR ORIGINAL (para desktop)
    public UserManager() {
        this.dao = new UserDAO();
    }

    // ✅ CONSTRUCTOR PARA WEB (usa la conexión proporcionada)
    public UserManager(Connection connection) {
        this.dao = new UserDAO(connection); // ✅ PASA la conexión al DAO
    }

    // ✅ MÉTODO para la web
    public User getUserByDni(String dni) {
        try { 
            return dao.findByDNI(dni); 
        } catch (SQLException e) { 
            System.err.println("❌ Error en getUserByDni: " + e.getMessage());
            e.printStackTrace();
            return null; 
        }
    }

    // ✅ MANTENER todos tus métodos existentes
    public void addUser(User user) {
        try { 
            dao.addUser(user); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    
    public User findUserByDNI(String dni) {
        try { 
            return dao.findByDNI(dni); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return null; 
        }
    }
    
    public User findUserByUsuario(String usuario) {
        try { 
            return dao.findByUsuario(usuario); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return null; 
        }
    }
    
    public User[] getAllUsers() {
        try {
            List<User> list = dao.getAllUsers();
            return list.toArray(new User[0]);
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return new User[0]; 
        }
    }
    
    public boolean eliminarUsuarioPorDNI(String dni) {
        try { 
            return dao.deleteByDNI(dni); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}