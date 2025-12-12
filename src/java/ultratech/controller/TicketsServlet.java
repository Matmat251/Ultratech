package ultratech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import utils.DBConnection;

@WebServlet("/tickets")
public class TicketsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        try {
            HttpSession session = request.getSession(false);

            // ✅ CORREGIDO: Verificar sesión más estricta
            if (session == null) {
                System.out.println("❌ No hay sesión activa");
                response.sendRedirect("index.jsp");
                return;
            }

            models.User user = (models.User) session.getAttribute("user");
            String clientId = (String) session.getAttribute("clientId");

            System.out.println("🎫 TicketsServlet - Usuario en sesión: " + (user != null ? user.getNombre() : "NULL"));
            System.out.println("🎫 TicketsServlet - ClientId en sesión: " + clientId);

            if (user == null && clientId == null) {
                System.out.println("❌ No hay usuario en sesión, redirigiendo a login");
                response.sendRedirect("index.jsp");
                return;
            }

            // ✅ CORREGIDO: Usar clientId si user es null
            String dniBusqueda = (user != null) ? user.getDni() : clientId;

            if (dniBusqueda == null) {
                System.out.println("❌ No hay DNI para buscar tickets");
                response.sendRedirect("index.jsp");
                return;
            }

            // ✅ CORREGIDO: Crear nueva conexión para esta petición
            conn = DBConnection.getConnection();
            managers.TicketManager ticketManager = new managers.TicketManager(conn);

            System.out.println("🔍 Buscando tickets para usuario DNI: " + dniBusqueda);
            List<models.Ticket> tickets = ticketManager.getTicketsByUserDni(dniBusqueda);
            System.out.println("✅ Tickets encontrados: " + (tickets != null ? tickets.size() : 0));

            // ✅ CONVERTIR List<Ticket> a List<HashMap> para el JSP
            List<HashMap<String, String>> ticketMaps = new ArrayList<>();
            if (tickets != null) {
                for (models.Ticket ticket : tickets) {
                    HashMap<String, String> ticketMap = new HashMap<>();
                    ticketMap.put("id", ticket.getId());
                    ticketMap.put("fecha", ticket.getFechaCreacion());
                    ticketMap.put("descripcion", ticket.getDescripcion());
                    ticketMap.put("monto", String.valueOf(ticket.getMontoReparacion()));
                    ticketMap.put("estado", ticket.getEstado());
                    // ✅ NUEVO: Agregar estado de diagnóstico pagado
                    ticketMap.put("diagnosticoPagado", ticket.isDiagnosticoPagado() ? "SI" : "NO");
                    ticketMaps.add(ticketMap);
                }
            }

            request.setAttribute("tickets", ticketMaps);
            request.getRequestDispatcher("tickets.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ Error cargando tickets: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error cargando tickets: " + e.getMessage());
            request.getRequestDispatcher("tickets.jsp").forward(request, response);
        } finally {
            // ✅ CORREGIDO: Cerrar conexión en finally
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}