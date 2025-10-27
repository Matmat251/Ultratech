package toptech.controller;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.*;
import java.util.Map;

public class DownloadServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    HttpSession s = req.getSession(false);
    if (s == null || s.getAttribute("clientId") == null) {
      resp.sendRedirect("index.jsp");
      return;
    }

    String clientId = (String) s.getAttribute("clientId");
    String ticketId = req.getParameter("id");

    // ✅ CORREGIDO: Usar TicketManager en lugar de TicketDAO
    try {
        java.sql.Connection connection = utils.DBConnection.getConnection();
        managers.TicketManager ticketManager = new managers.TicketManager(connection);
        
        // Obtener el ticket usando TicketManager
        models.Ticket ticket = ticketManager.findTicketById(ticketId);
        
        if (ticket == null) {
            resp.sendError(404, "No encontrado");
            return;
        }

        resp.setContentType("text/html");
        resp.setHeader("Content-Disposition", "attachment; filename=boleta_ticket_" + ticketId + ".html");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>Boleta Simple - TOP TECH</h2>");
            out.println("<p><strong>Ticket ID:</strong> " + ticket.getId() + "</p>");
            out.println("<p><strong>Cliente:</strong> " + ticket.getCliente() + "</p>");
            out.println("<p><strong>Descripción:</strong> " + ticket.getDescripcion() + "</p>");
            out.println("<p><strong>Estado:</strong> " + ticket.getEstado() + "</p>");
            out.println("<p><strong>Monto Reparación:</strong> S/ " + ticket.getMontoReparacion() + "</p>");
            out.println("</body></html>");
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        resp.sendError(500, "Error interno del servidor");
    }
  }
}