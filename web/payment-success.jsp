<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% String ticketId=request.getParameter("ticketId"); String method=request.getParameter("method"); String
        monto=request.getParameter("monto"); if (ticketId==null) { response.sendRedirect("tickets"); return; } %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Pago Exitoso - UltraTech</title>
            <link rel="stylesheet" href="assets/css/styles.css">
            <style>
                .success-page {
                    min-height: 100vh;
                    background: linear-gradient(135deg, var(--primary-lighter) 0%, var(--success-light) 50%, var(--primary-bg) 100%);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    padding: 2rem;
                    position: relative;
                    overflow: hidden;
                }

                /* Confetti animation */
                .confetti {
                    position: absolute;
                    width: 100%;
                    height: 100%;
                    pointer-events: none;
                    overflow: hidden;
                }

                .confetti-piece {
                    position: absolute;
                    width: 10px;
                    height: 10px;
                    background: var(--success);
                    animation: confetti-fall 3s ease-out forwards;
                }

                @keyframes confetti-fall {
                    0% {
                        transform: translateY(-100vh) rotate(0deg);
                        opacity: 1;
                    }

                    100% {
                        transform: translateY(100vh) rotate(720deg);
                        opacity: 0;
                    }
                }

                .success-container {
                    background: var(--white);
                    padding: 3rem 2.5rem;
                    border-radius: var(--radius-2xl);
                    box-shadow: 0 25px 50px rgba(16, 185, 129, 0.15);
                    max-width: 500px;
                    width: 100%;
                    text-align: center;
                    position: relative;
                    z-index: 10;
                    animation: scaleIn 0.5s ease-out;
                }

                .success-container::before {
                    content: '';
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    height: 5px;
                    background: linear-gradient(90deg, var(--success), var(--primary-light), var(--success));
                    border-radius: var(--radius-2xl) var(--radius-2xl) 0 0;
                }

                .success-icon {
                    width: 120px;
                    height: 120px;
                    background: linear-gradient(135deg, var(--success) 0%, var(--success-dark) 100%);
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    margin: 0 auto 1.5rem;
                    box-shadow: 0 15px 40px rgba(16, 185, 129, 0.4);
                    animation: pulse 2s ease-in-out infinite;
                }

                .success-icon .checkmark {
                    width: 60px;
                    height: 60px;
                }

                .success-icon .checkmark path {
                    stroke: white;
                    stroke-width: 4;
                    stroke-linecap: round;
                    stroke-linejoin: round;
                    fill: none;
                    stroke-dasharray: 100;
                    stroke-dashoffset: 100;
                    animation: checkmark 0.8s ease-out 0.5s forwards;
                }

                @keyframes checkmark {
                    to {
                        stroke-dashoffset: 0;
                    }
                }

                h1 {
                    color: var(--gray-800);
                    font-size: var(--font-size-2xl);
                    font-weight: 700;
                    margin-bottom: 1.5rem;
                    animation: fadeInUp 0.5s ease-out 0.3s backwards;
                }

                .info-card {
                    background: linear-gradient(180deg, var(--success-light) 0%, #d1fae5 100%);
                    padding: 1.5rem;
                    border-radius: var(--radius-xl);
                    margin-bottom: 1.5rem;
                    text-align: left;
                    animation: fadeInUp 0.5s ease-out 0.4s backwards;
                }

                .info-card .info-row {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    padding: 0.625rem 0;
                    border-bottom: 1px solid rgba(16, 185, 129, 0.2);
                }

                .info-card .info-row:last-child {
                    border-bottom: none;
                }

                .info-card .info-icon {
                    font-size: 1.25rem;
                }

                .info-card .info-label {
                    color: var(--gray-600);
                    font-size: var(--font-size-sm);
                }

                .info-card .info-value {
                    margin-left: auto;
                    font-weight: 700;
                    color: var(--success-dark);
                }

                .message {
                    color: var(--gray-600);
                    font-size: var(--font-size-sm);
                    line-height: 1.7;
                    margin-bottom: 2rem;
                    padding: 0 1rem;
                    animation: fadeInUp 0.5s ease-out 0.5s backwards;
                }

                .btn-back {
                    display: inline-flex;
                    align-items: center;
                    gap: 0.5rem;
                    padding: 1rem 2rem;
                    background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
                    color: var(--white);
                    text-decoration: none;
                    border-radius: var(--radius-lg);
                    font-weight: 700;
                    font-size: var(--font-size-base);
                    transition: all var(--transition-base);
                    box-shadow: 0 4px 20px rgba(11, 111, 194, 0.35);
                    animation: fadeInUp 0.5s ease-out 0.6s backwards;
                }

                .btn-back:hover {
                    transform: translateY(-3px);
                    box-shadow: 0 8px 30px rgba(11, 111, 194, 0.45);
                }

                .brand-footer {
                    margin-top: 2rem;
                    padding-top: 1.5rem;
                    border-top: 1px solid var(--gray-200);
                    animation: fadeIn 0.5s ease-out 0.7s backwards;
                }

                .brand-footer p {
                    color: var(--gray-400);
                    font-size: var(--font-size-xs);
                }

                @media (max-width: 480px) {
                    .success-container {
                        padding: 2rem 1.5rem;
                        margin: 0.5rem;
                    }

                    .success-icon {
                        width: 100px;
                        height: 100px;
                    }

                    .success-icon .checkmark {
                        width: 50px;
                        height: 50px;
                    }

                    h1 {
                        font-size: var(--font-size-xl);
                    }
                }
            </style>
        </head>

        <body>
            <div class="success-page">
                <!-- Confetti -->
                <div class="confetti" id="confetti"></div>

                <div class="success-container">
                    <div class="success-icon">
                        <svg class="checkmark" viewBox="0 0 52 52">
                            <path d="M14 27l10 10 14-20" />
                        </svg>
                    </div>

                    <h1>¡Pago Registrado!</h1>

                    <div class="info-card">
                        <div class="info-row">
                            <span class="info-icon">🎫</span>
                            <span class="info-label">Ticket ID</span>
                            <span class="info-value">
                                <%= ticketId %>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-icon">💳</span>
                            <span class="info-label">Método</span>
                            <span class="info-value">
                                <%= method !=null ? method.toUpperCase() : "N/A" %>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-icon">💰</span>
                            <span class="info-label">Monto</span>
                            <span class="info-value">S/ <%= monto !=null ? monto : "0.00" %></span>
                        </div>
                    </div>

                    <p class="message">
                        Tu comprobante ha sido registrado correctamente y será verificado por nuestro equipo técnico.
                        Recibirás una confirmación pronto.
                    </p>

                    <a href="tickets" class="btn-back">
                        ← Volver a Mis Tickets
                    </a>

                    <div class="brand-footer">
                        <p>© 2025 UltraTech - Servicio Técnico</p>
                    </div>
                </div>
            </div>

            <script>
                // Create confetti
                function createConfetti() {
                    const container = document.getElementById('confetti');
                    const colors = ['#10b981', '#0b6fc2', '#f59e0b', '#3b82f6', '#8b5cf6'];

                    for (let i = 0; i < 50; i++) {
                        const piece = document.createElement('div');
                        piece.className = 'confetti-piece';
                        piece.style.left = Math.random() * 100 + '%';
                        piece.style.animationDelay = Math.random() * 2 + 's';
                        piece.style.background = colors[Math.floor(Math.random() * colors.length)];
                        piece.style.transform = 'rotate(' + Math.random() * 360 + 'deg)';
                        container.appendChild(piece);
                    }
                }

                createConfetti();
            </script>
        </body>

        </html>