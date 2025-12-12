/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ultratech;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import managers.TicketManager;
import managers.PaymentManager;
import models.Ticket;
import models.Payment;

/**
 * ListTicketsWindow - Con funcionalidad completa y GUI Designer editable
 * 
 * @author HP
 */
public class ListTicketsWindow extends javax.swing.JFrame {

        private TicketManager ticketManager;
        private PaymentManager paymentManager;
        private JFrame parentWindow;

        public ListTicketsWindow(TicketManager ticketManager, JFrame parentWindow) {
                this.ticketManager = ticketManager != null ? ticketManager : new TicketManager();
                this.paymentManager = new PaymentManager();
                this.parentWindow = parentWindow;

                initComponents();
                configurarTabla();
                configurarEventos();
                cargarTickets();
                setLocationRelativeTo(null);
        }

        public ListTicketsWindow() {
                this.ticketManager = new TicketManager();
                this.paymentManager = new PaymentManager();

                initComponents();
                configurarTabla();
                configurarEventos();
                cargarTickets();
                setLocationRelativeTo(null);
        }

        private void configurarTabla() {
                DefaultTableModel model = new DefaultTableModel(
                                new Object[][] {},
                                new String[] {
                                                "ID", "CLIENTE", "DNI", "EQUIPO", "DESCRIPCION", "ESTADO", "TECNICO",
                                                "PRIORIDAD",
                                                "FECHA DE CREACION", "FECHA FIN", "CORREO", "CELULAR", "DIAGNÓSTICO",
                                                "MONTO", "ESTADO PAGO"
                                }) {
                        @Override
                        public boolean isCellEditable(int row, int col) {
                                return false;
                        }
                };
                tblistadetickets.setModel(model);
                tblistadetickets.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                int[] widths = { 60, 140, 90, 80, 200, 100, 130, 80, 130, 100, 160, 100, 120, 80, 100 };
                for (int i = 0; i < widths.length && i < tblistadetickets.getColumnModel().getColumnCount(); i++) {
                        tblistadetickets.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
                }

                JTableHeader header = tblistadetickets.getTableHeader();
                header.setBackground(new Color(144, 199, 255));
                header.setForeground(Color.BLACK);
                header.setFont(header.getFont().deriveFont(Font.BOLD));

                tblistadetickets.setBackground(new Color(245, 245, 246));
                tblistadetickets.setGridColor(Color.LIGHT_GRAY);
                tblistadetickets.setSelectionBackground(new Color(200, 220, 240));
                tblistadetickets.setSelectionForeground(Color.BLACK);
                tblistadetickets.setRowHeight(25);
        }

        private void configurarEventos() {
                tblistadetickets.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                if (e.getClickCount() == 2) {
                                        int row = tblistadetickets.getSelectedRow();
                                        if (row >= 0) {
                                                String ticketId = String.valueOf(tblistadetickets.getValueAt(row, 0));
                                                abrirModifyTicketWindow(ticketId);
                                        }
                                }
                        }
                });

                btnVolver.addActionListener(e -> {
                        if (parentWindow != null)
                                parentWindow.setVisible(true);
                        dispose();
                });

                btnimprimirpdf.addActionListener(e -> imprimirTabla());
        }

        public void cargarTickets() {
                DefaultTableModel model = (DefaultTableModel) tblistadetickets.getModel();
                model.setRowCount(0);

                Ticket[] tickets = ticketManager.getAllTickets();
                if (tickets == null)
                        return;

                for (Ticket t : tickets) {
                        model.addRow(buildRowForTicket(t));
                }
        }

        private Object[] buildRowForTicket(Ticket t) {
                String diag = t.isDiagnosticoPagado() ? "PAGADO" : "NO PAGADO";
                String monto = String.format("%.2f", t.getMontoReparacion());
                String estadoPago = calcularEstadoPago(t);

                return new Object[] {
                                safe(t.getId()),
                                safe(t.getCliente()),
                                safe(t.getDni()),
                                safe(t.getEquipo()),
                                safe(t.getDescripcion()),
                                safe(t.getEstado()),
                                safe(t.getTecnico()),
                                safe(t.getPrioridad()),
                                safe(t.getFechaCreacion()),
                                safe(t.getFechaFin()),
                                safe(t.getCorreo()),
                                safe(t.getCelular()),
                                diag,
                                monto,
                                estadoPago
                };
        }

        private String calcularEstadoPago(Ticket t) {
                try {
                        if (!t.isDiagnosticoPagado())
                                return "PENDIENTE";
                        double monto = t.getMontoReparacion();
                        if (monto <= 0.0)
                                return "SIN MONTO";
                        Payment pago = paymentManager.obtenerPagoPorTicket(t.getId());
                        if (pago == null)
                                return "PENDIENTE";
                        return "PAGADO";
                } catch (Exception e) {
                        return "DESCONOCIDO";
                }
        }

        private void abrirModifyTicketWindow(String ticketId) {
                try {
                        ModifyTicketWindow mtw = new ModifyTicketWindow(ticketId);
                        mtw.setVisible(true);
                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al abrir ventana: " + ex.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void imprimirTabla() {
                try {
                        boolean complete = tblistadetickets.print();
                        JOptionPane.showMessageDialog(this, complete ? "Impresión completa." : "Impresión cancelada.");
                } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        public void refresh() {
                cargarTickets();
        }

        private String safe(String s) {
                return s == null ? "" : s;
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablalistausuarios = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblistadetickets = new javax.swing.JTable();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel5 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnimprimirpdf = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txttoptech = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablalistausuarios.setBackground(new java.awt.Color(51, 102, 255));
        tablalistausuarios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de tickets", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Decker", 0, 24), new java.awt.Color(255, 255, 255))); // NOI18N
        tablalistausuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblistadetickets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "DNI", "EQUIPO", "DESCRIPCION", "ESTADO", "TECNICO", "PRIORIDAD", "FECHA DE CREACION", "FECHA FIN", "CORREO", "CELULAR"
            }
        ));
        jScrollPane7.setViewportView(tblistadetickets);

        tablalistausuarios.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 1210, 230));
        tablalistausuarios.add(jScrollBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 80, -1, 210));

        jPanel5.setBackground(new java.awt.Color(153, 204, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVolver.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnVolver.setText("VOLVER");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel5.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 100, 40));

        btnimprimirpdf.setFont(new java.awt.Font("Decker", 0, 14)); // NOI18N
        btnimprimirpdf.setText("IMPRIMIR PDF");
        btnimprimirpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnimprimirpdfActionPerformed(evt);
            }
        });
        jPanel5.add(btnimprimirpdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 142, 40));

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));

        txttoptech.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 48)); // NOI18N
        txttoptech.setText("ultra tech");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(450, 450, 450)
                .addComponent(txttoptech, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(txttoptech)
                .addGap(388, 388, 388))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tablalistausuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tablalistausuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
                if (parentWindow != null) {
                        parentWindow.setVisible(true);
                }
                dispose();
        }

        private void btnimprimirpdfActionPerformed(java.awt.event.ActionEvent evt) {
                imprimirTabla();
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                new ListTicketsWindow().setVisible(true);
                        }
                });
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton btnimprimirpdf;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel tablalistausuarios;
    private javax.swing.JTable tblistadetickets;
    private javax.swing.JLabel txttoptech;
    // End of variables declaration//GEN-END:variables
}
