package managers;

import dao.PaymentDAO;
import models.Payment;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaymentManager {
    private final PaymentDAO paymentDAO;

    // ✅ CONSTRUCTOR ORIGINAL (para tu app desktop) - MANTENIDO
    public PaymentManager() {
        this.paymentDAO = new PaymentDAO();
    }

    // ✅ NUEVO CONSTRUCTOR (para la app web) - AGREGADO
    public PaymentManager(Connection connection) {
        this.paymentDAO = new PaymentDAO(connection);
    }

    // ✅ MÉTODO para la web - AGREGADO (sin JOptionPane)
    public boolean procesarPago(int ticketId, double monto) {
        try {
            String ticketRef = "TK" + String.format("%03d", ticketId); // Convertir ID a referencia de ticket
            
            if (paymentDAO.existePago(ticketRef)) {
                System.out.println("⚠️ Ya existe un pago para el ticket: " + ticketRef);
                return false;
            }

            Payment payment = new Payment(ticketRef, monto, LocalDate.now(), "COMPLETADO");
            paymentDAO.addPayment(payment);
            
            System.out.println("✅ Pago procesado correctamente para ticket: " + ticketRef);
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al procesar pago: " + e.getMessage());
            return false;
        }
    }

    // ✅ MÉTODO para la web - AGREGADO
    public Payment obtenerPagoPorTicket(String ticketRef) {
        try {
            return paymentDAO.findByTicketRef(ticketRef);
        } catch (SQLException e) {
            System.err.println("❌ Error obteniendo pago: " + e.getMessage());
            return null;
        }
    }

    // ✅ TODOS TUS MÉTODOS ORIGINALES - MANTENIDOS 100%
    public void registrarPago(String ticketRef, double amount) {
        try {
            if (paymentDAO.existePago(ticketRef)) {
                JOptionPane.showMessageDialog(null,
                    "⚠️ Ya existe un pago registrado para el ticket: " + ticketRef,
                    "Pago duplicado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            Payment payment = new Payment(ticketRef, amount, LocalDate.now(), "COMPLETADO");
            paymentDAO.addPayment(payment);

            JOptionPane.showMessageDialog(null,
                "✅ Pago realizado correctamente para el ticket: " + ticketRef,
                "Confirmación de Pago",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error al registrar el pago: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void confirmarPago(String ticketRef) {
        try {
            Payment pago = paymentDAO.findByTicketRef(ticketRef);
            if (pago != null && !pago.getStatus().equalsIgnoreCase("CONFIRMADO")) {
                pago.setStatus("CONFIRMADO");
                paymentDAO.updatePayment(pago);
                JOptionPane.showMessageDialog(null,
                    "✅ Pago confirmado para el ticket: " + ticketRef,
                    "Confirmación Técnica",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "⚠️ El pago ya estaba confirmado o no existe.",
                    "Sin acción",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error al confirmar el pago: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}