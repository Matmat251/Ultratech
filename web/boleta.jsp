<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> boleta = (Map<String, Object>) request.getAttribute("boleta");
    if (boleta == null) {
        response.sendRedirect("tickets");
        return;
    }
    List<Map<String, Object>> detalles = (List<Map<String, Object>>) boleta.get("detalles");
    String logoPath = application.getContextPath() + "/assets/images/LOGO1.png";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Boleta <%= boleta.get("numeroBoleta") %></title>
    <style>
        @media print {
            .no-print { display: none !important; }
            body { margin: 0; padding: 0; }
            .boleta-container { border: none; box-shadow: none; margin: 0; }
        }
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; padding: 20px; color: #000; line-height: 1.2; }
        .boleta-container { max-width: 800px; margin: 0 auto; background: white; border: 1px solid #ccc; padding: 0; position: relative; }
        .logo-container { position: absolute; top: 15px; right: 20px; width: 80px; height: 80px; z-index: 10; }
        .logo-empresa { width: 100%; height: 100%; object-fit: contain; border: 1px solid #ddd; background: white; }
        .encabezado-principal { text-align: center; padding: 15px 20px; border-bottom: 2px solid #000; position: relative; }
        .titulo-boleta { font-size: 16px; font-weight: bold; margin-bottom: 5px; text-transform: uppercase; }
        .ruc { font-size: 14px; margin-bottom: 5px; }
        .numero-factura { font-size: 15px; font-weight: bold; margin: 8px 0; }
        .empresa-section { text-align: center; padding: 10px 20px; border-bottom: 1px solid #000; margin-top: 10px; }
        .nombre-empresa { font-size: 18px; font-weight: bold; margin-bottom: 3px; }
        .servicio-tecnico { font-size: 14px; font-weight: bold; }
        .cliente-section { padding: 15px 20px; border-bottom: 1px solid #000; }
        .cliente-line { margin: 4px 0; font-size: 14px; }
        .tabla-section { padding: 10px 20px; }
        .tabla-productos { width: 100%; border-collapse: collapse; border: 1px solid #000; }
        .tabla-productos th { border: 1px solid #000; padding: 6px 8px; text-align: left; font-weight: bold; font-size: 13px; background-color: #f0f0f0; }
        .tabla-productos td { border: 1px solid #000; padding: 6px 8px; font-size: 13px; height: auto; vertical-align: top; }
        .col-cantidad { width: 80px; text-align: center; }
        .col-producto { text-align: left; }
        .col-puedo { width: 100px; text-align: right; font-weight: bold; }
        .descripcion-falla { font-size: 11px; color: #666; margin-top: 3px; line-height: 1.3; font-style: italic; }
        .monto-letras { padding: 10px 20px; border-bottom: 1px solid #000; font-size: 13px; font-weight: bold; }
        .total-section { padding: 10px 20px; border-bottom: 1px solid #000; }
        .total-container { display: flex; justify-content: space-between; align-items: center; }
        .agradecimiento-mini { font-weight: bold; font-size: 13px; }
        .importe-total { text-align: right; }
        .importe-total-label { font-size: 13px; font-weight: bold; }
        .importe-total-monto { font-size: 16px; font-weight: bold; margin-top: 3px; }
        .agradecimiento { text-align: center; padding: 10px 20px; border-bottom: 1px solid #000; font-weight: bold; font-size: 14px; }
        .contacto-section { padding: 10px 20px; text-align: center; font-size: 12px; }
        .email { font-weight: bold; margin-bottom: 3px; }
        .telefonos { margin-bottom: 3px; }
        .nota-legal { padding: 8px 20px; font-size: 9px; text-align: center; color: #666; line-height: 1.3; border-top: 1px solid #ccc; background-color: #f9f9f9; }
        .botones { text-align: center; padding: 20px; background: white; margin-top: 20px; border-radius: 5px; border: 1px solid #ccc; }
        .btn { padding: 10px 20px; margin: 0 5px; border: 1px solid #ccc; border-radius: 3px; font-size: 14px; cursor: pointer; text-decoration: none; display: inline-block; background: white; color: #000; }
        .btn-print { background: #000; color: white; border: 1px solid #000; }
        .btn-download { background: #333; color: white; border: 1px solid #333; }
        .btn-back { background: #666; color: white; border: 1px solid #666; }
        @media print { .logo-container { position: absolute; top: 10px; right: 15px; } }
    </style>
</head>
<body>
    <div class="boleta-container">
        <div class="logo-container">
            <img src="<%= logoPath %>" alt="Logo Ultratech" class="logo-empresa" onerror="this.style.display='none'">
        </div>
        <div class="encabezado-principal">
            <div class="titulo-boleta">BOLETA ELECTRONICA</div>
            <div class="ruc">RUC: 10737954351</div>
            <div class="numero-factura">N° <%= boleta.get("numeroBoleta") %></div>
        </div>
        <div class="empresa-section">
            <div class="nombre-empresa">ULTRATECH</div>
            <div class="servicio-tecnico">SERVICIO TECNICO</div>
        </div>
        <div class="cliente-section">
            <div class="cliente-line">Cliente: <strong><%= boleta.get("cliente") %></strong></div>
            <div class="cliente-line">DNI: <strong><%= boleta.get("dniCliente") %></strong></div>
            <div class="cliente-line">Tipo de moneda: <strong>SOLES</strong></div>
        </div>
        <div class="tabla-section">
            <table class="tabla-productos">
                <thead>
                    <tr>
                        <th class="col-cantidad">Cantidad</th>
                        <th class="col-producto">Producto</th>
                        <th class="col-puedo">Precio</th>
                    </tr>
                </thead>
                <tbody>
<% for (Map<String, Object> detalle : detalles) { String tipo = detalle.get("tipo") != null ? detalle.get("tipo").toString() : "REPARACION"; boolean esDiagnostico = "DIAGNOSTICO".equals(tipo); %>
<tr style="<%= esDiagnostico ? "background-color: #fff3cd; color: #856404;" : "" %>">
<td class="col-cantidad">1</td>
<td class="col-producto">
<% if (esDiagnostico) { %>
<strong style="color: #856404;">CARGO ADICIONAL</strong><br><span style="font-size: 11px;"><%= detalle.get("descripcion") %></span>
<% } else { %>
<strong>Ticket <%= detalle.get("codigo") %></strong><br><span style="font-size: 11px; font-weight: bold;">Equipo: <%= detalle.get("equipo") %></span>
<% if (detalle.get("descripcion") != null && !detalle.get("descripcion").toString().trim().isEmpty()) { %>
<div class="descripcion-falla"><strong>Falla reportada:</strong> <%= detalle.get("descripcion") %></div>
<% } %>
<% } %>
</td>
<td class="col-puedo" style="<%= esDiagnostico ? "color: #856404;" : "" %>">S/ <%= String.format("%.2f", detalle.get("monto")) %></td>
</tr>
<% } %>
<% for (int i = detalles.size(); i < 8; i++) { %>
<tr><td class="col-cantidad">&nbsp;</td><td class="col-producto">&nbsp;</td><td class="col-puedo">&nbsp;</td></tr>
<% } %>
                </tbody>
            </table>
        </div>
        <div class="monto-letras">SON: <%= boleta.get("montoLetras") %></div>
        <div class="total-section">
            <div class="total-container">
                <div class="agradecimiento-mini">Gracias por su compra!</div>
                <div class="importe-total">
                    <div class="importe-total-label">IMPORTE</div>
                    <div class="importe-total-label">TOTAL</div>
                    <div class="importe-total-monto">S/ <%= String.format("%.2f", boleta.get("montoTotal")) %></div>
                </div>
            </div>
        </div>
        <div class="agradecimiento">Gracias por su compra!</div>
        <div class="contacto-section">
            <div class="email">ULTRATECHPERU@GMAIL.COM</div>
            <div class="telefonos">906.289.945 - 924.045.900</div>
        </div>
        <div class="nota-legal">Este documento es un comprobante de pago valido. Conservelo para futuras referencias. ULTRATECH - Servicio Tecnico de Computadoras. Todos los derechos reservados 2024.</div>
    </div>
    <div class="botones no-print">
        <button class="btn btn-print" onclick="window.print()">Imprimir Boleta</button>
        <a href="boleta-pdf?ids=<%= request.getParameter("ids") %>" class="btn btn-download">Descargar PDF</a>
        <a href="tickets" class="btn btn-back">Volver a Tickets</a>
    </div>
    <script>
<% if ("true".equals(request.getParameter("autoPrint"))) { %>
window.onload = function() { setTimeout(function() { window.print(); }, 500); };
<% } %>
document.addEventListener('DOMContentLoaded', function() { var logo = document.querySelector('.logo-empresa'); if (logo) { logo.onerror = function() { this.style.display = 'none'; }; } });
    </script>
</body>
</html>