package ultratech;

import javax.swing.*;
import managers.TicketManager;
import managers.UserManager;
import models.Ticket;
import models.User;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTicketWindow extends javax.swing.JFrame {
    private TicketManager ticketManager;
    private UserManager userManager;
    private JFrame parentWindow;

    public CreateTicketWindow(TicketManager ticketManager, UserManager userManager, JFrame parentWindow) {
        this.ticketManager = ticketManager;
        this.userManager = userManager;
        this.parentWindow = parentWindow;
        initComponents();
        setTitle("Crear Ticket");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        inicializarDatos();
    }

    public CreateTicketWindow() {
        initComponents();
    }

    private void inicializarDatos() {
        tecnicos.removeAllItems();
        tecnicos.addItem(""); // Opción vacía
        for (User u : userManager.getAllUsers()) {
            if ("TECNICO".equalsIgnoreCase(u.getRol())) {
                tecnicos.addItem(u.getNombre() + " " + u.getApellido());
            }
        }
        txtidedelticket.setText(ticketManager.generarNuevoIdTicket());
        txtidedelticket.setEditable(false); // ID no editable
        txtfechadecreacion.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        txtfechadecreacion.setEditable(false); // Fecha de creación no editable
        txtfechafin.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtprioridad = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtidedelticket = new javax.swing.JTextPane();
        txtDNI = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        equipo = new javax.swing.JComboBox<>();
        txtequipotitu = new javax.swing.JLabel();
        txtclientetitu = new javax.swing.JLabel();
        txttecnico = new javax.swing.JLabel();
        txtcrearticket = new javax.swing.JLabel();
        txttoptech = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextPane();
        estado = new javax.swing.JComboBox<>();
        txtestadotitu = new javax.swing.JLabel();
        prioridad = new javax.swing.JComboBox<>();
        tecnicos = new javax.swing.JComboBox<>();
        txtcreaciontitu = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtfechadecreacion = new javax.swing.JTextPane();
        txtfechafintitu = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtfechafin = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtcelular1 = new javax.swing.JTextPane();
        txtcrearticket1 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtdni1 = new javax.swing.JTextPane();
        txtcelular = new javax.swing.JLabel();
        txtcorreo = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtcliente1 = new javax.swing.JTextPane();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtcorreo1 = new javax.swing.JTextPane();
        chkDiagnosticoPagado = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1030, 607));
        jPanel1.setMinimumSize(new java.awt.Dimension(1030, 607));

        txtprioridad.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtprioridad.setText("PRIORIDAD");

        jPanel3.setBackground(new java.awt.Color(51, 102, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setViewportView(txtidedelticket);

        txtDNI.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtDNI.setText("D.N.I / RUC");

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

        txtcrearticket.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtcrearticket.setText("ID TICKET");

        txttoptech.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 48)); // NOI18N
        txttoptech.setText("ultra tech");

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

        txtestadotitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtestadotitu.setText("ESTADO");

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

        txtcreaciontitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtcreaciontitu.setText("FECHA DE CREACION");

        jScrollPane5.setViewportView(txtfechadecreacion);

        txtfechafintitu.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtfechafintitu.setText("FECHA DE FIN");

        jScrollPane7.setViewportView(txtfechafin);

        jScrollPane8.setViewportView(txtcelular1);

        txtcrearticket1.setFont(new java.awt.Font("Decker", 0, 18)); // NOI18N
        txtcrearticket1.setText("CREAR TICKET");

        txtDescripcion.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtDescripcion.setText("DESCRIPCION");

        jScrollPane9.setViewportView(txtdni1);

        txtcelular.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtcelular.setText("CELULAR");

        txtcorreo.setFont(new java.awt.Font("Decker", 0, 12)); // NOI18N
        txtcorreo.setText("CORREO");

        jScrollPane10.setViewportView(txtcliente1);

        jScrollPane11.setViewportView(txtcorreo1);

        chkDiagnosticoPagado.setText("Diagnóstico pagado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtcrearticket)
                                                .addComponent(txtcrearticket1))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(66, 66, 66)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(txtprioridad)
                                                    .addComponent(txtDNI)
                                                    .addComponent(txtequipotitu)
                                                    .addComponent(txtclientetitu)
                                                    .addComponent(txtcorreo)
                                                    .addComponent(txtcelular)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(87, 87, 87)
                                                .addComponent(txtestadotitu)))
                                        .addGap(594, 594, 594))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txttecnico, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(598, 598, 598))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(295, 295, 295)
                                        .addComponent(chkDiagnosticoPagado))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(143, 143, 143)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(prioridad, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(txttoptech, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtcreaciontitu)
                                    .addComponent(txtfechafintitu))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                    .addComponent(jScrollPane5)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(399, 399, 399)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttoptech)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(logo)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcreaciontitu))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(txtfechafintitu))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtcrearticket1)
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtcrearticket)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtclientetitu)
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtDNI)
                                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(21, 21, 21)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtequipotitu)
                                            .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtestadotitu))
                                        .addGap(16, 16, 16)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txtprioridad)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(160, 160, 160)
                                                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtcorreo)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtcelular)
                                                        .addGap(23, 23, 23)
                                                        .addComponent(txtDescripcion)))
                                                .addGap(78, 78, 78))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(prioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(chkDiagnosticoPagado)
                                                .addGap(19, 19, 19)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txttecnico)
                                            .addComponent(tecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(25, 25, 25)
                                        .addComponent(jSeparator2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void equipoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_equipoActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_equipoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuardarActionPerformed
        String cliente = txtcliente1.getText().trim();
        String dni = txtdni1.getText().trim();
        String equipoSel = (String) equipo.getSelectedItem();
        String descripcion = txtdescripcion.getText().trim();
        String estadoSel = (String) estado.getSelectedItem();
        String tecnicoSel = (String) tecnicos.getSelectedItem();
        String prioridadSel = (String) prioridad.getSelectedItem();
        String fechaCreacion = txtfechadecreacion.getText();
        String fechaFin = txtfechafin.getText();
        String correo = txtcorreo1.getText().trim();
        String celular = txtcelular1.getText().trim();
        boolean diagnosticoPagado = chkDiagnosticoPagado.isSelected();

        if (cliente.isEmpty() || !cliente.matches("[A-Za-z]+ [A-Za-z]+")) {
            JOptionPane.showMessageDialog(this, "Cliente: solo 1 nombre y 1 apellido, letras.");
            return;
        }
        if (!(dni.length() == 8 || dni.length() == 11) || !dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "DNI/RUC: solo 8 o 11 dígitos numéricos.");
            return;
        }
        if (correo.isEmpty() || !correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Correo: debe ingresar un correo válido (obligatorio).");
            return;
        }
        if (!celular.matches("\\d{9,}")) {
            JOptionPane.showMessageDialog(this, "Celular: solo números, como mínimo 9 dígitos.");
            return;
        }
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descripción requerida.");
            return;
        }

        // Validar que se haya seleccionado un técnico
        if (tecnicoSel == null || tecnicoSel.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un técnico asignado.");
            return;
        }

        String id = txtidedelticket.getText();

        // Validar que no exista un ticket con este ID
        if (ticketManager.findTicketById(id) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe un ticket con el ID: " + id + ". Regenerando ID...");
            txtidedelticket.setText(ticketManager.generarNuevoIdTicket());
            return;
        }

        Ticket ticket = new Ticket(
                id, cliente, dni, equipoSel, descripcion, estadoSel, tecnicoSel, prioridadSel, fechaCreacion, fechaFin,
                correo, celular,
                diagnosticoPagado, 0.0);
        ticketManager.addTicket(ticket);

        if (userManager.findUserByDNI(dni) == null) {
            String[] parts = cliente.split(" ");
            userManager.addUser(new User(dni, parts[0], parts[1], dni, "", "CLIENTE"));
        }
        JOptionPane.showMessageDialog(this, "Ticket creado correctamente.");
        if (parentWindow != null)
            parentWindow.setVisible(true);
        dispose();

    }// GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelarActionPerformed
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        }
        this.dispose();
    }// GEN-LAST:event_btnCancelarActionPerformed

    private void estadoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_estadoActionPerformed
        String estadoSel = (String) estado.getSelectedItem();
        if ("SOLUCIONADO".equalsIgnoreCase(estadoSel)) {
            txtfechafin.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        } else {
            txtfechafin.setText("");
        }
    }// GEN-LAST:event_estadoActionPerformed

    private void prioridadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_prioridadActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_prioridadActionPerformed

    private void tecnicosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tecnicosActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_tecnicosActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateTicketWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JCheckBox chkDiagnosticoPagado;
    private javax.swing.JComboBox<String> equipo;
    private javax.swing.JComboBox<String> estado;
    private javax.swing.Box.Filler filler1;
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
    private javax.swing.JLabel txtDNI;
    private javax.swing.JLabel txtDescripcion;
    private javax.swing.JLabel txtcelular;
    private javax.swing.JTextPane txtcelular1;
    private javax.swing.JTextPane txtcliente1;
    private javax.swing.JLabel txtclientetitu;
    private javax.swing.JLabel txtcorreo;
    private javax.swing.JTextPane txtcorreo1;
    private javax.swing.JLabel txtcreaciontitu;
    private javax.swing.JLabel txtcrearticket;
    private javax.swing.JLabel txtcrearticket1;
    private javax.swing.JTextPane txtdescripcion;
    private javax.swing.JTextPane txtdni1;
    private javax.swing.JLabel txtequipotitu;
    private javax.swing.JLabel txtestadotitu;
    private javax.swing.JTextPane txtfechadecreacion;
    private javax.swing.JTextPane txtfechafin;
    private javax.swing.JLabel txtfechafintitu;
    private javax.swing.JTextPane txtidedelticket;
    private javax.swing.JLabel txtprioridad;
    private javax.swing.JLabel txttecnico;
    private javax.swing.JLabel txttoptech;
    // End of variables declaration//GEN-END:variables
}
