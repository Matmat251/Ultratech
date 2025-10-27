package toptech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/report")
public class PDFServlet extends HttpServlet {
    private managers.TicketManager ticketManager;
    
    @Override
    public void init() throws ServletException {
        try {
            java.sql.Connection connection = utils.DBConnection.getConnection();
            this.ticketManager = new managers.TicketManager(connection);
        } catch (java.sql.SQLException e) {
            throw new ServletException("Error inicializando TicketManager", e);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        models.User user = (models.User) session.getAttribute("user");
        String ticketId = request.getParameter("id");
        
        // ✅ Si no hay ticketId específico, generar reporte de todos los tickets
        if (ticketId == null || ticketId.trim().isEmpty()) {
            generarReporteCompleto(user, response);
            return;
        }
        
        // ✅ Generar PDF para un ticket específico (funcionalidad original)
        try {
            models.Ticket ticket = ticketManager.findTicketById(ticketId);
            
            if (ticket == null) {
                response.sendError(404, "Ticket no encontrado");
                return;
            }
            
            // ✅ MANTENER la lógica original de generación de PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=boleta_ticket_" + ticketId + ".pdf");
            
            try (OutputStream os = response.getOutputStream()) {
                // ✅ MANTENER el formato original del PDF
                String text = "Boleta TOP TECH\n" +
                            "Ticket: " + ticket.getId() + "\n" +
                            "Cliente: " + ticket.getCliente() + "\n" +
                            "DNI: " + ticket.getDni() + "\n" +
                            "Fecha: " + ticket.getFechaCreacion() + "\n" +
                            "Descripcion: " + ticket.getDescripcion() + "\n" +
                            "Estado: " + ticket.getEstado() + "\n" +
                            "Monto: S/ " + ticket.getMontoReparacion() + "\n" +
                            "Técnico: " + ticket.getTecnico() + "\n" +
                            "Prioridad: " + ticket.getPrioridad();
                
                os.write(text.getBytes("UTF-8"));
            }
            
        } catch (Exception e) {
            response.sendError(500, "Error generando PDF: " + e.getMessage());
        }
    }
    
    // ✅ NUEVO: Método para reporte completo de todos los tickets
    private void generarReporteCompleto(models.User user, HttpServletResponse response) 
            throws IOException {
        try {
            java.util.List<models.Ticket> tickets = ticketManager.getTicketsByUserDni(user.getDni());
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_tickets_" + user.getDni() + ".pdf");
            
            try (OutputStream os = response.getOutputStream()) {
                StringBuilder reporte = new StringBuilder();
                reporte.append("REPORTE DE TICKETS - TOP TECH\n");
                reporte.append("Cliente: ").append(user.getNombre()).append(" ").append(user.getApellido()).append("\n");
                reporte.append("DNI: ").append(user.getDni()).append("\n");
                reporte.append("Fecha Reporte: ").append(new java.util.Date()).append("\n\n");
                
                if (tickets.isEmpty()) {
                    reporte.append("No hay tickets registrados.\n");
                } else {
                    reporte.append("TICKETS:\n");
                    reporte.append("==========================================\n");
                    
                    for (models.Ticket ticket : tickets) {
                        reporte.append("Ticket: ").append(ticket.getId()).append("\n");
                        reporte.append("Descripción: ").append(ticket.getDescripcion()).append("\n");
                        reporte.append("Estado: ").append(ticket.getEstado()).append("\n");
                        reporte.append("Monto: S/ ").append(ticket.getMontoReparacion()).append("\n");
                        reporte.append("Fecha: ").append(ticket.getFechaCreacion()).append("\n");
                        reporte.append("------------------------------------------\n");
                    }
                    
                    // Total
                    double total = tickets.stream()
                                        .mapToDouble(models.Ticket::getMontoReparacion)
                                        .sum();
                    reporte.append("TOTAL: S/ ").append(total).append("\n");
                }
                
                os.write(reporte.toString().getBytes("UTF-8"));
            }
            
        } catch (Exception e) {
            response.sendError(500, "Error generando reporte completo: " + e.getMessage());
        }
    }
}