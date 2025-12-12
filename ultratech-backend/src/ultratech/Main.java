/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ultratech;

import managers.UserManager;
import managers.TicketManager;
import models.User;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        TicketManager ticketManager = new TicketManager();

        // ✅ VERIFICAR si el usuario ya existe antes de crearlo
        User usuarioExistente = userManager.findUserByDNI("00000000");
        if (usuarioExistente == null) {
            // Solo crear si no existe
            userManager.addUser(new User("00000000", "Admin", "Sistema", "admini", "admini", "ADMIN"));
            System.out.println("✅ Usuario admin creado exitosamente");
        } else {
            System.out.println("⚠️ Usuario admin ya existe: " + usuarioExistente.getNombre() + " " + usuarioExistente.getApellido());
        }

        LoginWindow login = new LoginWindow(userManager, ticketManager);
        login.setVisible(true);
    }
}