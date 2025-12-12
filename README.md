# UltraTech - Sistema de Gestión de Tickets

## Descripción

UltraTech es un sistema de gestión de tickets diseñado para técnicos de reparación de computadoras. Permite administrar órdenes de servicio, clientes, pagos y seguimiento de reparaciones.

El proyecto consta de dos componentes:
- **Aplicación de Escritorio** (Java Swing) - Gestión interna para técnicos
- **Aplicación Web** (JSP/Servlets) - Portal para clientes

---

## Características

### Aplicación de Escritorio
- Gestión de usuarios (Administradores y Técnicos)
- Creación y seguimiento de tickets de servicio
- Registro de pagos
- Notificaciones por Email y WhatsApp
- Sistema de autenticación seguro

### Portal Web para Clientes
- Consulta de estado de tickets
- Visualización de historial de servicios
- Pagos en línea (Yape, Tarjeta)
- Descarga de boletas en PDF
- Interfaz responsive

---

## Tecnologías Utilizadas

| Componente | Tecnología |
|------------|------------|
| Backend Desktop | Java SE (Swing) |
| Backend Web | Java EE (Servlets, JSP) |
| Base de Datos | MySQL |
| Servidor Web | Apache Tomcat |
| IDE | NetBeans |
| Generación PDF | iText |

---

## Estructura del Proyecto

```
Ultratech/
├── ultratech-backend/          # Aplicación de Escritorio
│   ├── src/
│   │   ├── dao/                # Data Access Objects
│   │   ├── managers/           # Lógica de negocio
│   │   ├── models/             # Entidades (User, Ticket, Payment)
│   │   ├── notifications/      # Servicios de notificación
│   │   ├── ultratech/          # Ventanas de la aplicación
│   │   └── utils/              # Utilidades
│   └── nbproject/
│
├── src/java/                   # Backend Web
│   ├── com/ultratech/util/     # Configuración BD
│   └── ultratech/controller/   # Servlets
│
├── web/                        # Frontend Web
│   ├── assets/
│   │   ├── css/
│   │   └── images/
│   ├── index.jsp
│   ├── tickets.jsp
│   ├── boleta.jsp
│   └── payment-success.jsp
│
└── README.md
```

---

## Instalación y Configuración

### Requisitos Previos
- JDK 8 o superior
- Apache Tomcat 9+
- MySQL 5.7+
- NetBeans IDE

### Pasos de Instalación

1. Clonar el repositorio
   ```bash
   git clone https://github.com/Matmat251/Ultratech.git
   ```

2. Configurar la base de datos
   - Crear una base de datos MySQL llamada `ultratech`
   - Importar el script SQL (si disponible)

3. Configurar conexión a BD
   - Editar `ultratech-backend/src/utils/DBConnection.java`
   - Editar `src/java/com/ultratech/util/DBConfig.java`

4. Abrir en NetBeans
   - Importar ambos proyectos
   - Configurar el servidor Tomcat para el proyecto web

5. Ejecutar
   - Iniciar la aplicación de escritorio desde `Main.java`
   - Desplegar la aplicación web en Tomcat

---

## Autor

**Mathew Tenorio**

- GitHub: [@Matmat251](https://github.com/Matmat251)

---

## Licencia

Este proyecto está bajo la Licencia MIT.
