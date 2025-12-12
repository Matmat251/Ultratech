# 🔧 UltraTech - Sistema de Gestión de Tickets para Técnicos

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)

## 📋 Descripción

**UltraTech** es un sistema completo de gestión de tickets diseñado para técnicos de reparación de computadoras. Permite administrar órdenes de servicio, clientes, pagos y seguimiento de reparaciones de manera eficiente.

El proyecto consta de dos componentes principales:
- 🖥️ **Aplicación de Escritorio** (Java Swing) - Para gestión interna de técnicos
- 🌐 **Aplicación Web** (JSP/Servlets) - Portal para clientes

---

## 🚀 Características

### Aplicación de Escritorio
- ✅ Gestión de usuarios (Administradores y Técnicos)
- ✅ Creación y seguimiento de tickets de servicio
- ✅ Registro de pagos
- ✅ Notificaciones por Email y WhatsApp
- ✅ Sistema de autenticación seguro

### Portal Web para Clientes
- ✅ Consulta de estado de tickets
- ✅ Visualización de historial de servicios
- ✅ Pagos en línea (Yape, Tarjeta)
- ✅ Descarga de boletas en PDF
- ✅ Interfaz moderna y responsive

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología |
|------------|------------|
| Backend Desktop | Java SE (Swing) |
| Backend Web | Java EE (Servlets, JSP) |
| Base de Datos | MySQL |
| Servidor Web | Apache Tomcat |
| IDE | NetBeans |
| Generación PDF | iText |

---

## 📁 Estructura del Proyecto

```
Ultratech/
│
├── 📂 ultratech-backend/          # Aplicación de Escritorio
│   ├── src/
│   │   ├── dao/                   # Data Access Objects
│   │   ├── managers/              # Lógica de negocio
│   │   ├── models/                # Entidades (User, Ticket, Payment)
│   │   ├── notifications/         # Servicios de notificación
│   │   ├── ultratech/             # Ventanas de la aplicación
│   │   └── utils/                 # Utilidades
│   └── nbproject/
│
├── 📂 src/java/                   # Backend Web
│   ├── com/ultratech/util/        # Configuración BD
│   └── ultratech/controller/      # Servlets
│
├── 📂 web/                        # Frontend Web
│   ├── assets/
│   │   ├── css/                   # Estilos
│   │   └── images/                # Recursos gráficos
│   ├── index.jsp                  # Página principal
│   ├── tickets.jsp                # Consulta de tickets
│   ├── boleta.jsp                 # Generación de boletas
│   └── payment-success.jsp        # Confirmación de pago
│
└── 📄 README.md
```

---

## ⚙️ Instalación y Configuración

### Requisitos Previos
- JDK 8 o superior
- Apache Tomcat 9+
- MySQL 5.7+
- NetBeans IDE

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Matmat251/Ultratech.git
   ```

2. **Configurar la base de datos**
   - Crear una base de datos MySQL llamada `ultratech`
   - Importar el script SQL (si disponible)

3. **Configurar conexión a BD**
   - Editar `ultratech-backend/src/utils/DBConnection.java`
   - Editar `src/java/com/ultratech/util/DBConfig.java`

4. **Abrir en NetBeans**
   - Importar ambos proyectos
   - Configurar el servidor Tomcat para el proyecto web

5. **Ejecutar**
   - Iniciar la aplicación de escritorio desde `Main.java`
   - Desplegar la aplicación web en Tomcat

---

## 📸 Capturas de Pantalla

> *Próximamente*

---

## 👨‍💻 Autor

**Mathew Tenorio** - *Desarrollo Full Stack*

- GitHub: [@Matmat251](https://github.com/Matmat251)
- Portafolio: [MathewDev](https://matmat251.github.io/PortafolioDev/)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes, por favor abre un issue primero para discutir qué te gustaría cambiar.

---

<p align="center">
  Hecho con ❤️ por MathewDev
</p>
