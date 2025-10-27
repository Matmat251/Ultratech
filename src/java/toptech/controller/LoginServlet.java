package toptech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import managers.UserManager;
import managers.TicketManager;
import models.User;
import models.Ticket;
import utils.DBConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dni = request.getParameter("dni");
        System.out.println("🔍 Intento de login con DNI: " + dni);

        if (dni == null || dni.trim().isEmpty()) {
            request.setAttribute("error", "Debe ingresar su DNI");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            UserManager userManager = new UserManager(conn);
            User user = userManager.getUserByDni(dni.trim());

            if (user != null) {
                System.out.println("✅ Usuario encontrado: " + user.getNombre());

                // 🔒 Invalidar sesiones viejas
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) oldSession.invalidate();

                // 🔐 Crear nueva sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("clientId", user.getDni());

                // ✅ Cargar los tickets del usuario desde la BD
                TicketManager ticketManager = new TicketManager(conn);
                List<Ticket> tickets = ticketManager.getTicketsByUserDni(user.getDni());
                session.setAttribute("tickets", tickets);

                System.out.println("📋 Tickets cargados: " + (tickets != null ? tickets.size() : 0));

                // ✅ CORREGIDO: Redirigir al servlet de tickets para que procese los datos
                response.sendRedirect("tickets");
                
            } else {
                System.out.println("❌ DNI no encontrado");
                request.setAttribute("error", "DNI no registrado");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error del sistema: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } finally {
            // ✅ CORREGIDO: Cerrar conexión en finally
            if (conn != null) {
                try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}