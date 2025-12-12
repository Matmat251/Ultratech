/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package notifications;

import models.Ticket;

public interface NotificationService {
    void onTicketCreated(Ticket t);
    void onTicketUpdated(Ticket t, String oldEstado);
}

