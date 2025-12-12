package models;

public class Ticket {
    private String id, cliente, dni, equipo, descripcion, estado, tecnico, prioridad, fechaCreacion, fechaFin;
    private String correo, celular;
    private boolean diagnosticoPagado;
    private double montoReparacion;

    // Constructor completo para BD
    public Ticket(String id, String cliente, String dni, String equipo, String descripcion, String estado, String tecnico, String prioridad,
                  String fechaCreacion, String fechaFin, String correo, String celular, boolean diagnosticoPagado, double montoReparacion) {
        this.id = id; this.cliente = cliente; this.dni = dni; this.equipo = equipo; this.descripcion = descripcion;
        this.estado = estado; this.tecnico = tecnico; this.prioridad = prioridad; this.fechaCreacion = fechaCreacion;
        this.fechaFin = fechaFin; this.correo = correo; this.celular = celular;
        this.diagnosticoPagado = diagnosticoPagado; this.montoReparacion = montoReparacion;
    }

    // Constructor para crear ticket desde formulario (sin pago ni monto, por defecto)
    public Ticket(String id, String cliente, String dni, String equipo, String descripcion, String estado, String tecnico, String prioridad,
                  String fechaCreacion, String fechaFin, String correo, String celular) {
        this(id, cliente, dni, equipo, descripcion, estado, tecnico, prioridad, fechaCreacion, fechaFin, correo, celular, false, 0.0);
    }

    // Getters y setters
    public String getId() { return id; }
    public String getCliente() { return cliente; }
    public String getDni() { return dni; }
    public String getEquipo() { return equipo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public String getTecnico() { return tecnico; }
    public String getPrioridad() { return prioridad; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaFin() { return fechaFin; }
    public String getCorreo() { return correo; }
    public String getCelular() { return celular; }
    public boolean isDiagnosticoPagado() { return diagnosticoPagado; }
    public double getMontoReparacion() { return montoReparacion; }

    public void setId(String id) { this.id = id; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public void setDni(String dni) { this.dni = dni; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setCelular(String celular) { this.celular = celular; }
    public void setDiagnosticoPagado(boolean diagnosticoPagado) { this.diagnosticoPagado = diagnosticoPagado; }
    public void setMontoReparacion(double montoReparacion) { this.montoReparacion = montoReparacion; }
}