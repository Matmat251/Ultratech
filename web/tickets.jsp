<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*,java.util.HashMap,models.User" %>
<%
    // ✅ VERIFICACIÓN ROBUSTA DE SESIÓN
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    User user = null;
    String clientId = null;
    
    if (session != null) {
        user = (User) session.getAttribute("user");
        clientId = (String) session.getAttribute("clientId");
        System.out.println("🎫 tickets.jsp - Sesión detectada:");
        System.out.println("   - user: " + (user != null ? "PRESENTE" : "AUSENTE"));
        System.out.println("   - clientId: " + clientId);
    } else {
        System.out.println("🎫 tickets.jsp - NO hay sesión activa");
    }
    
    // ✅ REDIRECCIÓN INMEDIATA si no hay usuario
    if (user == null) {
        System.out.println("❌ tickets.jsp - Redirigiendo a login (sin usuario)");
        response.sendRedirect("index.jsp");
        return;
    }
    
    // ✅ GARANTIZAR que clientId esté presente
    if (clientId == null) {
        clientId = user.getDni();
        session.setAttribute("clientId", clientId);
        System.out.println("🔄 clientId establecido desde user: " + clientId);
    }
    
    List<HashMap<String, String>> tickets = (List<HashMap<String, String>>) request.getAttribute("tickets");
    
    // ✅ Calcular contadores
    int asignadoCount = 0, atendidoCount = 0, solucionadoCount = 0, canceladoCount = 0;
    if (tickets != null) {
        for (HashMap<String, String> ticket : tickets) {
            String estado = ticket.get("estado");
            if ("ASIGNADO".equalsIgnoreCase(estado)) asignadoCount++;
            else if ("ATENDIDO".equalsIgnoreCase(estado)) atendidoCount++;
            else if ("SOLUCIONADO".equalsIgnoreCase(estado)) solucionadoCount++;
            else if ("CANCELADO".equalsIgnoreCase(estado)) canceladoCount++;
        }
    }
    
    System.out.println("✅ tickets.jsp - Renderizando página para: " + user.getNombre() + " (" + clientId + ")");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Mis Tickets - TOP TECH</title>
    <link rel="stylesheet" href="assets/css/style.css"/>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <style>
        /* ✅ ESTILOS ADICIONALES PARA EL FORMULARIO DE PAGO */
        .payment-form-container {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }
        
        .payment-select {
            padding: 8px 12px;
            border: 2px solid #3498db;
            border-radius: 5px;
            background: white;
            font-size: 14px;
            min-width: 180px;
        }
        
        .btn-pay {
            background-color: #2ecc71;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 8px 15px;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .btn-pay:hover {
            background-color: #27ae60;
        }
        
        .btn-pay:disabled {
            background-color: #95a5a6;
            cursor: not-allowed;
        }
        
        .payment-method-info {
            font-size: 12px;
            color: #7f8c8d;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="brand">
                <h1>TOP TECH</h1>
                <p>SERVICIO TÉCNICO</p>
            </div>
            <div class="user-info">
                <span>Usuario: <strong><%= user.getNombre() %> <%= user.getApellido() %></strong></span>
                <form action="logout" method="post">
                    <button type="submit" class="btn-logout">Cerrar sesión</button>
                </form>
            </div>
        </div>

        <div class="welcome-section">
            <h2>Bienvenido(a), <%= user.getNombre() %> <%= user.getApellido() %></h2>
            <p>DNI: <strong><%= clientId %></strong> - Consulta el estado de tus tickets y pagos pendientes.</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card asignado">
                <h3>Asignado</h3>
                <div class="stat-number"><%= asignadoCount %> Tickets</div>
            </div>
            <div class="stat-card atendido">
                <h3>Atendido</h3>
                <div class="stat-number"><%= atendidoCount %> Tickets</div>
            </div>
            <div class="stat-card solucionado">
                <h3>Solucionado</h3>
                <div class="stat-number"><%= solucionadoCount %> Tickets</div>
            </div>
            <div class="stat-card cancelado">
                <h3>Cancelado</h3>
                <div class="stat-number"><%= canceladoCount %> Tickets</div>
            </div>
        </div>

        <% if (asignadoCount > 0) { %>
        <div class="alert">
            <div class="alert-icon">⚠️</div>
            <div>¡Atención! Tienes <%= asignadoCount %> ticket(s) pendiente(s) de atención.</div>
        </div>
        <% } %>

        <div class="tickets-section">
            <h2>Mis Tickets</h2>
            
            <div id="list">
                <% if (tickets == null || tickets.isEmpty()) { %>
                    <div class="no-tickets">
                        <p>No tiene tickets registrados.</p>
                    </div>
                <% } else {
                    for (HashMap<String, String> t : tickets) { 
                        String estado = t.get("estado");
                        boolean puedePagar = "SOLUCIONADO".equalsIgnoreCase(estado) || "ATENDIDO".equalsIgnoreCase(estado);
                %>
                        <div class="ticket">
                            <div class="ticket-header">
                                <div class="ticket-id">ID: <%= t.get("id") %></div>
                                <div class="ticket-date">Fecha: <%= t.get("fecha") %></div>
                                <div class="ticket-status">Estado: <%= estado %></div>
                            </div>
                            <div class="ticket-desc"><strong>Descripción:</strong> <%= t.get("descripcion") %></div>
                            <div class="ticket-amount">Monto: S/ <%= t.get("monto") %></div>
                            
                            <div class="ticket-actions">
                                <!-- ✅ FORMULARIO DE PAGO CORREGIDO -->
                                <div class="payment-form-container">
                                    <form action="pay" method="get" style="display:inline-block">
                                        <input type="hidden" name="ticketId" value="<%= t.get("id") %>"/>
                                        <select name="method" class="payment-select" <%= !puedePagar ? "disabled" : "" %>>
                                            <option value="">Seleccionar método</option>
                                            <option value="paypal">PayPal (Sandbox)</option>
                                            <option value="yape">Yape</option>
                                            <option value="card">Tarjeta (Visa/Mastercard)</option>
                                        </select>
                                        <button type="submit" class="btn btn-pay" <%= !puedePagar ? "disabled" : "" %>>
                                            <%= puedePagar ? "Pagar" : "No disponible" %>
                                        </button>
                                    </form>
                                    
                                    <div class="payment-method-info">
                                        <% if (!puedePagar) { %>
                                            <span style="color: #e74c3c;">⏳ El pago estará disponible cuando el ticket esté en estado "Atendido" o "Solucionado"</span>
                                        <% } else { %>
                                            <span style="color: #27ae60;">✅ Pago disponible</span>
                                        <% } %>
                                    </div>
                                </div>
                                
                                <div style="margin-top: 10px;">
                                    <a class="btn btn-download" href="download?id=<%= t.get("id") %>">Descargar boleta (HTML)</a>
                                    <a class="btn btn-download" href="report?id=<%= t.get("id") %>">Descargar detalle (PDF)</a>
                                </div>
                            </div>
                        </div>
                <%  } } %>
            </div>
        </div>
    </div>
    
    <script>
        // ✅ EVITAR CACHE DEL NAVEGADOR
        window.onpageshow = function(event) {
            if (event.persisted) {
                console.log("🔄 Página cargada desde cache, recargando...");
                window.location.reload();
            }
        };
        
        // ✅ PREVENIR navegación con botones atrás/adelante
        history.pushState(null, null, location.href);
        window.onpopstate = function(event) {
            history.go(1);
        };
        
        // ✅ MEJORAR USABILIDAD DEL FORMULARIO DE PAGO
        document.addEventListener('DOMContentLoaded', function() {
            const paymentForms = document.querySelectorAll('form[action="pay"]');
            
            paymentForms.forEach(form => {
                const select = form.querySelector('select[name="method"]');
                const button = form.querySelector('button[type="submit"]');
                
                // Habilitar/deshabilitar botón según selección
                select.addEventListener('change', function() {
                    if (this.value && !button.disabled) {
                        button.style.opacity = '1';
                        button.textContent = 'Pagar con ' + this.options[this.selectedIndex].text;
                    } else {
                        button.style.opacity = '0.7';
                        button.textContent = 'Pagar';
                    }
                });
                
                // Validación antes de enviar
                form.addEventListener('submit', function(e) {
                    if (!select.value) {
                        e.preventDefault();
                        alert('Por favor selecciona un método de pago');
                        return false;
                    }
                    
                    // Mostrar confirmación
                    const ticketId = form.querySelector('input[name="ticketId"]').value;
                    const method = select.options[select.selectedIndex].text;
                    const confirmar = confirm(`¿Estás seguro de que quieres pagar el ticket ${ticketId} usando ${method}?`);
                    
                    if (!confirmar) {
                        e.preventDefault();
                        return false;
                    }
                });
            });
        });
    </script>
</body>
</html>