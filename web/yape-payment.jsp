<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*" %>
        <% java.util.HashMap<String, String> ticket = (java.util.HashMap<String, String>)
                request.getAttribute("ticket");
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
                    <title>Pago con Yape - UltraTech</title>
                    <link rel="stylesheet" href="assets/css/styles.css">
                    <style>
                        .yape-page {
                            min-height: 100vh;
                            background: linear-gradient(135deg, #0b2b60 0%, #094e87 50%, #0b6fc2 100%);
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 2rem;
                        }

                        .yape-container {
                            max-width: 700px;
                            width: 100%;
                            background: rgba(255, 255, 255, 0.08);
                            backdrop-filter: blur(20px);
                            -webkit-backdrop-filter: blur(20px);
                            border-radius: var(--radius-2xl);
                            padding: 2.5rem;
                            box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
                            border: 1px solid rgba(255, 255, 255, 0.1);
                            animation: fadeInUp 0.5s ease-out;
                        }

                        .yape-header {
                            text-align: center;
                            margin-bottom: 2rem;
                        }

                        .yape-logo {
                            font-size: 4rem;
                            margin-bottom: 0.75rem;
                            animation: float 3s ease-in-out infinite;
                        }

                        .yape-header h1 {
                            color: var(--primary-light);
                            font-size: var(--font-size-2xl);
                            font-weight: 700;
                            margin-bottom: 0.5rem;
                        }

                        .yape-header p {
                            color: rgba(255, 255, 255, 0.7);
                            font-size: var(--font-size-sm);
                            letter-spacing: 2px;
                            text-transform: uppercase;
                        }

                        .ticket-info-card {
                            background: rgba(11, 111, 194, 0.3);
                            padding: 1.25rem 1.5rem;
                            border-left: 4px solid var(--primary-light);
                            border-radius: var(--radius-lg);
                            margin-bottom: 2rem;
                        }

                        .ticket-info-card p {
                            margin: 0.5rem 0;
                            color: rgba(255, 255, 255, 0.9);
                            font-size: var(--font-size-sm);
                        }

                        .ticket-info-card strong {
                            color: var(--primary-light);
                        }

                        .qr-section {
                            display: flex;
                            flex-wrap: wrap;
                            gap: 2rem;
                            align-items: center;
                            justify-content: center;
                            margin-bottom: 2rem;
                        }

                        .qr-code {
                            position: relative;
                        }

                        .qr-code img {
                            width: 200px;
                            height: 200px;
                            border: 4px solid var(--primary-light);
                            border-radius: var(--radius-xl);
                            padding: 0.75rem;
                            background: white;
                            box-shadow: 0 10px 30px rgba(126, 208, 249, 0.3);
                        }

                        .qr-code::after {
                            content: '';
                            position: absolute;
                            top: -8px;
                            left: -8px;
                            right: -8px;
                            bottom: -8px;
                            border: 2px solid var(--primary-light);
                            border-radius: calc(var(--radius-xl) + 8px);
                            opacity: 0.5;
                            animation: pulse 2s ease-in-out infinite;
                        }

                        .payment-details {
                            background: rgba(0, 0, 0, 0.3);
                            padding: 1.5rem;
                            border-radius: var(--radius-xl);
                            border: 1px solid rgba(126, 208, 249, 0.3);
                            max-width: 280px;
                        }

                        .payment-details p {
                            color: rgba(255, 255, 255, 0.9);
                            margin-bottom: 0.75rem;
                            font-size: var(--font-size-sm);
                        }

                        .phone-number {
                            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
                            color: white;
                            padding: 1rem;
                            border-radius: var(--radius-lg);
                            margin: 1rem 0;
                            font-weight: 700;
                            font-size: var(--font-size-lg);
                            text-align: center;
                            box-shadow: 0 4px 15px rgba(11, 111, 194, 0.4);
                        }

                        .concepto {
                            margin-top: 1rem;
                            color: var(--primary-light);
                            background: rgba(126, 208, 249, 0.1);
                            padding: 1rem;
                            border-radius: var(--radius-md);
                            font-size: var(--font-size-sm);
                        }

                        .instructions {
                            background: rgba(0, 0, 0, 0.2);
                            border-radius: var(--radius-xl);
                            padding: 1.5rem;
                            margin-bottom: 2rem;
                        }

                        .instructions h3 {
                            color: var(--primary-light);
                            margin-bottom: 1rem;
                            font-size: var(--font-size-lg);
                        }

                        .instructions ol {
                            padding-left: 1.5rem;
                        }

                        .instructions li {
                            margin: 0.75rem 0;
                            color: rgba(255, 255, 255, 0.85);
                            line-height: 1.6;
                        }

                        .instructions li strong {
                            color: var(--primary-light);
                        }

                        .form-group {
                            margin-bottom: 1.5rem;
                        }

                        .form-group label {
                            color: var(--primary-light);
                            display: block;
                            margin-bottom: 0.625rem;
                            font-weight: 600;
                            font-size: var(--font-size-sm);
                        }

                        .form-group input[type="text"],
                        .form-group input[type="file"] {
                            width: 100%;
                            padding: 1rem;
                            background: rgba(255, 255, 255, 0.1);
                            border: 2px solid rgba(126, 208, 249, 0.4);
                            color: white;
                            border-radius: var(--radius-lg);
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            transition: all var(--transition-base);
                        }

                        .form-group input:focus {
                            border-color: var(--primary-light);
                            outline: none;
                            background: rgba(255, 255, 255, 0.15);
                            box-shadow: 0 0 0 4px rgba(126, 208, 249, 0.2);
                        }

                        .form-group input::placeholder {
                            color: rgba(255, 255, 255, 0.5);
                        }

                        .form-group small {
                            color: rgba(255, 255, 255, 0.6);
                            font-size: var(--font-size-xs);
                            margin-top: 0.5rem;
                            display: block;
                        }

                        .file-preview {
                            margin-top: 1rem;
                            text-align: center;
                        }

                        .file-preview img {
                            max-width: 200px;
                            max-height: 150px;
                            border: 2px dashed var(--primary-light);
                            border-radius: var(--radius-lg);
                            padding: 0.5rem;
                            background: rgba(255, 255, 255, 0.1);
                        }

                        .buttons {
                            display: flex;
                            gap: 1rem;
                            justify-content: center;
                            flex-wrap: wrap;
                        }

                        .btn-confirm {
                            padding: 1rem 2rem;
                            background: linear-gradient(135deg, var(--success) 0%, var(--success-dark) 100%);
                            color: white;
                            border: none;
                            border-radius: var(--radius-lg);
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            font-weight: 700;
                            cursor: pointer;
                            transition: all var(--transition-base);
                            box-shadow: 0 4px 15px rgba(16, 185, 129, 0.4);
                        }

                        .btn-confirm:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 8px 25px rgba(16, 185, 129, 0.5);
                        }

                        .btn-cancel {
                            padding: 1rem 2rem;
                            background: rgba(255, 255, 255, 0.1);
                            color: white;
                            border: 2px solid rgba(255, 255, 255, 0.3);
                            border-radius: var(--radius-lg);
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            font-weight: 600;
                            text-decoration: none;
                            transition: all var(--transition-base);
                        }

                        .btn-cancel:hover {
                            background: rgba(255, 255, 255, 0.2);
                            transform: translateY(-3px);
                        }

                        .required::after {
                            content: " *";
                            color: #ff6b6b;
                        }

                        @media (max-width: 600px) {
                            .yape-container {
                                padding: 1.5rem;
                            }

                            .qr-section {
                                flex-direction: column;
                            }

                            .payment-details {
                                max-width: 100%;
                            }

                            .buttons {
                                flex-direction: column;
                            }

                            .btn-confirm,
                            .btn-cancel {
                                width: 100%;
                                text-align: center;
                            }
                        }
                    </style>
                </head>

                <body>
                    <div class="yape-page">
                        <div class="yape-container">
                            <div class="yape-header">
                                <div class="yape-logo">📱</div>
                                <h1>Pago con Yape</h1>
                                <p>UltraTech - Servicio Técnico</p>
                            </div>

                            <div class="ticket-info-card">
                                <p><strong>🎫 Ticket ID:</strong>
                                    <%= ticketId %>
                                </p>
                                <p><strong>📝 Descripción:</strong>
                                    <%= descripcion %>
                                </p>
                                <p><strong>💰 Monto a pagar:</strong> S/ <%= monto %>
                                </p>
                            </div>

                            <form action="pay" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="ticketId" value="<%= ticketId %>">
                                <input type="hidden" name="method" value="<%= method %>">
                                <input type="hidden" name="action" value="confirm">
                                <input type="hidden" name="monto" value="<%= monto %>">

                                <div class="qr-section">
                                    <div class="qr-code">
                                        <img src="assets/images/yape-924045900.png" alt="QR Yape">
                                    </div>
                                    <div class="payment-details">
                                        <p><strong>Número Yape:</strong></p>
                                        <div class="phone-number">📞 924 045 900</div>
                                        <p><strong>Nombre:</strong> Lesly Esther Matienzo Huamani</p>
                                        <div class="concepto">
                                            <strong>Concepto obligatorio:</strong><br>
                                            Ticket <%= ticketId %>
                                        </div>
                                    </div>
                                </div>

                                <div class="instructions">
                                    <h3>📋 Instrucciones:</h3>
                                    <ol>
                                        <li><strong>Abre la app de Yape</strong> en tu celular.</li>
                                        <li><strong>Escanea el código QR</strong> o busca el número <strong>924 045
                                                900</strong>.</li>
                                        <li><strong>Transfiere el monto exacto:</strong> <strong
                                                style="color: var(--primary-light);">S/ <%= monto %></strong>.</li>
                                        <li><strong>Ingresa el concepto:</strong> Ticket <%= ticketId %>
                                        </li>
                                        <li><strong>Toma screenshot</strong> del comprobante.</li>
                                    </ol>
                                </div>

                                <div class="form-group">
                                    <label for="numeroOperacion" class="required">Número de Operación Yape:</label>
                                    <input type="text" id="numeroOperacion" name="numeroOperacion"
                                        placeholder="Ej: 123456789" required>
                                    <small>Encuéntralo en el comprobante de Yape</small>
                                </div>

                                <div class="form-group">
                                    <label for="comprobante" class="required">Comprobante de Pago:</label>
                                    <input type="file" id="comprobante" name="comprobante" accept="image/*" required>
                                    <small>📷 Sube la captura del comprobante (JPG, PNG - Máx. 5MB)</small>
                                    <div class="file-preview" id="filePreview"></div>
                                </div>

                                <div class="buttons">
                                    <button type="submit" class="btn-confirm">✅ Confirmar Pago</button>
                                    <a href="tickets" class="btn-cancel">❌ Cancelar</a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <script>
                        document.getElementById('comprobante').addEventListener('change', function (e) {
                            const file = e.target.files[0];
                            const preview = document.getElementById('filePreview');

                            if (file) {
                                if (file.size > 5 * 1024 * 1024) {
                                    alert('El archivo es muy grande. Máximo 5MB.');
                                    e.target.value = '';
                                    preview.innerHTML = '';
                                    return;
                                }

                                if (!file.type.match('image.*')) {
                                    alert('Solo se permiten imágenes.');
                                    e.target.value = '';
                                    preview.innerHTML = '';
                                    return;
                                }

                                const reader = new FileReader();
                                reader.onload = function (e) {
                                    preview.innerHTML = '<img src="' + e.target.result + '" alt="Vista previa">';
                                }
                                reader.readAsDataURL(file);
                            } else {
                                preview.innerHTML = '';
                            }
                        });

                        document.querySelector('form').addEventListener('submit', function (e) {
                            const comprobante = document.getElementById('comprobante');
                            const numeroOperacion = document.getElementById('numeroOperacion');

                            if (!comprobante.files || comprobante.files.length === 0) {
                                alert('Por favor sube el comprobante.');
                                e.preventDefault();
                                return;
                            }

                            if (!numeroOperacion.value.trim()) {
                                alert('Ingresa el número de operación.');
                                e.preventDefault();
                                return;
                            }

                            if (!confirm('¿Confirmas que realizaste el pago?')) {
                                e.preventDefault();
                            }
                        });
                    </script>
                </body>

                </html>