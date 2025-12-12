package managers;

import utils.DBConnection;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EvidenceManager {
    public void guardarEvidencia(String ticketId, File imagen) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO evidencias (ticket_id, imagen, fecha) VALUES (?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ticketId);
                ps.setBytes(2, Files.readAllBytes(imagen.toPath()));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("Error al guardar evidencia: " + e.getMessage());
        }
    }
}