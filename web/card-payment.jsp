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
                    <title>Pago con Tarjeta - UltraTech</title>
                    <link rel="stylesheet" href="assets/css/styles.css">
                    <style>
                        .card-page {
                            min-height: 100vh;
                            background: linear-gradient(135deg, var(--primary-lighter) 0%, var(--primary-bg) 50%, var(--white) 100%);
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 2rem;
                        }

                        .card-container {
                            max-width: 520px;
                            width: 100%;
                            background: var(--white);
                            border-radius: var(--radius-2xl);
                            box-shadow: 0 25px 50px rgba(11, 111, 194, 0.15);
                            overflow: hidden;
                            animation: fadeInUp 0.5s ease-out;
                        }

                        .card-header {
                            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
                            color: var(--white);
                            padding: 2rem;
                            text-align: center;
                            position: relative;
                        }

                        .card-header::after {
                            content: '';
                            position: absolute;
                            bottom: 0;
                            left: 0;
                            right: 0;
                            height: 4px;
                            background: linear-gradient(90deg, var(--primary-light), var(--success), var(--primary-light));
                        }

                        .card-header h1 {
                            font-size: var(--font-size-2xl);
                            font-weight: 700;
                            margin-bottom: 0.5rem;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            gap: 0.75rem;
                        }

                        .card-header p {
                            opacity: 0.9;
                            font-size: var(--font-size-sm);
                            letter-spacing: 1px;
                        }

                        .ticket-summary {
                            background: linear-gradient(180deg, var(--primary-bg) 0%, var(--primary-lighter) 100%);
                            padding: 1.25rem 2rem;
                            border-bottom: 1px solid rgba(11, 111, 194, 0.1);
                        }

                        .ticket-summary p {
                            margin: 0.375rem 0;
                            color: var(--primary-darker);
                            font-size: var(--font-size-sm);
                        }

                        .ticket-summary strong {
                            color: var(--primary-dark);
                        }

                        .payment-form {
                            padding: 2rem;
                        }

                        .form-group {
                            margin-bottom: 1.5rem;
                        }

                        .form-group label {
                            display: block;
                            margin-bottom: 0.625rem;
                            font-size: var(--font-size-sm);
                            font-weight: 600;
                            color: var(--gray-700);
                        }

                        .form-group label.required::after {
                            content: ' *';
                            color: var(--danger);
                        }

                        .form-group input {
                            width: 100%;
                            padding: 1rem;
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            color: var(--gray-800);
                            background: var(--gray-50);
                            border: 2px solid var(--gray-200);
                            border-radius: var(--radius-lg);
                            transition: all var(--transition-base);
                        }

                        .form-group input:focus {
                            outline: none;
                            border-color: var(--primary);
                            background: var(--white);
                            box-shadow: 0 0 0 4px rgba(11, 111, 194, 0.1);
                        }

                        .card-icons {
                            display: flex;
                            gap: 0.75rem;
                            margin-top: 0.75rem;
                        }

                        .card-icon {
                            width: 50px;
                            height: 32px;
                            background: linear-gradient(135deg, var(--gray-50) 0%, var(--primary-lighter) 100%);
                            border: 1px solid var(--primary-light);
                            border-radius: var(--radius-sm);
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 11px;
                            font-weight: 700;
                            color: var(--primary);
                        }

                        .card-details {
                            display: grid;
                            grid-template-columns: 2fr 1fr;
                            gap: 1rem;
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
                            background: var(--gray-50);
                        }

                        .demo-notice {
                            background: linear-gradient(135deg, var(--warning-light) 0%, #fef3c7 100%);
                            border: 1px solid var(--warning);
                            border-radius: var(--radius-lg);
                            padding: 1rem 1.25rem;
                            margin: 1.5rem 0;
                            text-align: center;
                            color: #92400e;
                            font-size: var(--font-size-sm);
                        }

                        .buttons {
                            display: grid;
                            grid-template-columns: 1fr 1fr;
                            gap: 1rem;
                            margin-top: 2rem;
                        }

                        .btn-confirm {
                            padding: 1rem;
                            background: linear-gradient(135deg, var(--success) 0%, var(--success-dark) 100%);
                            color: var(--white);
                            border: none;
                            border-radius: var(--radius-lg);
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            font-weight: 700;
                            cursor: pointer;
                            transition: all var(--transition-base);
                            box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
                        }

                        .btn-confirm:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
                        }

                        .btn-cancel {
                            padding: 1rem;
                            background: linear-gradient(135deg, var(--gray-600) 0%, var(--gray-700) 100%);
                            color: var(--white);
                            border: none;
                            border-radius: var(--radius-lg);
                            font-family: var(--font-family);
                            font-size: var(--font-size-base);
                            font-weight: 600;
                            text-decoration: none;
                            text-align: center;
                            transition: all var(--transition-base);
                        }

                        .btn-cancel:hover {
                            transform: translateY(-3px);
                            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
                        }

                        @media (max-width: 480px) {
                            .card-details {
                                grid-template-columns: 1fr;
                            }

                            .buttons {
                                grid-template-columns: 1fr;
                            }

                            .card-container {
                                margin: 0.5rem;
                            }
                        }
                    </style>
                </head>

                <body>
                    <div class="card-page">
                        <div class="card-container">
                            <div class="card-header">
                                <h1>💳 Pago con Tarjeta</h1>
                                <p>UltraTech - Servicio Técnico</p>
                            </div>

                            <div class="ticket-summary">
                                <p><strong>🎫 Ticket ID:</strong>
                                    <%= ticketId %>
                                </p>
                                <p><strong>📝 Descripción:</strong>
                                    <%= descripcion %>
                                </p>
                                <p><strong>💰 Monto a pagar:</strong> S/ <%= monto %>
                                </p>
                            </div>

                            <form action="pay" method="post" class="payment-form" enctype="multipart/form-data">
                                <input type="hidden" name="ticketId" value="<%= ticketId %>">
                                <input type="hidden" name="method" value="<%= method %>">
                                <input type="hidden" name="action" value="confirm">
                                <input type="hidden" name="monto" value="<%= monto %>">

                                <div class="form-group">
                                    <label for="cardNumber" class="required">Número de Tarjeta</label>
                                    <input type="text" id="cardNumber" name="cardNumber"
                                        placeholder="1234 5678 9012 3456" maxlength="19" required>
                                    <div class="card-icons">
                                        <div class="card-icon">VISA</div>
                                        <div class="card-icon">MC</div>
                                        <div class="card-icon">AMEX</div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="cardHolder" class="required">Nombre del Titular</label>
                                    <input type="text" id="cardHolder" name="cardHolder" placeholder="NOMBRE APELLIDO"
                                        required>
                                </div>

                                <div class="card-details">
                                    <div class="form-group">
                                        <label for="expiryDate" class="required">Fecha de Expiración</label>
                                        <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/AA"
                                            maxlength="5" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="cvv" class="required">CVV</label>
                                        <input type="text" id="cvv" name="cvv" placeholder="123" maxlength="3" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="numeroOperacion">Número de Operación</label>
                                    <input type="text" id="numeroOperacion" name="numeroOperacion"
                                        placeholder="OP123456789">
                                </div>

                                <div class="form-group">
                                    <label for="comprobante" class="required">Comprobante de Pago</label>
                                    <input type="file" id="comprobante" name="comprobante" accept="image/*" required>
                                    <small
                                        style="color: var(--gray-500); display: block; margin-top: 0.5rem; font-size: 13px;">
                                        📷 Captura del comprobante (JPG, PNG - Máx. 5MB)
                                    </small>
                                    <div class="file-preview" id="filePreview"></div>
                                </div>

                                <div class="demo-notice">
                                    💡 <strong>Modo Demo:</strong> Usa cualquier número para probar.<br>
                                    <strong>IMPORTANTE:</strong> Sube el comprobante real.
                                </div>

                                <div class="buttons">
                                    <button type="submit" class="btn-confirm">✅ Confirmar</button>
                                    <a href="tickets" class="btn-cancel">❌ Cancelar</a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <script>
                        // Format card number
                        document.getElementById('cardNumber').addEventListener('input', function (e) {
                            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
                            let formatted = value.match(/.{1,4}/g)?.join(' ');
                            e.target.value = formatted || value;
                        });

                        // Format expiry date
                        document.getElementById('expiryDate').addEventListener('input', function (e) {
                            let value = e.target.value.replace(/\//g, '').replace(/[^0-9]/gi, '');
                            if (value.length >= 2) {
                                value = value.substring(0, 2) + '/' + value.substring(2, 4);
                            }
                            e.target.value = value;
                        });

                        // Limit CVV
                        document.getElementById('cvv').addEventListener('input', function (e) {
                            this.value = this.value.replace(/[^0-9]/g, '').substring(0, 3);
                        });

                        // File preview
                        document.getElementById('comprobante').addEventListener('change', function (e) {
                            const file = e.target.files[0];
                            const preview = document.getElementById('filePreview');

                            if (file) {
                                if (file.size > 5 * 1024 * 1024) {
                                    alert('Archivo muy grande. Máximo 5MB.');
                                    e.target.value = '';
                                    preview.innerHTML = '';
                                    return;
                                }

                                if (!file.type.match('image.*')) {
                                    alert('Solo imágenes permitidas.');
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

                        // Validate form
                        document.querySelector('form').addEventListener('submit', function (e) {
                            const fileInput = document.getElementById('comprobante');
                            if (!fileInput.files || fileInput.files.length === 0) {
                                e.preventDefault();
                                alert('Por favor sube el comprobante.');
                                return;
                            }
                        });
                    </script>
                </body>

                </html>