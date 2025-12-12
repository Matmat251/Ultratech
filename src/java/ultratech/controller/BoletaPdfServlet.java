package ultratech.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import utils.DBConnection;

@WebServlet("/boleta-pdf")
public class BoletaPdfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ticketIds = request.getParameter("ids");
        if (ticketIds == null || ticketIds.isEmpty()) {
            response.sendRedirect("tickets");
            return;
        }

        try {
            Map<String, Object> boleta = obtenerDatosBoleta(ticketIds);

            if (boleta == null) {
                response.sendRedirect("tickets");
                return;
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=boleta-" + boleta.get("numeroBoleta") + ".pdf");

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            generarPdfSimple(document, boleta);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar PDF: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void generarPdfSimple(Document document, Map<String, Object> boleta) throws DocumentException {

        // Fuentes simples
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        Font fontNegrita = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontPequeno = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);
        Font fontTotal = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

        // ========== ENCABEZADO ==========
        Paragraph titulo = new Paragraph("BOLETA ELECTRONICA", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph ruc = new Paragraph("RUC: 10737954351", fontNormal);
        ruc.setAlignment(Element.ALIGN_CENTER);
        document.add(ruc);

        Paragraph numBoleta = new Paragraph("N° " + boleta.get("numeroBoleta"), fontNegrita);
        numBoleta.setAlignment(Element.ALIGN_CENTER);
        numBoleta.setSpacingAfter(10);
        document.add(numBoleta);

        // Linea separadora
        document.add(new Paragraph("_____________________________________________", fontNormal));
        document.add(Chunk.NEWLINE);

        // ========== EMPRESA ==========
        Paragraph empresa = new Paragraph("ULTRATECH", fontTitulo);
        empresa.setAlignment(Element.ALIGN_CENTER);
        document.add(empresa);

        Paragraph servicio = new Paragraph("SERVICIO TECNICO", fontNegrita);
        servicio.setAlignment(Element.ALIGN_CENTER);
        servicio.setSpacingAfter(10);
        document.add(servicio);

        document.add(new Paragraph("_____________________________________________", fontNormal));
        document.add(Chunk.NEWLINE);

        // ========== DATOS DEL CLIENTE ==========
        document.add(new Paragraph("Cliente: " + boleta.get("cliente"), fontNegrita));
        document.add(new Paragraph("DNI: " + boleta.get("dniCliente"), fontNegrita));
        document.add(new Paragraph("Tipo de moneda: SOLES", fontNormal));
        document.add(Chunk.NEWLINE);

        // ========== TABLA DE PRODUCTOS ==========
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] { 1, 5, 2 });

        // Encabezados
        PdfPCell cellCant = new PdfPCell(new Phrase("Cant.", fontNegrita));
        cellCant.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellCant.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCant.setPadding(5);
        tabla.addCell(cellCant);

        PdfPCell cellProd = new PdfPCell(new Phrase("Producto", fontNegrita));
        cellProd.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellProd.setPadding(5);
        tabla.addCell(cellProd);

        PdfPCell cellPrecio = new PdfPCell(new Phrase("Precio", fontNegrita));
        cellPrecio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellPrecio.setPadding(5);
        tabla.addCell(cellPrecio);

        // Filas de productos
        List<Map<String, Object>> detalles = (List<Map<String, Object>>) boleta.get("detalles");

        for (Map<String, Object> detalle : detalles) {
            String tipo = detalle.get("tipo") != null ? detalle.get("tipo").toString() : "REPARACION";
            boolean esDiagnostico = "DIAGNOSTICO".equals(tipo);

            // Cantidad
            PdfPCell c1 = new PdfPCell(new Phrase("1", fontNormal));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPadding(5);
            if (esDiagnostico) {
                c1.setBackgroundColor(new BaseColor(255, 243, 205)); // Amarillo claro
            }
            tabla.addCell(c1);

            // Producto
            String productoTexto;
            if (esDiagnostico) {
                productoTexto = "CARGO ADICIONAL\n" + detalle.get("descripcion");
            } else {
                productoTexto = "Ticket " + detalle.get("codigo") + "\nEquipo: " + detalle.get("equipo");
                if (detalle.get("descripcion") != null && !detalle.get("descripcion").toString().trim().isEmpty()) {
                    productoTexto += "\nFalla: " + detalle.get("descripcion");
                }
            }
            PdfPCell c2 = new PdfPCell(new Phrase(productoTexto, fontNormal));
            c2.setPadding(5);
            if (esDiagnostico) {
                c2.setBackgroundColor(new BaseColor(255, 243, 205));
            }
            tabla.addCell(c2);

            // Precio
            double monto = (Double) detalle.get("monto");
            PdfPCell c3 = new PdfPCell(new Phrase("S/ " + String.format("%.2f", monto), fontNormal));
            c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c3.setPadding(5);
            if (esDiagnostico) {
                c3.setBackgroundColor(new BaseColor(255, 243, 205));
            }
            tabla.addCell(c3);
        }

        document.add(tabla);
        document.add(Chunk.NEWLINE);

        // ========== MONTO EN LETRAS ==========
        document.add(new Paragraph("SON: " + boleta.get("montoLetras"), fontNegrita));
        document.add(Chunk.NEWLINE);

        // ========== TOTAL ==========
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setWidths(new float[] { 3, 1 });

        PdfPCell graciasCell = new PdfPCell(new Phrase("Gracias por su compra!", fontNegrita));
        graciasCell.setBorder(Rectangle.NO_BORDER);
        graciasCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalTable.addCell(graciasCell);

        double montoTotal = (Double) boleta.get("montoTotal");
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL: S/ " + String.format("%.2f", montoTotal), fontTotal));
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setBorder(Rectangle.BOX);
        totalCell.setPadding(10);
        totalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalTable.addCell(totalCell);

        document.add(totalTable);
        document.add(Chunk.NEWLINE);

        // ========== CONTACTO ==========
        document.add(new Paragraph("_____________________________________________", fontNormal));

        Paragraph contacto = new Paragraph();
        contacto.setAlignment(Element.ALIGN_CENTER);
        contacto.add(new Chunk("ULTRATECHPERU@GMAIL.COM\n", fontNegrita));
        contacto.add(new Chunk("906.289.945 - 924.045.900", fontNormal));
        document.add(contacto);
        document.add(Chunk.NEWLINE);

        // ========== NOTA LEGAL ==========
        Paragraph nota = new Paragraph(
                "Este documento es un comprobante de pago valido. Conservelo para futuras referencias. " +
                        "ULTRATECH - Servicio Tecnico de Computadoras. Todos los derechos reservados 2024.",
                fontPequeno);
        nota.setAlignment(Element.ALIGN_CENTER);
        document.add(nota);
    }

    private Map<String, Object> obtenerDatosBoleta(String ticketIds) {
        Map<String, Object> boleta = new HashMap<>();

        try {
            Connection conn = DBConnection.getConnection();

            String[] idsArray = ticketIds.split(",");
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < idsArray.length; i++) {
                if (i > 0)
                    placeholders.append(",");
                placeholders.append("?");
            }

            String sql = "SELECT * FROM Tickets WHERE codigo IN (" + placeholders + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < idsArray.length; i++) {
                stmt.setString(i + 1, idsArray[i].trim());
            }

            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> detalles = new ArrayList<>();
            double total = 0;
            String cliente = "";
            String dni = "";

            while (rs.next()) {
                // Verificar si el diagnostico esta pagado
                boolean diagPagado = rs.getBoolean("diagnosticoPagado");
                if (!diagPagado) {
                    // Agregar cargo extra por diagnostico
                    Map<String, Object> detalleDiag = new HashMap<>();
                    detalleDiag.put("codigo", rs.getString("codigo"));
                    detalleDiag.put("descripcion",
                            "Cargo por diagnostico no pagado - Ticket " + rs.getString("codigo"));
                    detalleDiag.put("monto", 50.0);
                    detalleDiag.put("tipo", "DIAGNOSTICO");
                    detalles.add(detalleDiag);
                    total += 50.0;
                }

                Map<String, Object> detalle = new HashMap<>();
                detalle.put("codigo", rs.getString("codigo"));
                detalle.put("equipo", rs.getString("equipo"));
                detalle.put("descripcion", rs.getString("descripcion"));
                detalle.put("monto", rs.getDouble("montoReparacion"));
                detalle.put("tipo", "REPARACION");
                detalles.add(detalle);
                total += rs.getDouble("montoReparacion");

                if (cliente.isEmpty()) {
                    cliente = rs.getString("cliente");
                    dni = rs.getString("dni");
                }
            }

            boleta.put("numeroBoleta", generarNumeroBoleta());
            boleta.put("fechaEmision", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            boleta.put("cliente", cliente);
            boleta.put("dniCliente", dni);
            boleta.put("montoTotal", total);
            boleta.put("montoLetras", convertirNumeroATexto(total));
            boleta.put("detalles", detalles);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return boleta;
    }

    private String generarNumeroBoleta() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "B001-" + sdf.format(new Date());
    }

    private String convertirNumeroATexto(double numero) {
        int parteEntera = (int) numero;
        int centavos = (int) Math.round((numero - parteEntera) * 100);

        String texto = convertirParteEntera(parteEntera);

        if (centavos > 0) {
            return texto.toUpperCase() + " CON " + String.format("%02d", centavos) + "/100 SOLES";
        } else {
            return texto.toUpperCase() + " SOLES";
        }
    }

    private String convertirParteEntera(int numero) {
        if (numero == 0)
            return "cero";
        if (numero < 0)
            return "menos " + convertirParteEntera(-numero);

        String[] unidades = { "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve" };
        String[] especiales = { "diez", "once", "doce", "trece", "catorce", "quince", "dieciseis", "diecisiete",
                "dieciocho", "diecinueve" };
        String[] decenas = { "", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta",
                "noventa" };
        String[] centenas = { "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos",
                "setecientos", "ochocientos", "novecientos" };

        if (numero < 10)
            return unidades[numero];
        if (numero < 20)
            return especiales[numero - 10];
        if (numero < 100) {
            if (numero % 10 == 0)
                return decenas[numero / 10];
            if (numero < 30)
                return "veinti" + unidades[numero % 10];
            return decenas[numero / 10] + " y " + unidades[numero % 10];
        }
        if (numero == 100)
            return "cien";
        if (numero < 1000) {
            int c = numero / 100;
            int resto = numero % 100;
            if (resto == 0)
                return centenas[c];
            return centenas[c] + " " + convertirParteEntera(resto);
        }
        if (numero < 2000) {
            return "mil " + convertirParteEntera(numero - 1000);
        }
        if (numero < 1000000) {
            int miles = numero / 1000;
            int resto = numero % 1000;
            String milesTexto = convertirParteEntera(miles) + " mil";
            if (resto == 0)
                return milesTexto;
            return milesTexto + " " + convertirParteEntera(resto);
        }

        return String.valueOf(numero);
    }
}