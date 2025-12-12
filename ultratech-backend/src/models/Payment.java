package models;

import java.time.LocalDate;

public class Payment {
    private String ticketRef;
    private double amount;
    private LocalDate date;
    private String status;
    private String metodoPago;
    private String imagenUrl;
    private String numeroOperacion;

    // Constructor original
    public Payment(String ticketRef, double amount, LocalDate date, String status) {
        this.ticketRef = ticketRef;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Constructor completo
    public Payment(String ticketRef, double amount, LocalDate date, String status, 
                  String metodoPago, String imagenUrl, String numeroOperacion) {
        this.ticketRef = ticketRef;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.metodoPago = metodoPago;
        this.imagenUrl = imagenUrl;
        this.numeroOperacion = numeroOperacion;
    }

    // Constructor vacío para reflexión
    public Payment() {
    }

    // Getters y Setters
    public String getTicketRef() { return ticketRef; }
    public void setTicketRef(String ticketRef) { this.ticketRef = ticketRef; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }

    // Métodos alternativos para compatibilidad con reflexión
    public String getMetodo() { return metodoPago; }
    public void setMetodo(String metodo) { this.metodoPago = metodo; }
    
    public String getEstado() { return status; }
    public void setEstado(String estado) { this.status = estado; }
    
    public double getMonto() { return amount; }
    public void setMonto(double monto) { this.amount = monto; }
    
    public String getFechaPago() { 
        return date != null ? date.toString() : null; 
    }
    
    public void setFechaPago(String fecha) { 
        if (fecha != null && !fecha.trim().isEmpty()) {
            try {
                // Manejar diferentes formatos de fecha
                String datePart = fecha.split(" ")[0]; // Tomar solo la parte de la fecha
                this.date = LocalDate.parse(datePart);
                System.out.println("✅ Fecha parseada: " + date + " desde: " + fecha);
            } catch (Exception e) {
                System.err.println("❌ Error parseando fecha: " + fecha + " - " + e.getMessage());
                this.date = LocalDate.now();
            }
        } else {
            this.date = LocalDate.now();
        }
    }

    // Para debugging
    @Override
    public String toString() {
        return "Payment{" +
                "ticketRef='" + ticketRef + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", numeroOperacion='" + numeroOperacion + '\'' +
                '}';
    }
}