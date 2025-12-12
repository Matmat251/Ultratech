/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author yomar
 */

package notifications;

import models.Ticket;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotificationService implements NotificationService {

    private final Session session;
    private final String from;

    public EmailNotificationService(Properties smtpProps) {
        // Requiere: mail.smtp.host, mail.smtp.port, mail.smtp.auth, mail.smtp.starttls.enable
        //           mail.user, mail.password
        // Opcional: mail.from   (si no está, usará mail.user)
        this.from = smtpProps.getProperty("mail.from", smtpProps.getProperty("mail.user"));

        this.session = Session.getInstance(smtpProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    smtpProps.getProperty("mail.user"),
                    smtpProps.getProperty("mail.password")
                );
            }
        });
        // Si quieres ver logs de SMTP:
         this.session.setDebug(true);
    }

    private void send(String to, String subject, String body) {
        if (to == null || to.isEmpty()) return; // sin destinatario, no se envía
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body); // texto plano (simple)
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace(); // En producción: usa logger
        }
    }

    private static String s(String v) { return v == null ? "" : v; }

    @Override
    public void onTicketCreated(Ticket t) {
        String subject = "[TOP TECH] Ticket #" + s(t.getId()) + " creado";
        String body =
            "Hola " + s(t.getCliente()) + ",\n\n" +
            "Tu ticket #" + s(t.getId()) + " fue creado.\n" +
            "Equipo/Asunto: " + s(t.getEquipo()) + "\n" +
            "Prioridad: " + s(t.getPrioridad()) + "\n" +
            "Estado: " + s(t.getEstado()) + "\n" +
            "Descripción: " + s(t.getDescripcion()) + "\n\n" +
            "Gracias por contactarnos.\n— TOP TECH Soporte";
        send(t.getCorreo(), subject, body);
    }

    @Override
    public void onTicketUpdated(Ticket t, String oldEstado) {
        String subject = "[TOP TECH] Ticket #" + s(t.getId()) + " actualizado";
        String body =
            "Hola " + s(t.getCliente()) + ",\n\n" +
            "Tu ticket #" + s(t.getId()) + " cambió de estado:\n" +
            "  " + s(oldEstado) + " → " + s(t.getEstado()) + "\n\n" +
            "Equipo/Asunto: " + s(t.getEquipo()) + "\n" +
            "Prioridad: " + s(t.getPrioridad()) + "\n" +
            "Descripción: " + s(t.getDescripcion()) + "\n\n" +
            "— TOP TECH Soporte";
        send(t.getCorreo(), subject, body);
    }
}