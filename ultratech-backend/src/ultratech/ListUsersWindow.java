package ultratech;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import managers.UserManager;
import managers.TicketManager;
import models.User;
import models.Ticket;

/**
 * ListUsersWindow - Ventana de lista de usuarios con sistema de filtros Similar
 * al estilo de ListTicketsWindow pero adaptado para usuarios
 *
 * Filtros disponibles: Usuario, Nombre, Apellido, Rol, DNI Funcionalidades:
 * Filtrar, Exportar CSV, Imprimir PDF, Eliminar Usuario
 */
public class ListUsersWindow extends javax.swing.JFrame {

    private UserManager userManager;
    private TicketManager ticketManager;
    private JFrame parentWindow;

    public ListUsersWindow(UserManager userManager, JFrame parentWindow) {
        this.userManager = userManager;
        this.ticketManager = new TicketManager(); // CORREGIDO
        this.parentWindow = parentWindow;
        initComponents();
        setTitle("Lista de Usuarios");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        configureBehavior();
        cargarUsuarios();
    }

    public ListUsersWindow() {
        this.userManager = new UserManager();
        this.ticketManager = new TicketManager(); // CORREGIDO
        this.parentWindow = null;
        initComponents();
        setTitle("Lista de Usuarios");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        configureBehavior();
        cargarUsuarios();
    }

    /**
     * Configura el comportamiento de los componentes
     */
    private void configureBehavior() {
        // Acción para eliminar usuario
        btnEliminarUser.addActionListener(evt -> btnEliminarUserActionPerformed(evt));

        // Acción al presionar Enter en el campo de búsqueda
        txtusuariogenerado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    filtrarUsuarios();
                }
            }
        });

        // Acción al presionar Enter en el campo de búsqueda CLIENTES
        txtclientegenerado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    filtrarClientes();
                }
            }
        });

    }

    /**
     * Carga solo usuarios ADMIN y TECNICO en la tabla (no clientes)
     */
    private void cargarUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tblistadeusuarios.getModel();
        model.setRowCount(0);

        User[] usuarios = userManager.getAllUsers();
        if (usuarios == null) {
            return;
        }

        for (User u : usuarios) {
            // Solo mostrar ADMIN y TECNICO, no clientes
            if ("ADMIN".equalsIgnoreCase(u.getRol()) || "TECNICO".equalsIgnoreCase(u.getRol())) {
                model.addRow(new Object[] {
                        safe(u.getUsuario()),
                        safe(u.getNombre()),
                        safe(u.getApellido()),
                        safe(u.getRol()),
                        safe(u.getDni())
                });
            }
        }
        cargarClientes();
    }

    /**
     * Filtra los usuarios ADMIN/TECNICO según el criterio seleccionado
     */
    private void filtrarUsuarios() {
        String filtroSeleccionado = String.valueOf(BUSCARLISTUSER.getSelectedItem()).trim().toUpperCase();
        String textoBusqueda = txtusuariogenerado.getText() == null ? ""
                : txtusuariogenerado.getText().trim().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblistadeusuarios.getModel();
        model.setRowCount(0);

        User[] usuarios = userManager.getAllUsers();
        if (usuarios == null) {
            return;
        }

        // Si no hay texto de búsqueda, mostrar todos los ADMIN/TECNICO
        if (textoBusqueda.isEmpty()) {
            for (User u : usuarios) {
                if ("ADMIN".equalsIgnoreCase(u.getRol()) || "TECNICO".equalsIgnoreCase(u.getRol())) {
                    model.addRow(buildRowForUser(u));
                }
            }
            return;
        }

        // Filtrar según el criterio seleccionado (solo ADMIN/TECNICO)
        for (User u : usuarios) {
            // Solo procesar ADMIN y TECNICO
            if (!"ADMIN".equalsIgnoreCase(u.getRol()) && !"TECNICO".equalsIgnoreCase(u.getRol())) {
                continue;
            }

            boolean coincide = false;

            switch (filtroSeleccionado) {
                case "USUARIO":
                    coincide = contains(u.getUsuario(), textoBusqueda);
                    break;

                case "NOMBRE":
                    coincide = contains(u.getNombre(), textoBusqueda);
                    break;

                case "APELLIDO":
                    coincide = contains(u.getApellido(), textoBusqueda);
                    break;

                case "ROL":
                    coincide = contains(u.getRol(), textoBusqueda);
                    break;

                case "DNI":
                    coincide = contains(u.getDni(), textoBusqueda);
                    break;

                default:
                    // Si no hay filtro específico, buscar en todos los campos
                    coincide = contains(u.getUsuario(), textoBusqueda)
                            || contains(u.getNombre(), textoBusqueda)
                            || contains(u.getApellido(), textoBusqueda)
                            || contains(u.getRol(), textoBusqueda)
                            || contains(u.getDni(), textoBusqueda);
            }

            if (coincide) {
                model.addRow(buildRowForUser(u));
            }
        }

        // Mostrar mensaje si no se encontraron resultados
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron usuarios que coincidan con el criterio de búsqueda.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Construye una fila para la tabla con los datos del usuario
     */
    private Object[] buildRowForUser(User u) {
        return new Object[] {
                safe(u.getUsuario()),
                safe(u.getNombre()),
                safe(u.getApellido()),
                safe(u.getRol()),
                safe(u.getDni())
        };
    }

    private void cargarClientes() {
        DefaultTableModel model = (DefaultTableModel) tblistadeusuarios1.getModel();
        model.setRowCount(0);

        Ticket[] tickets = ticketManager.getAllTickets();
        if (tickets == null) {
            return;
        }

        for (Ticket t : tickets) {
            model.addRow(new Object[] {
                    safe(t.getCliente()),
                    safe(t.getDni()),
                    safe(t.getCorreo()),
                    safe(t.getCelular()),
                    safe(t.getId())
            });
        }
    }

    /**
     * Filtra clientes según el criterio seleccionado
     */
    private void filtrarClientes() {
        String filtroSeleccionado = String.valueOf(BUSCARLISTCLIENT.getSelectedItem()).trim().toUpperCase();
        String textoBusqueda = txtclientegenerado.getText() == null ? ""
                : txtclientegenerado.getText().trim().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblistadeusuarios1.getModel();
        model.setRowCount(0);

        Ticket[] tickets = ticketManager.getAllTickets();
        if (tickets == null) {
            return;
        }

        if (textoBusqueda.isEmpty()) {
            for (Ticket t : tickets) {
                model.addRow(new Object[] {
                        safe(t.getCliente()),
                        safe(t.getDni()),
                        safe(t.getCorreo()),
                        safe(t.getCelular()),
                        safe(t.getId())
                });
            }
            return;
        }

        for (Ticket t : tickets) {
            boolean coincide = false;

            switch (filtroSeleccionado) {
                case "CLIENTE":
                    coincide = contains(t.getCliente(), textoBusqueda);
                    break;
                case "DNI":
                    coincide = contains(t.getDni(), textoBusqueda);
                    break;
                case "CORREO":
                    coincide = contains(t.getCorreo(), textoBusqueda);
                    break;
                case "CELULAR":
                    coincide = contains(t.getCelular(), textoBusqueda);
                    break;
                case "TICKET":
                    coincide = contains(t.getId(), textoBusqueda);
                    break;
                default:
                    coincide = contains(t.getCliente(), textoBusqueda)
                            || contains(t.getDni(), textoBusqueda)
                            || contains(t.getCorreo(), textoBusqueda)
                            || contains(t.getCelular(), textoBusqueda)
                            || contains(t.getId(), textoBusqueda);
            }

            if (coincide) {
                model.addRow(new Object[] {
                        safe(t.getCliente()),
                        safe(t.getDni()),
                        safe(t.getCorreo()),
                        safe(t.getCelular()),
                        safe(t.getId())
                });
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron clientes.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Exporta tabla de clientes a CSV
     */
    private void exportarClientesACSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo CSV");
        chooser.setSelectedFile(new File("clientes.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();
        if (selectedFile == null) {
            return;
        }

        String path = selectedFile.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) {
            path += ".csv";
        }

        File outputFile = new File(path);
        File parentDir = outputFile.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            DefaultTableModel model = (DefaultTableModel) tblistadeusuarios1.getModel();

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
                    PrintWriter pw = new PrintWriter(writer)) {

                for (int c = 0; c < model.getColumnCount(); c++) {
                    pw.print("\"" + model.getColumnName(c).replace("\"", "\"\"") + "\"");
                    if (c < model.getColumnCount() - 1) {
                        pw.print(",");
                    }
                }
                pw.println();

                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        String cell = val == null ? "" : String.valueOf(val).replace("\"", "\"\"");
                        pw.print("\"" + cell + "\"");
                        if (c < model.getColumnCount() - 1) {
                            pw.print(",");
                        }
                    }
                    pw.println();
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Archivo CSV exportado correctamente a:\n" + outputFile.getAbsolutePath(),
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifica si el texto fuente contiene el texto buscado (case insensitive)
     */
    private boolean contains(String source, String search) {
        if (source == null) {
            return false;
        }
        if (search == null || search.isEmpty()) {
            return true;
        }
        return source.toLowerCase().contains(search.toLowerCase());
    }

    /**
     * Retorna un string seguro (nunca null)
     */
    private String safe(String s) {
        return s == null ? "" : s;
    }

    /**
     * Exporta la tabla actual a un archivo CSV
     */
    private void exportarTablaACSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar archivo CSV");
        chooser.setSelectedFile(new File("usuarios.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();
        if (selectedFile == null) {
            return;
        }

        String path = selectedFile.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) {
            path += ".csv";
        }

        File outputFile = new File(path);
        File parentDir = outputFile.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            DefaultTableModel model = (DefaultTableModel) tblistadeusuarios.getModel();

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
                    PrintWriter pw = new PrintWriter(writer)) {

                // Escribir encabezados
                for (int c = 0; c < model.getColumnCount(); c++) {
                    pw.print("\"" + model.getColumnName(c).replace("\"", "\"\"") + "\"");
                    if (c < model.getColumnCount() - 1) {
                        pw.print(",");
                    }
                }
                pw.println();

                // Escribir datos
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        String cell = val == null ? "" : String.valueOf(val).replace("\"", "\"\"");
                        pw.print("\"" + cell + "\"");
                        if (c < model.getColumnCount() - 1) {
                            pw.print(",");
                        }
                    }
                    pw.println();
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Archivo CSV exportado correctamente a:\n" + outputFile.getAbsolutePath(),
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar el archivo CSV:\n" + e.getMessage(),
                    "Error de exportación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Acción del botón Eliminar Usuario
     */
    private void btnEliminarUserActionPerformed(java.awt.event.ActionEvent evt) {
        int filaSeleccionada = tblistadeusuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario a eliminar.");
            return;
        }

        String dni = (String) tblistadeusuarios.getValueAt(filaSeleccionada, 4); // columna 4 = DNI
        String nombre = (String) tblistadeusuarios.getValueAt(filaSeleccionada, 1);
        String apellido = (String) tblistadeusuarios.getValueAt(filaSeleccionada, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al usuario?\n\n"
                        + "Nombre: " + nombre + " " + apellido + "\n"
                        + "DNI: " + dni,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean resultado = userManager.eliminarUsuarioPorDNI(dni);
            if (resultado) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarUsuarios(); // Recargar todos los usuarios
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método público para recargar la tabla desde otras ventanas
     */
    public void refresh() {
        cargarUsuarios();
        cargarClientes();
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

        tablalistausuarios = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblistadeusuarios = new javax.swing.JTable();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel4 = new javax.swing.JPanel();
        txttoptech = new javax.swing.JLabel();
        BUSCARLISTUSER = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtusuariogenerado = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnimprimirpdf = new javax.swing.JButton();
        btnEliminarUser = new javax.swing.JButton();
        tablalistaclientes = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblistadeusuarios1 = new javax.swing.JTable();
        jScrollBar2 = new javax.swing.JScrollBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnbuscarclient = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtclientegenerado = new javax.swing.JTextPane();
        btnexportcsvclient = new javax.swing.JButton();
        BUSCARLISTCLIENT = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablalistausuarios.setBackground(new java.awt.Color(51, 102, 255));
        tablalistausuarios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de usuarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Decker", 0, 24), new java.awt.Color(255, 255, 255))); // NOI18N
        tablalistausuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblistadeusuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario", "Nombre", "Apellido", "Rol", "DNI"
            }
        ));
        jScrollPane7.setViewportView(tblistadeusuarios);

        tablalistausuarios.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 1070, 120));
        tablalistausuarios.add(jScrollBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 40, -1, 120));

        getContentPane().add(tablalistausuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 106, 1137, 167));

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txttoptech.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 48)); // NOI18N
        txttoptech.setText("ultra tech");
        jPanel4.add(txttoptech, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 320, 40));

        BUSCARLISTUSER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Nombre", "Apellido", "Rol", "DNI" }));
        BUSCARLISTUSER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUSCARLISTUSER(evt);
            }
        });
        jPanel4.add(BUSCARLISTUSER, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 100, 30));

        jLabel2.setText("FILTRAR POR:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        jLabel4.setText("SELECCIONE:");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, -1, -1));

        jButton1.setText("BUSCAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUSCARLIST(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, -1, -1));

        jButton2.setText("Exportar CSV");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EXPORTCSV(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 50, -1, -1));

        jScrollPane8.setViewportView(txtusuariogenerado);

        jPanel4.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 300, 30));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 88));

        jPanel5.setBackground(new java.awt.Color(153, 204, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVolver.setBackground(new java.awt.Color(51, 102, 255));
        btnVolver.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("VOLVER");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel5.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 100, 40));

        btnimprimirpdf.setBackground(new java.awt.Color(51, 102, 255));
        btnimprimirpdf.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnimprimirpdf.setForeground(new java.awt.Color(255, 255, 255));
        btnimprimirpdf.setText("IMPRIMIR PDF");
        btnimprimirpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirpdfActionPerformed(evt);
            }
        });
        jPanel5.add(btnimprimirpdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 142, 40));

        btnEliminarUser.setBackground(new java.awt.Color(255, 0, 51));
        btnEliminarUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminarUser.setForeground(new java.awt.Color(255, 255, 153));
        btnEliminarUser.setText("ELIMINAR USUARIO");
        jPanel5.add(btnEliminarUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 160, 40));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 1137, 60));

        tablalistaclientes.setBackground(new java.awt.Color(51, 102, 255));
        tablalistaclientes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de usuarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Decker", 0, 24), new java.awt.Color(255, 255, 255))); // NOI18N
        tablalistaclientes.setToolTipText("");
        tablalistaclientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblistadeusuarios1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cliente", "DNI", "Correo", "Celular", "Ticket"
            }
        ));
        jScrollPane9.setViewportView(tblistadeusuarios1);

        tablalistaclientes.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 1070, 130));
        tablalistaclientes.add(jScrollBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 40, -1, 130));

        getContentPane().add(tablalistaclientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 1137, 190));
        tablalistaclientes.getAccessibleContext().setAccessibleName("Lista de clientes");

        jLabel3.setText("FILTRAR POR:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, -1, -1));

        jLabel5.setText("SELECCIONE:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, -1, -1));

        btnbuscarclient.setText("BUSCAR");
        btnbuscarclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarclientBUSCARLIST(evt);
            }
        });
        getContentPane().add(btnbuscarclient, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, -1, -1));

        jScrollPane10.setViewportView(txtclientegenerado);

        getContentPane().add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 300, 30));

        btnexportcsvclient.setText("Exportar CSV");
        btnexportcsvclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexportcsvclientEXPORTCSV(evt);
            }
        });
        getContentPane().add(btnexportcsvclient, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 300, -1, -1));

        BUSCARLISTCLIENT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente", "DNI", "Correo", "Celular", "Ticket" }));
        BUSCARLISTCLIENT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BUSCARLISTCLIENT(evt);
            }
        });
        getContentPane().add(BUSCARLISTCLIENT, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, 100, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        if (parentWindow != null) {
            parentWindow.setVisible(true);
        }
        this.dispose();
    }// GEN-LAST:event_btnVolverActionPerformed

    private void btnimprimirpdfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnimprimirpdfActionPerformed
        // TODO add your handling code here:
        try {
            boolean complete = tblistadeusuarios.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Impresión completa.");
            } else {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage());
        }
    }// GEN-LAST:event_btnimprimirpdfActionPerformed

    private void BUSCARLIST(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_BUSCARLIST
        filtrarUsuarios();
    }// GEN-LAST:event_BUSCARLIST

    private void BUSCARLISTUSER(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_BUSCARLISTUSER

    }// GEN-LAST:event_BUSCARLISTUSER

    private void EXPORTCSV(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_EXPORTCSV
        // Ejecutar la exportación a CSV
        exportarTablaACSV();
    }// GEN-LAST:event_EXPORTCSV

    private void BUSCARLISTCLIENT(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_BUSCARLISTCLIENT
        // TODO add your handling code here:
    }// GEN-LAST:event_BUSCARLISTCLIENT

    private void btnbuscarclientBUSCARLIST(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnbuscarclientBUSCARLIST
        filtrarClientes();
    }// GEN-LAST:event_btnbuscarclientBUSCARLIST

    private void btnexportcsvclientEXPORTCSV(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnexportcsvclientEXPORTCSV
        exportarClientesACSV();
    }// GEN-LAST:event_btnexportcsvclientEXPORTCSV

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListUsersWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListUsersWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListUsersWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListUsersWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListUsersWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> BUSCARLISTCLIENT;
    private javax.swing.JComboBox<String> BUSCARLISTUSER;
    private javax.swing.JButton btnEliminarUser;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton btnbuscarclient;
    private javax.swing.JButton btnexportcsvclient;
    private javax.swing.JButton btnimprimirpdf;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollBar jScrollBar2;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel tablalistaclientes;
    private javax.swing.JPanel tablalistausuarios;
    private javax.swing.JTable tblistadeusuarios;
    private javax.swing.JTable tblistadeusuarios1;
    private javax.swing.JTextPane txtclientegenerado;
    private javax.swing.JLabel txttoptech;
    private javax.swing.JTextPane txtusuariogenerado;
    // End of variables declaration//GEN-END:variables
}
