package ultratech;

import dao.PaymentDAO;
import managers.TicketManager;
import models.Payment;
import models.Ticket;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

/**
 * PagoDialog - Versión fusionada CORREGIDA con tabla funcional
 */
public class PagoDialog extends JDialog {
    private String ticketId;

    public PagoDialog(JFrame parent, String ticketId) {
        super(parent, "Verificación de Pagos - " + ticketId, true);
        this.ticketId = ticketId;
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        setSize(800, 450);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(198, 227, 255));
        getContentPane().add(panel);

        // Título
        JLabel lblTitle = new JLabel("💳 VERIFICACIÓN DE PAGOS");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        lblTitle.setBounds(20, 10, 300, 24);
        panel.add(lblTitle);

        // Información del ticket
        JLabel lblTicketInfo = new JLabel("📋 Ticket: " + ticketId);
        lblTicketInfo.setFont(lblTicketInfo.getFont().deriveFont(Font.PLAIN, 13f));
        lblTicketInfo.setBounds(20, 40, 400, 20);
        panel.add(lblTicketInfo);

        try {
            Ticket ticket = new TicketManager().findTicketById(ticketId);
            if (ticket != null) {
                JLabel lblCliente = new JLabel("👤 Cliente: " + ticket.getCliente());
                lblCliente.setBounds(20, 65, 400, 20);
                panel.add(lblCliente);

                JLabel lblEquipo = new JLabel("💻 Equipo: " + ticket.getEquipo() + " | 💰 Monto: S/ " + String.format("%.2f", ticket.getMontoReparacion()));
                lblEquipo.setBounds(20, 90, 500, 20);
                panel.add(lblEquipo);
            }
        } catch (Exception e) {
            JLabel lblError = new JLabel("⚠️ Error al cargar información del ticket");
            lblError.setBounds(20, 65, 400, 20);
            lblError.setForeground(Color.RED);
            panel.add(lblError);
        }

        // Panel de instrucciones
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBackground(Color.WHITE);
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("📌 INSTRUCCIONES PARA VERIFICAR PAGOS"));
        instructionsPanel.setBounds(20, 125, 750, 180);

        JLabel inst1 = new JLabel("1. La lista de pagos pendientes se abrirá automáticamente");
        JLabel inst2 = new JLabel("2. Selecciona un pago de la tabla");
        JLabel inst3 = new JLabel("3. Haz clic en 'VER COMPROBANTE' para revisar la imagen del pago");
        JLabel inst4 = new JLabel("4. Si el pago es correcto, haz clic en 'MARCAR COMO VERIFICADO'");
        JLabel inst5 = new JLabel("5. El sistema actualizará el estado del pago automáticamente");

        inst1.setAlignmentX(Component.LEFT_ALIGNMENT);
        inst2.setAlignmentX(Component.LEFT_ALIGNMENT);
        inst3.setAlignmentX(Component.LEFT_ALIGNMENT);
        inst4.setAlignmentX(Component.LEFT_ALIGNMENT);
        inst5.setAlignmentX(Component.LEFT_ALIGNMENT);

        inst1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inst2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inst3.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inst4.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inst5.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        instructionsPanel.add(inst1);
        instructionsPanel.add(inst2);
        instructionsPanel.add(inst3);
        instructionsPanel.add(inst4);
        instructionsPanel.add(inst5);

        panel.add(instructionsPanel);

        // Botones
        JButton btnVerPagos = new JButton("👁️ VER PAGOS PENDIENTES");
        btnVerPagos.setBounds(200, 330, 180, 36);
        btnVerPagos.setBackground(new Color(70, 130, 180));
        btnVerPagos.setForeground(Color.WHITE);
        btnVerPagos.setFocusPainted(false);
        btnVerPagos.addActionListener(e -> mostrarPagosPendientes());
        panel.add(btnVerPagos);

        JButton btnDiagnostico = new JButton("🔧 DIAGNOSTICO");
        btnDiagnostico.setBounds(400, 330, 130, 36);
        btnDiagnostico.setBackground(new Color(255, 165, 0));
        btnDiagnostico.setForeground(Color.WHITE);
        btnDiagnostico.setFocusPainted(false);
        btnDiagnostico.addActionListener(e -> diagnosticoCompleto());
        panel.add(btnDiagnostico);

        JButton btnCerrar = new JButton("❌ CERRAR");
        btnCerrar.setBounds(550, 330, 100, 36);
        btnCerrar.setBackground(new Color(220, 53, 69));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnCerrar);

        // Cargar y mostrar automáticamente los pagos pendientes
        SwingUtilities.invokeLater(() -> mostrarPagosPendientes());
    }

    // ✅ CORREGIDO: Mostrar diálogo con pagos pendientes con diseño funcional
    private void mostrarPagosPendientes() {
        System.out.println("🔄 PagoDialog - Mostrando pagos pendientes...");

        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            List<Payment> pagos = paymentDAO.getPagosPendientes();

            System.out.println("📊 Pagos obtenidos de BD: " + pagos.size());

            if (pagos.isEmpty()) {
                System.out.println("ℹ️ No hay pagos pendientes en la BD");
                JOptionPane.showMessageDialog(this,
                    "✅ No hay pagos pendientes de verificación.\n\n" +
                    "Todos los pagos están al día.",
                    "Sin Pagos Pendientes",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // CORREGIDO: Usar BorderLayout como en el primer código funcional
            JDialog pagosDialog = new JDialog(this, "PAGOS PENDIENTES - TOP TECH", true);
            pagosDialog.setSize(1000, 600);
            pagosDialog.setLocationRelativeTo(this);
            pagosDialog.setLayout(new BorderLayout(10, 10));
            pagosDialog.getContentPane().setBackground(new Color(198, 227, 255));

            // Header del diálogo (como en el primer código)
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 73, 94));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JLabel titleLabel = new JLabel("📋 PAGOS PENDIENTES DE VERIFICACIÓN");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);

            JLabel countLabel = new JLabel("Total: " + pagos.size() + " pago(s) pendiente(s)");
            countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            countLabel.setForeground(new Color(236, 240, 241));

            headerPanel.add(titleLabel, BorderLayout.NORTH);
            headerPanel.add(countLabel, BorderLayout.SOUTH);

            // Tabla con pagos pendientes (EXACTAMENTE como en el primer código funcional)
            String[] columnNames = {"Ticket", "Cliente", "Monto", "Método", "Fecha", "Operación", "Estado", "Comprobante"};
            Object[][] data = new Object[pagos.size()][8];

            for (int i = 0; i < pagos.size(); i++) {
                Payment pago = pagos.get(i);
                data[i][0] = pago.getTicketRef();
                
                // Obtener nombre del cliente
                try {
                    Ticket ticket = new TicketManager().findTicketById(pago.getTicketRef());
                    data[i][1] = ticket != null ? ticket.getCliente() : "N/A";
                } catch (Exception e) {
                    data[i][1] = "N/A";
                }
                
                data[i][2] = "S/ " + String.format("%.2f", pago.getAmount());
                data[i][3] = pago.getMetodoPago();
                data[i][4] = pago.getDate() != null ? pago.getDate().toString() : "N/A";
                data[i][5] = pago.getNumeroOperacion() != null ? pago.getNumeroOperacion() : "N/A";
                data[i][6] = pago.getStatus();
                data[i][7] = pago.getImagenUrl() != null && !pago.getImagenUrl().isEmpty() ? "✅ Disponible" : "❌ Sin imagen";

                System.out.println("📋 Pago en tabla: " + pago.getTicketRef() + " - Imagen: " + pago.getImagenUrl());
            }

            JTable table = new JTable(data, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(70, 130, 180));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setReorderingAllowed(false);
            table.setSelectionBackground(new Color(173, 216, 230));
            table.setSelectionForeground(Color.BLACK);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Panel de botones (como en el primer código)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
            buttonPanel.setBackground(new Color(198, 227, 255));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton verImagenBtn = new JButton("👁️ VER COMPROBANTE");
            verImagenBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            verImagenBtn.setBackground(new Color(70, 130, 180));
            verImagenBtn.setForeground(Color.WHITE);
            verImagenBtn.setFocusPainted(false);
            verImagenBtn.setPreferredSize(new Dimension(200, 40));

            verImagenBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Payment pagoSeleccionado = pagos.get(selectedRow);
                    System.out.println("🎯 Pago seleccionado: " + pagoSeleccionado.getTicketRef());
                    System.out.println("🖼️ Ruta de imagen: " + pagoSeleccionado.getImagenUrl());
                    mostrarImagenPago(pagoSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(pagosDialog,
                        "⚠️ Por favor, selecciona un pago de la tabla para ver el comprobante.",
                        "Selección Requerida",
                        JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton verificarBtn = new JButton("✅ MARCAR COMO VERIFICADO");
            verificarBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            verificarBtn.setBackground(new Color(40, 167, 69));
            verificarBtn.setForeground(Color.WHITE);
            verificarBtn.setFocusPainted(false);
            verificarBtn.setPreferredSize(new Dimension(240, 40));

            verificarBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Payment pagoSeleccionado = pagos.get(selectedRow);
                    verificarPago(pagoSeleccionado, pagosDialog);
                } else {
                    JOptionPane.showMessageDialog(pagosDialog,
                        "⚠️ Por favor, selecciona un pago de la tabla para verificar.",
                        "Selección Requerida",
                        JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton actualizarBtn = new JButton("🔄 ACTUALIZAR");
            actualizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            actualizarBtn.setBackground(new Color(255, 193, 7));
            actualizarBtn.setForeground(Color.WHITE);
            actualizarBtn.setFocusPainted(false);
            actualizarBtn.setPreferredSize(new Dimension(160, 40));
            actualizarBtn.addActionListener(e -> {
                pagosDialog.dispose();
                mostrarPagosPendientes();
            });

            JButton cerrarBtn = new JButton("❌ CERRAR");
            cerrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            cerrarBtn.setBackground(new Color(220, 53, 69));
            cerrarBtn.setForeground(Color.WHITE);
            cerrarBtn.setFocusPainted(false);
            cerrarBtn.setPreferredSize(new Dimension(130, 40));
            cerrarBtn.addActionListener(e -> pagosDialog.dispose());

            buttonPanel.add(verImagenBtn);
            buttonPanel.add(verificarBtn);
            buttonPanel.add(actualizarBtn);
            buttonPanel.add(cerrarBtn);

            // Panel principal del diálogo (como en el primer código)
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(198, 227, 255));
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            pagosDialog.add(mainPanel);
            pagosDialog.setVisible(true);
            System.out.println("✅ Diálogo de pagos pendientes mostrado CORRECTAMENTE");

        } catch (SQLException e) {
            System.err.println("💥 Error al cargar pagos pendientes: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Error al cargar pagos pendientes:\n" + e.getMessage(),
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ Mantener el método mostrarImagenPago del primer código que SÍ funciona
    private void mostrarImagenPago(Payment pago) {
        String imagenUrl = pago.getImagenUrl();
        System.out.println("🖼️ MostrarImagenPago - Ruta de BD: " + imagenUrl);

        if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
            System.out.println("⚠️ imagenUrl es null o vacío");
            JOptionPane.showMessageDialog(this,
                "❌ No hay comprobante disponible para este pago.\n\n" +
                "Ticket: " + pago.getTicketRef() + "\n" +
                "El cliente no subió ninguna imagen de comprobante.",
                "Sin Comprobante",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String serverBaseUrl = "http://localhost:8080/toptechweb";
            String imageFullUrl = serverBaseUrl + "/ImageServlet?path=" + imagenUrl;

            System.out.println("🔗 URL completa de imagen: " + imageFullUrl);
            System.out.println("📂 Ruta desde BD: " + imagenUrl);

            // Descargar imagen
            BufferedImage imagen = descargarImagen(imageFullUrl);

            if (imagen == null) {
                throw new Exception("No se pudo cargar la imagen desde el servidor");
            }

            System.out.println("✅ Imagen cargada correctamente: " + imagen.getWidth() + "x" + imagen.getHeight());

            // CORREGIDO: Usar BorderLayout para el diálogo de imagen como en el primer código
            JDialog imageDialog = new JDialog(this, "Comprobante de Pago - " + pago.getTicketRef(), true);
            imageDialog.setSize(850, 700);
            imageDialog.setLocationRelativeTo(this);
            imageDialog.setLayout(new BorderLayout(10, 10));
            imageDialog.getContentPane().setBackground(new Color(198, 227, 255));

            // Header con información
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 73, 94));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JLabel titleLabel = new JLabel("📸 COMPROBANTE DE PAGO");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.NORTH);

            // Panel de información del pago
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 15, 8));
            infoPanel.setBackground(new Color(236, 240, 241));
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1)
            ));

            addInfoLabel(infoPanel, "📋 Ticket:", pago.getTicketRef());
            addInfoLabel(infoPanel, "💰 Monto:", "S/ " + String.format("%.2f", pago.getAmount()));
            addInfoLabel(infoPanel, "💳 Método:", pago.getMetodoPago());
            addInfoLabel(infoPanel, "🔢 Operación:", pago.getNumeroOperacion() != null ? pago.getNumeroOperacion() : "N/A");
            addInfoLabel(infoPanel, "📅 Fecha:", pago.getDate() != null ? pago.getDate().toString() : "N/A");
            addInfoLabel(infoPanel, "📌 Estado:", pago.getStatus());

            // Escalar imagen
            int maxWidth = 750;
            int maxHeight = 450;
            Image scaledImage = imagen;

            if (imagen.getWidth() > maxWidth || imagen.getHeight() > maxHeight) {
                double scale = Math.min(
                    (double) maxWidth / imagen.getWidth(),
                    (double) maxHeight / imagen.getHeight()
                );
                int newWidth = (int) (imagen.getWidth() * scale);
                int newHeight = (int) (imagen.getHeight() * scale);
                scaledImage = imagen.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            }

            JLabel imagenLabel = new JLabel(new ImageIcon(scaledImage));
            imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JScrollPane scrollPane = new JScrollPane(imagenLabel);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
            scrollPane.setBackground(Color.WHITE);

            // Botón cerrar
            JButton cerrarBtn = new JButton("❌ CERRAR");
            cerrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            cerrarBtn.setBackground(new Color(220, 53, 69));
            cerrarBtn.setForeground(Color.WHITE);
            cerrarBtn.setFocusPainted(false);
            cerrarBtn.setPreferredSize(new Dimension(150, 40));
            cerrarBtn.addActionListener(e -> imageDialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(new Color(198, 227, 255));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(cerrarBtn);

            // Panel principal
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mainPanel.setBackground(new Color(198, 227, 255));
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(infoPanel, BorderLayout.CENTER);

            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setBackground(new Color(198, 227, 255));
            imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            imagePanel.add(scrollPane, BorderLayout.CENTER);

            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setBackground(new Color(198, 227, 255));
            contentPanel.add(mainPanel, BorderLayout.NORTH);
            contentPanel.add(imagePanel, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            imageDialog.add(contentPanel);
            imageDialog.setVisible(true);

        } catch (Exception e) {
            System.err.println("💥 Error al cargar imagen: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Error al cargar comprobante:\n\n" +
                "Ticket: " + pago.getTicketRef() + "\n" +
                "Ruta: " + imagenUrl + "\n" +
                "Error: " + e.getMessage() + "\n\n" +
                "Verifica:\n" +
                "• Que Tomcat esté ejecutándose en puerto 8080\n" +
                "• Que ImageServlet esté configurado\n" +
                "• Que el archivo exista en Tomcat",
                "Error al Cargar Imagen",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para añadir labels de información
    private void addInfoLabel(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(new Color(52, 73, 94));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(new Color(44, 62, 80));

        panel.add(lblLabel);
        panel.add(lblValue);
    }

    private BufferedImage descargarImagen(String imageUrl) {
        System.out.println("📥 DescargarImagen - URL: " + imageUrl);

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "TopTech-Desktop-App/1.0");
            connection.setRequestProperty("Accept", "image/*");

            int responseCode = connection.getResponseCode();
            System.out.println("📞 Código de respuesta HTTP: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("❌ Error HTTP: " + responseCode + " - " + connection.getResponseMessage());
                return null;
            }

            try (InputStream inputStream = connection.getInputStream()) {
                BufferedImage imagen = ImageIO.read(inputStream);
                if (imagen == null) {
                    System.out.println("❌ No se pudo decodificar la imagen");
                    return null;
                }
                System.out.println("✅ Imagen descargada: " + imagen.getWidth() + "x" + imagen.getHeight());
                return imagen;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            System.out.println("❌ Error descargando imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void diagnosticoCompleto() {
        try {
            System.out.println("🔍 INICIANDO DIAGNÓSTICO COMPLETO");

            PaymentDAO paymentDAO = new PaymentDAO();
            List<Payment> pagos = paymentDAO.getPagosPendientes();

            System.out.println("📊 Pagos en BD: " + pagos.size());

            if (pagos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "ℹ️ No hay pagos pendientes para diagnosticar",
                    "Diagnóstico",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Payment primerPago = pagos.get(0);
            System.out.println("🎯 Primer pago: " + primerPago.getTicketRef());
            System.out.println("🖼️ Ruta imagen: " + primerPago.getImagenUrl());

            String testUrl = "http://localhost:8080/toptechweb/ImageServlet?path=" + primerPago.getImagenUrl();
            System.out.println("🧪 Probando URL: " + testUrl);

            BufferedImage imagen = descargarImagen(testUrl);
            if (imagen != null) {
                System.out.println("✅ DIAGNÓSTICO EXITOSO");
                JOptionPane.showMessageDialog(this,
                    "✅ DIAGNÓSTICO EXITOSO\n\n" +
                    "La conexión con el servidor funciona correctamente.\n\n" +
                    "Detalles:\n" +
                    "• Ticket: " + primerPago.getTicketRef() + "\n" +
                    "• Archivo: " + primerPago.getImagenUrl() + "\n" +
                    "• Dimensiones: " + imagen.getWidth() + "x" + imagen.getHeight() + " px\n" +
                    "• Servidor: http://localhost:8080/toptechweb",
                    "Diagnóstico Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("❌ DIAGNÓSTICO FALLIDO");
                JOptionPane.showMessageDialog(this,
                    "❌ DIAGNÓSTICO FALLIDO\n\n" +
                    "No se pudo cargar la imagen desde el servidor.\n\n" +
                    "Verifica:\n" +
                    "1. Que Tomcat esté corriendo en puerto 8080\n" +
                    "2. Que ImageServlet esté desplegado\n" +
                    "3. URL de prueba:\n" + testUrl,
                    "Diagnóstico Fallido",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("💥 Error en diagnóstico: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Error en diagnóstico:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarPago(Payment pago, JDialog parentDialog) {
        int confirm = JOptionPane.showConfirmDialog(parentDialog,
            "¿Marcar el pago como VERIFICADO?\n\n" +
            "📋 Detalles del pago:\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "• Ticket: " + pago.getTicketRef() + "\n" +
            "• Monto: S/ " + String.format("%.2f", pago.getAmount()) + "\n" +
            "• Método: " + pago.getMetodoPago() + "\n" +
            "• Operación: " + (pago.getNumeroOperacion() != null ? pago.getNumeroOperacion() : "N/A") + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Esta acción actualizará el estado del pago a VERIFICADO.",
            "✅ Confirmar Verificación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PaymentDAO paymentDAO = new PaymentDAO();
                paymentDAO.actualizarEstadoPago(pago.getTicketRef(), "VERIFICADO");

                System.out.println("✅ Pago verificado: " + pago.getTicketRef());

                JOptionPane.showMessageDialog(parentDialog,
                    "✅ PAGO VERIFICADO EXITOSAMENTE\n\n" +
                    "El ticket " + pago.getTicketRef() + " ha sido marcado como VERIFICADO.\n" +
                    "El cliente será notificado del estado de su pago.",
                    "Verificación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

                parentDialog.dispose();

            } catch (SQLException e) {
                System.err.println("❌ Error al verificar pago: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentDialog,
                    "❌ Error al verificar pago:\n\n" + e.getMessage(),
                    "Error de Verificación",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}