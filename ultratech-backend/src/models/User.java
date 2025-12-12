/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class User {
    private String dni, nombre, apellido, usuario, contrasena, rol;
    private int id; // ✅ AGREGADO campo id

    // ✅ CONSTRUCTOR ORIGINAL MANTENIDO
    public User(String dni, String nombre, String apellido, String usuario, String contrasena, String rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // ✅ CONSTRUCTOR VACÍO AGREGADO (necesario para DAO)
    public User() {
    }

    // ✅ GETTERS Y SETTERS ORIGINALES MANTENIDOS
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(String rol) { this.rol = rol; }

    // ✅ GETTERS Y SETTERS NUEVOS AGREGADOS
    public int getId() { return id; } // ✅ AGREGADO
    public void setId(int id) { this.id = id; } // ✅ AGREGADO

    // ✅ MÉTODO PARA COMPATIBILIDAD CON LA WEB
    public String getUsername() { return this.usuario; } // ✅ AGREGADO
}