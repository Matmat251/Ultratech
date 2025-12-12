/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ultratech;

import managers.TicketManager;
import managers.UserManager;
import managers.PaymentManager;
import models.Ticket;
import models.User;
import models.Payment;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;

/**
 *
 * @author HP
 */
public class ModifyTicketWindow extends javax.swing.JFrame {

        private TicketManager ticketManager;
        private UserManager userManager;
        private String currentTicketId;

        /**
         * Creates new form ModifyTicketWindow
         */
        public ModifyTicketWindow() {
                this.ticketManager = new TicketManager();
                this.userManager = new UserManager();
                initComponents();
                inicializarTecnicos();
                setLocationRelativeTo(null);
        }

        /**
         * Constructor con ID de ticket para cargar automáticamente
         */
        public ModifyTicketWindow(String ticketId) {
                this.ticketManager = new TicketManager();
                this.userManager = new UserManager();
                initComponents();
                inicializarTecnicos();
                setLocationRelativeTo(null);
                if (ticketId != null && !ticketId.isEmpty()) {
                        loadTicketById(ticketId);
                }
        }

        /**
         * Inicializa el combo de técnicos y carga IDs de tickets disponibles
         */
        private void inicializarTecnicos() {
                tecnicos.removeAllItems();
                tecnicos.addItem("");
                try {
                        for (User u : userManager.getAllUsers()) {
                                if (u != null && "TECNICO".equalsIgnoreCase(u.getRol())) {
                                        String nombre = (u.getNombre() != null ? u.getNombre().trim() : "") + " " +
                                                        (u.getApellido() != null ? u.getApellido().trim() : "");
                                        nombre = nombre.trim();
                                        if (!nombre.isEmpty()) {
                                                tecnicos.addItem(nombre);
                                        }
                                }
                        }
                } catch (Exception e) {
                        System.err.println("Error cargando técnicos: " + e.getMessage());
                }

                // Cargar lista de tickets existentes como sugerencia
                cargarTicketsDisponibles();
        }

        /**
         * Carga los IDs de tickets disponibles y muestra sugerencia
         */
        private void cargarTicketsDisponibles() {
                try {
                        Ticket[] tickets = ticketManager.getAllTickets();
                        if (tickets != null && tickets.length > 0) {
                                StringBuilder sb = new StringBuilder("IDs disponibles: ");
                                int count = 0;
                                for (Ticket t : tickets) {
                                        if (count > 0)
                                                sb.append(", ");
                                        sb.append(t.getId());
                                        count++;
                                        if (count >= 5) {
                                                sb.append("... (").append(tickets.length).append(" total)");
                                                break;
                                        }
                                }
                                // Mostrar hint en el título de la ventana
                                setTitle("Modificar Ticket - " + sb.toString());
                        }
                } catch (Exception e) {
                        System.err.println("Error cargando tickets: " + e.getMessage());
                }
        }

        /**
         * Carga un ticket por su ID
         */
        public void loadTicketById(String id) {
                if (id == null || id.trim().isEmpty())
                        return;
                txtidedelticket.setText(id.trim());
                buscarTicket();
        }

        /**
         * Busca y carga el ticket en los campos
         */
        private void buscarTicket() {
                String id = txtidedelticket.getText().trim();
                if (id.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ingrese un ID de ticket", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                Ticket ticket = ticketManager.findTicketById(id);
                if (ticket == null) {
                        JOptionPane.showMessageDialog(this, "No se encontró el ticket con ID: " + id, "No encontrado",
                                        JOptionPane.WARNING_MESSAGE);
                        limpiarCampos();
                        return;
                }

                currentTicketId = id;
                cargarDatosTicket(ticket);
        }

        /**
         * Carga los datos del ticket en los campos del formulario
         */
        private void cargarDatosTicket(Ticket t) {
                txtdni1.setText(safe(t.getDni()));
                txtcliente.setText(safe(t.getCliente()));
                txtcorreo.setText(safe(t.getCorreo()));
                txtcelular.setText(safe(t.getCelular()));
                txtdescripcion.setText(safe(t.getDescripcion()));
                txtfechadecreacion.setText(safe(t.getFechaCreacion()));
                txtfechafin.setText(safe(t.getFechaFin()));

                selectComboItem(equipo, t.getEquipo());
                selectComboItem(estado, t.getEstado());
                selectComboItem(prioridad, t.getPrioridad());
                selectComboItem(tecnicos, t.getTecnico());

                chkDiagnosticoPagado.setSelected(t.isDiagnosticoPagado());
                txtMontoReparacion.setText(String.format("%.2f", t.getMontoReparacion()));
        }

        /**
         * Guarda los cambios del ticket
         */
        private void guardarTicket() {
                if (currentTicketId == null || currentTicketId.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Primero busque un ticket para modificar", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                Ticket ticket = ticketManager.findTicketById(currentTicketId);
                if (ticket == null) {
                        JOptionPane.showMessageDialog(this, "El ticket ya no existe", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                // Actualizar datos
                ticket.setDni(txtdni1.getText().trim());
                ticket.setCliente(txtcliente.getText().trim());
                ticket.setCorreo(txtcorreo.getText().trim());
                ticket.setCelular(txtcelular.getText().trim());
                ticket.setDescripcion(txtdescripcion.getText().trim());
                ticket.setEquipo(String.valueOf(equipo.getSelectedItem()).trim());
                ticket.setEstado(String.valueOf(estado.getSelectedItem()).trim());
                ticket.setPrioridad(String.valueOf(prioridad.getSelectedItem()).trim());
                ticket.setTecnico(String.valueOf(tecnicos.getSelectedItem()).trim());
                ticket.setFechaFin(txtfechafin.getText().trim());
                ticket.setDiagnosticoPagado(chkDiagnosticoPagado.isSelected());

                try {
                        double monto = Double.parseDouble(txtMontoReparacion.getText().trim().replace(",", "."));
                        ticket.setMontoReparacion(monto);
                } catch (NumberFormatException e) {
                        ticket.setMontoReparacion(0.0);
                }

                try {
                        ticketManager.updateTicket(ticket);
                        JOptionPane.showMessageDialog(this, "Ticket actualizado correctamente", "Éxito",
                                        JOptionPane.INFORMATION_MESSAGE);
                        refreshParentLists();
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el ticket: " + e.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        /**
         * Limpia todos los campos
         */
        private void limpiarCampos() {
                currentTicketId = null;
                txtdni1.setText("");
                txtcliente.setText("");
                txtcorreo.setText("");
                txtcelular.setText("");
                txtdescripcion.setText("");
                txtfechadecreacion.setText("");
                txtfechafin.setText("");
                txtMontoReparacion.setText("0.00");
                if (equipo.getItemCount() > 0)
                        equipo.setSelectedIndex(0);
                if (estado.getItemCount() > 0)
                        estado.setSelectedIndex(0);
                if (prioridad.getItemCount() > 0)
                        prioridad.setSelectedIndex(0);
                if (tecnicos.getItemCount() > 0)
                        tecnicos.setSelectedIndex(0);
                chkDiagnosticoPagado.setSelected(false);
        }

        private void selectComboItem(javax.swing.JComboBox<String> combo, String value) {
                if (combo == null || value == null)
                        return;
                for (int i = 0; i < combo.getItemCount(); i++) {
                        if (value.equalsIgnoreCase(String.valueOf(combo.getItemAt(i)).trim())) {
                                combo.setSelectedIndex(i);
                                return;
                        }
                }
        }

        private void refreshParentLists() {
                java.awt.Window[] wins = java.awt.Window.getWindows();
                for (java.awt.Window w : wins) {
                        if (w == null)
                                continue;
                        try {
                                java.lang.reflect.Method r = w.getClass().getMethod("refresh");
                                r.invoke(w);
                        } catch (NoSuchMethodException ignored) {
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                }
        }

        private String safe(String s) {
                return s == null ? "" : s;
        }

        /**
         * Muestra el comprobante de pago del ticket actual
         */
        private void verComprobante() {
                if (currentTicketId == null || currentTicketId.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Primero busque un ticket.", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                try {
                        PaymentManager paymentManager = new PaymentManager();
                        Payment pago = paymentManager.obtenerPagoPorTicket(currentTicketId);

                        if (pago == null) {
                                JOptionPane.showMessageDialog(this, "No hay pagos registrados para este ticket.",
                                                "Sin comprobante", JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }

                        String imagenUrl = pago.getImagenUrl();
                        if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "El pago no tiene comprobante adjunto.",
                                                "Sin comprobante", JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }

                        // Ruta base de Tomcat donde están las imágenes
                        String basePath = "C:/apache-tomcat-11.0.11/apache-tomcat-11.0.11/webapps/toptechweb";
                        String fullPath;

                        if (imagenUrl.startsWith("uploads/")) {
                                fullPath = basePath + "/" + imagenUrl;
                        } else if (imagenUrl.startsWith("/uploads/")) {
                                fullPath = basePath + imagenUrl;
                        } else {
                                fullPath = basePath + "/uploads/pagos/" + imagenUrl;
                        }

                        File imageFile = new File(fullPath);

                        if (!imageFile.exists()) {
                                JOptionPane.showMessageDialog(this,
                                                "No se encontró el archivo de comprobante.\nRuta: " + fullPath,
                                                "Archivo no encontrado", JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        // Mostrar imagen en un diálogo
                        ImageIcon icon = new ImageIcon(fullPath);
                        // Escalar si es muy grande
                        int maxW = 600, maxH = 800;
                        int w = icon.getIconWidth(), h = icon.getIconHeight();
                        if (w > maxW || h > maxH) {
                                double scale = Math.min((double) maxW / w, (double) maxH / h);
                                Image scaled = icon.getImage().getScaledInstance((int) (w * scale), (int) (h * scale),
                                                Image.SCALE_SMOOTH);
                                icon = new ImageIcon(scaled);
                        }

                        JOptionPane.showMessageDialog(this, icon,
                                        "Comprobante de Pago - " + currentTicketId,
                                        JOptionPane.PLAIN_MESSAGE);

                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                        "Error al cargar el comprobante: " + e.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                }
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtidedelticket = new javax.swing.JTextPane();
        txtDESCRIPCION = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        equipo = new javax.swing.JComboBox<>();
        txtequipotitu = new javax.swing.JLabel();
        txtclientetitu = new javax.swing.JLabel();
        txttecnico = new javax.swing.JLabel();
        txtidticket = new javax.swing.JLabel();
        txttoptech = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextPane();
        estado = new javax.swing.JComboBox<>();
        txttituloestadotitu = new javax.swing.JLabel();
        prioridad = new javax.swing.JComboBox<>();
        tecnicos = new javax.swing.JComboBox<>();
        txttitulocreaciontitu = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtfechadecreacion = new javax.swing.JTextPane();
        txtfechafintitu = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtfechafin = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtcliente = new javax.swing.JTextPane();
        txtMODIFICARTicket1 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        txtTITULODNI = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtdni1 = new javax.swing.JTextPane();
        txtequipotitu1 = new javax.swing.JLabel();
        chkDiagnosticoPagado = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        txtMontoReparacion = new javax.swing.JTextField();
        txttituloestadotitu1 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtcelular = new javax.swing.JTextPane();
        txttituloestadotitu2 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtcorreo = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1030, 607));
        jPanel1.setMinimumSize(new java.awt.Dimension(1030, 607));

        jPanel3.setBackground(new java.awt.Color(51, 102, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setViewportView(txtidedelticket);

        txtDESCRIPCION.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtDESCRIPCION.setText("DESCRIPCION");

        jSeparator1.setBackground(new java.awt.Color(51, 102, 255));
        jSeparator1.setForeground(new java.awt.Color(51, 102, 255));

        jSeparator2.setBackground(new java.awt.Color(51, 102, 255));
        jSeparator2.setForeground(new java.awt.Color(51, 102, 255));

        equipo.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        equipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OTROS", "PC", "LAPTOP", "IMPRESORA", " " }));
        equipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipoActionPerformed(evt);
            }
        });

        txtequipotitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtequipotitu.setText("EQUIPO");

        txtclientetitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtclientetitu.setText("CLIENTE");

        txttecnico.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        txttecnico.setText("# TECNICO");

        txtidticket.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtidticket.setText("ID TICKET");

        txttoptech.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 48)); // NOI18N
        txttoptech.setText("ultra tech ");

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/utils/LOGO1.png"))); // NOI18N

        btnGuardar.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(txtdescripcion);

        estado.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASIGNADO", "ATENCION", "SOLUCIONADO", "CANCELADO", " " }));
        estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estadoActionPerformed(evt);
            }
        });

        txttituloestadotitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txttituloestadotitu.setText("ESTADO");

        prioridad.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        prioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NORMAL", "***ALTA***", " " }));
        prioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prioridadActionPerformed(evt);
            }
        });

        tecnicos.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        tecnicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tecnicosActionPerformed(evt);
            }
        });

        txttitulocreaciontitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txttitulocreaciontitu.setText("FECHA DE CREACION");

        jScrollPane5.setViewportView(txtfechadecreacion);

        txtfechafintitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtfechafintitu.setText("FECHA DE FIN");

        jScrollPane7.setViewportView(txtfechafin);

        jScrollPane8.setViewportView(txtcliente);

        txtMODIFICARTicket1.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        txtMODIFICARTicket1.setText("MODIFICAR TICKET");

        btnBuscar.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        txtTITULODNI.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtTITULODNI.setText("D.N.I / RUC");

        jScrollPane9.setViewportView(txtdni1);

        txtequipotitu1.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtequipotitu1.setText("PRIORIDAD");

        chkDiagnosticoPagado.setText("Diagnóstico pagado");

        jLabel1.setBackground(new java.awt.Color(255, 255, 0));
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("MONTO REPARACION:");

        txttituloestadotitu1.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txttituloestadotitu1.setText("CORREO");

        jScrollPane10.setViewportView(txtcelular);

        txttituloestadotitu2.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txttituloestadotitu2.setText("CELULAR");

        jScrollPane11.setViewportView(txtcorreo);

        jButton1.setText("VER CAPTURAS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(397, 397, 397)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(txtMODIFICARTicket1)
                                        .addGap(69, 69, 69))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtTITULODNI)
                                            .addComponent(txtclientetitu)
                                            .addComponent(txtidticket))
                                        .addGap(45, 45, 45)))
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(381, 381, 381)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(txtDESCRIPCION)
                                    .addComponent(txtequipotitu1)
                                    .addComponent(txttituloestadotitu2)
                                    .addComponent(txttituloestadotitu1)
                                    .addComponent(txttituloestadotitu)
                                    .addComponent(txtequipotitu))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkDiagnosticoPagado)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtMontoReparacion, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prioridad, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(406, 406, 406)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtfechafintitu)
                                                    .addComponent(txttitulocreaciontitu))))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(124, 124, 124)
                                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(676, 676, 676)
                                .addComponent(txttecnico))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(txttoptech, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39)
                        .addComponent(tecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(txttoptech)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(txtMODIFICARTicket1)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBuscar)
                                    .addComponent(txtidticket))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(txtTITULODNI)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtclientetitu)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtequipotitu))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txttituloestadotitu)
                                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(logo)))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 62, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txttecnico)
                                            .addComponent(tecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(37, 37, 37)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txttitulocreaciontitu)
                                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(41, 41, 41)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtfechafintitu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(txttituloestadotitu1)
                                        .addGap(24, 24, 24)
                                        .addComponent(txttituloestadotitu2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtequipotitu1)
                                            .addComponent(prioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDESCRIPCION))))
                                .addGap(30, 30, 30)
                                .addComponent(chkDiagnosticoPagado)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel1)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtMontoReparacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(26, 26, 26)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        private void equipoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_equipoActionPerformed
        }// GEN-LAST:event_equipoActionPerformed

        private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuardarActionPerformed
                guardarTicket();
        }// GEN-LAST:event_btnGuardarActionPerformed

        private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelarActionPerformed
                dispose();
        }// GEN-LAST:event_btnCancelarActionPerformed

        private void estadoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_estadoActionPerformed
        }// GEN-LAST:event_estadoActionPerformed

        private void prioridadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_prioridadActionPerformed
        }// GEN-LAST:event_prioridadActionPerformed

        private void tecnicosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tecnicosActionPerformed
        }// GEN-LAST:event_tecnicosActionPerformed

        private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBuscarActionPerformed
                buscarTicket();
        }// GEN-LAST:event_btnBuscarActionPerformed

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
                // VER CAPTURAS - abre PagoDialog
                try {
                        String ticketId = txtidedelticket.getText().trim();
                        if (!ticketId.isEmpty()) {
                                PagoDialog dialog = new PagoDialog(this, ticketId);
                                dialog.setVisible(true);
                        } else {
                                javax.swing.JOptionPane.showMessageDialog(this, "Primero busque un ticket");
                        }
                } catch (Exception e) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
        }// GEN-LAST:event_jButton1ActionPerformed

        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                new ModifyTicketWindow().setVisible(true);
                        }
                });
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JCheckBox chkDiagnosticoPagado;
    private javax.swing.JComboBox<String> equipo;
    private javax.swing.JComboBox<String> estado;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel logo;
    private javax.swing.JComboBox<String> prioridad;
    private javax.swing.JComboBox<String> tecnicos;
    private javax.swing.JLabel txtDESCRIPCION;
    private javax.swing.JLabel txtMODIFICARTicket1;
    private javax.swing.JTextField txtMontoReparacion;
    private javax.swing.JLabel txtTITULODNI;
    private javax.swing.JTextPane txtcelular;
    private javax.swing.JTextPane txtcliente;
    private javax.swing.JLabel txtclientetitu;
    private javax.swing.JTextPane txtcorreo;
    private javax.swing.JTextPane txtdescripcion;
    private javax.swing.JTextPane txtdni1;
    private javax.swing.JLabel txtequipotitu;
    private javax.swing.JLabel txtequipotitu1;
    private javax.swing.JTextPane txtfechadecreacion;
    private javax.swing.JTextPane txtfechafin;
    private javax.swing.JLabel txtfechafintitu;
    private javax.swing.JTextPane txtidedelticket;
    private javax.swing.JLabel txtidticket;
    private javax.swing.JLabel txttecnico;
    private javax.swing.JLabel txttitulocreaciontitu;
    private javax.swing.JLabel txttituloestadotitu;
    private javax.swing.JLabel txttituloestadotitu1;
    private javax.swing.JLabel txttituloestadotitu2;
    private javax.swing.JLabel txttoptech;
    // End of variables declaration//GEN-END:variables
}
