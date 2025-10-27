<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    // ✅ CORREGIDO: Manejar el HashMap correctamente
    java.util.HashMap<String, String> ticket = (java.util.HashMap<String, String>) request.getAttribute("ticket");
    String method = (String) request.getAttribute("method");
    
    if (ticket == null) {
        response.sendRedirect("tickets");
        return;
    }
    
    String ticketId = ticket.get("id") != null ? ticket.get("id") : "N/A";
    String monto = ticket.get("monto") != null ? ticket.get("monto") : "0.00";
    String descripcion = ticket.get("descripcion") != null ? ticket.get("descripcion") : "Servicio técnico";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago con Tarjeta - TOP TECH</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
            overflow: hidden;
        }
        
        .header {
            background: #2c3e50;
            color: white;
            padding: 20px;
            text-align: center;
        }
        
        .header h1 {
            font-size: 24px;
            margin-bottom: 5px;
        }
        
        .ticket-info {
            background: #f8f9fa;
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
        }
        
        .ticket-info p {
            margin: 5px 0;
            color: #495057;
        }
        
        .payment-form {
            padding: 30px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #495057;
            font-weight: 500;
        }
        
        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s;
        }
        
        input[type="text"]:focus, input[type="number"]:focus {
            border-color: #3498db;
            outline: none;
        }
        
        .card-details {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 15px;
        }
        
        .buttons {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn {
            padding: 15px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-confirm {
            background: #27ae60;
            color: white;
        }
        
        .btn-confirm:hover {
            background: #219653;
        }
        
        .btn-cancel {
            background: #e74c3c;
            color: white;
        }
        
        .btn-cancel:hover {
            background: #c0392b;
        }
        
        .card-icons {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        
        .card-icon {
            width: 40px;
            height: 25px;
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 3px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: bold;
            color: #6c757d;
        }
        
        .demo-notice {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 5px;
            padding: 10px;
            margin-top: 20px;
            text-align: center;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>💳 Pago con Tarjeta</h1>
            <p>TOP TECH - Servicio Técnico</p>
        </div>
        
        <div class="ticket-info">
            <p><strong>Ticket ID:</strong> <%= ticket.get("id") %></p>
            <p><strong>Descripción:</strong> <%= ticket.get("descripcion") %></p>
            <p><strong>Monto a pagar:</strong> S/ <%= ticket.get("monto") %></p>
        </div>
        
        <form action="payment" method="post" class="payment-form">
            <input type="hidden" name="ticketId" value="<%= ticket.get("id") %>">
            <input type="hidden" name="method" value="<%= method %>">
            
            <div class="form-group">
                <label for="cardNumber">Número de Tarjeta</label>
                <input type="text" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456" maxlength="19" required>
                <div class="card-icons">
                    <div class="card-icon">VISA</div>
                    <div class="card-icon">MC</div>
                    <div class="card-icon">AMEX</div>
                </div>
            </div>
            
            <div class="form-group">
                <label for="cardHolder">Nombre del Titular</label>
                <input type="text" id="cardHolder" name="cardHolder" placeholder="JUAN PEREZ" required>
            </div>
            
            <div class="card-details">
                <div class="form-group">
                    <label for="expiryDate">Fecha de Expiración</label>
                    <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/AA" maxlength="5" required>
                </div>
                
                <div class="form-group">
                    <label for="cvv">CVV</label>
                    <input type="number" id="cvv" name="cvv" placeholder="123" maxlength="3" required>
                </div>
            </div>
            
            <div class="demo-notice">
                💡 <strong>Modo Demo:</strong> Usa cualquier número de tarjeta para probar
            </div>
            
            <div class="buttons">
                <button type="submit" name="action" value="confirm" class="btn btn-confirm">
                    ✅ Confirmar Pago
                </button>
                <button type="submit" name="action" value="cancel" class="btn btn-cancel">
                    ❌ Cancelar
                </button>
            </div>
        </form>
    </div>

    <script>
        // Formatear número de tarjeta
        document.getElementById('cardNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ');
            e.target.value = formattedValue || value;
        });

        // Formatear fecha de expiración
        document.getElementById('expiryDate').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\//g, '').replace(/[^0-9]/gi, '');
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });

        // Limitar CVV a 3 dígitos
        document.getElementById('cvv').addEventListener('input', function(e) {
            if (this.value.length > 3) {
                this.value = this.value.slice(0, 3);
            }
        });
    </script>
</body>
</html>