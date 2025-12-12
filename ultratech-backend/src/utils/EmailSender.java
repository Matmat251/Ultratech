package utils;

public class EmailSender {
    public static void send(String to, String subject, String message) {
        // Aquí va la lógica real JavaMail, pero por ahora solo imprime:
        System.out.println("Enviando email a " + to + ": " + subject + "\n" + message);
    }
}
