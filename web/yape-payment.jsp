<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    // Manejar el HashMap correctamente
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
    <title>Pago con Yape - TOP TECH</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        body {
            background: linear-gradient(135deg, #00c6ff 0%, #0072ff 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            overflow: hidden;
            text-align: center;
        }
        
        .header {
            background: #7b1fa2;
            color: white;
            padding: 25px;
        }
        
        .header h1 {
            font-size: 24px;
            margin-bottom: 10px;
        }
        
        .yape-logo {
            font-size: 48px;
            margin-bottom: 10px;
        }
        
        .ticket-info {
            padding: 20px;
            background: #f3e5f5;
            border-bottom: 1px solid #d1c4e9;
        }
        
        .ticket-info p {
            margin: 8px 0;
            color: #4a148c;
            font-weight: 500;
        }
        
        .qr-section {
            padding: 30px;
        }
        
        .qr-code {
            width: 200px;
            height: 200px;
            background: #f5f5f5;
            border: 2px dashed #7b1fa2;
            border-radius: 10px;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: #666;
            background-image: linear-gradient(45deg, #f3e5f5 25%, transparent 25%), 
                              linear-gradient(-45deg, #f3e5f5 25%, transparent 25%), 
                              linear-gradient(45deg, transparent 75%, #f3e5f5 75%), 
                              linear-gradient(-45deg, transparent 75%, #f3e5f5 75%);
            background-size: 20px 20px;
            background-position: 0 0, 0 10px, 10px -10px, -10px 0px;
        }
        
        .payment-details {
            background: #e8f5e8;
            padding: 20px;
            margin: 20px 0;
            border-radius: 10px;
            border: 2px solid #4caf50;
        }
        
        .phone-number {
            font-size: 24px;
            font-weight: bold;
            color: #2e7d32;
            margin: 15px 0;
            background: white;
            padding: 10px;
            border-radius: 8px;
            border: 2px dashed #4caf50;
        }
        
        .instructions {
            text-align: left;
            margin: 20px 0;
            padding: 0 10px;
        }
        
        .instructions ol {
            margin-left: 20px;
        }
        
        .instructions li {
            margin-bottom: 10px;
            color: #555;
            line-height: 1.4;
        }
        
        .buttons {
            display: grid;
            gap: 15px;
            padding: 20px;
        }
        
        .btn {
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-confirm {
            background: #4caf50;
            color: white;
        }
        
        .btn-confirm:hover {
            background: #388e3c;
        }
        
        .btn-cancel {
            background: #f44336;
            color: white;
        }
        
        .btn-cancel:hover {
            background: #d32f2f;
        }
        
        .demo-notice {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 5px;
            padding: 10px;
            margin: 15px;
            color: #856404;
            font-size: 14px;
        }
        
        .concepto {
            background: #fff3cd;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
            font-family: monospace;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="yape-logo">🧾</div>
            <h1>Pago con Yape</h1>
            <p>TOP TECH - Servicio Técnico</p>
        </div>
        
        <div class="ticket-info">
            <p><strong>Ticket ID:</strong> <%= ticketId %></p>
            <p><strong>Descripción:</strong> <%= descripcion %></p>
            <p><strong>Monto a pagar:</strong> S/ <%= monto %></p>
        </div>
        
        <form action="pay" method="post">
            <input type="hidden" name="ticketId" value="<%= ticketId %>">
            <input type="hidden" name="method" value="<%= method %>">
            <input type="hidden" name="action" value="confirm">
            
            <div class="qr-section">
                <div class="qr-code">
                    <!-- En producción aquí iría un QR real -->
                     CÓDIGO QR<br>YAPE
                    <div style="font-size: 10px; margin-top: 5px;">(Simulación)</div>
                </div>
                
                <div class="payment-details">
                    <p><strong>Número Yape para transferencia:</strong></p>
                    <div class="phone-number">📞 999 888 777</div>
                    <p><strong>Nombre:</strong> Tony Tech</p>
                    
                    <div class="concepto">
                        <strong>Concepto obligatorio:</strong><br>
                        Ticket <%= ticketId %>
                    </div>
                </div>
                
                <div class="instructions">
                    <h3> Instrucciones para pagar:</h3>
                    <ol>
                        <li><strong>Abre la app de Yape</strong> en tu celular</li>
                        <li><strong>Escanéa el código QR</strong> o busca el número: <strong>999 888 777</strong></li>
                        <li><strong>Transfiere el monto exacto:</strong> <strong style="color: #e74c3c;">S/ <%= monto %></strong></li>
                        <li><strong>Ingresa el concepto:</strong> <strong>Ticket <%= ticketId %></strong></li>
                        <li><strong>Confirma la transferencia</strong> y toma screenshot del comprobante</li>
                    </ol>
                </div>
            </div>
            
            <div class="demo-notice">
                 <strong>Modo Demo:</strong> Este es un sistema de simulación. 
                No se realizarán cargos reales. Haz clic en "✅ Ya pagué con Yape" para continuar.
            </div>
            
            <div class="buttons">
                <button type="submit" class="btn btn-confirm">
                    ✅ Ya pagué con Yape
                </button>
                <button type="button" onclick="window.history.back()" class="btn btn-cancel">
                     Cancelar
                </button>
            </div>
        </form>
    </div>

    <script>
      
        document.querySelector('form').addEventListener('submit', function(e) {
            const confirmar = confirm('¿Estás seguro de que ya realizaste el pago con Yape?\n\nTicket: <%= ticketId %>\nMonto: S/ <%= monto %>');
            
            if (!confirmar) {
                e.preventDefault();
            }
        });

       
        const qrCode = document.querySelector('.qr-code');
        let angle = 0;
        
        setInterval(() => {
            angle = (angle + 0.5) % 360;
            qrCode.style.transform = `rotate(${angle}deg)`;
        }, 50);
    </script>
</body>
</html>