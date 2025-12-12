    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - UltraTech</title>
    <link rel="stylesheet" href="assets/css/styles.css">
    <style>
        /* ========== LOGIN PAGE STYLES ========== */
        .login-page {
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: var(--spacing-lg);
            position: relative;
            overflow: hidden;
            background: linear-gradient(135deg, #0b2b60 0%, #094e87 30%, #0b6fc2 60%, #7ed0f9 100%);
        }
        
        /* Animated background elements */
        .login-page::before {
            content: '';
            position: absolute;
            width: 600px;
            height: 600px;
            background: radial-gradient(circle, rgba(126, 208, 249, 0.15) 0%, transparent 70%);
            top: -200px;
            right: -200px;
            animation: float 8s ease-in-out infinite;
        }
        
        .login-page::after {
            content: '';
            position: absolute;
            width: 400px;
            height: 400px;
            background: radial-gradient(circle, rgba(126, 208, 249, 0.1) 0%, transparent 70%);
            bottom: -100px;
            left: -100px;
            animation: float 6s ease-in-out infinite reverse;
        }
        
        /* Floating particles */
        .particles {
            position: absolute;
            width: 100%;
            height: 100%;
            overflow: hidden;
            pointer-events: none;
        }
        
        .particle {
            position: absolute;
            width: 6px;
            height: 6px;
            background: rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            animation: float 4s ease-in-out infinite;
        }
        
        .particle:nth-child(1) { left: 10%; top: 20%; animation-delay: 0s; }
        .particle:nth-child(2) { left: 20%; top: 80%; animation-delay: 1s; }
        .particle:nth-child(3) { left: 60%; top: 10%; animation-delay: 2s; }
        .particle:nth-child(4) { left: 80%; top: 70%; animation-delay: 0.5s; }
        .particle:nth-child(5) { left: 40%; top: 50%; animation-delay: 1.5s; }
        .particle:nth-child(6) { left: 90%; top: 30%; animation-delay: 2.5s; }
        
        /* Login Card */
        .login-card {
            width: 100%;
            max-width: 440px;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            border-radius: var(--radius-2xl);
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.4),
                        0 0 0 1px rgba(255, 255, 255, 0.1);
            padding: 3rem 2.5rem;
            position: relative;
            z-index: 10;
            animation: fadeInUp 0.6s ease-out;
        }
        
        /* Gradient border effect */
        .login-card::before {
            content: '';
            position: absolute;
            top: -2px;
            left: -2px;
            right: -2px;
            bottom: -2px;
            background: linear-gradient(135deg, var(--primary-light), var(--primary), var(--primary-light));
            border-radius: calc(var(--radius-2xl) + 2px);
            z-index: -1;
            opacity: 0.5;
            animation: gradientMove 3s ease infinite;
            background-size: 200% 200%;
        }
        
        /* Logo */
        .login-logo {
            text-align: center;
            margin-bottom: var(--spacing-xl);
        }
        
        .login-logo img {
            max-width: 120px;
            height: auto;
            border-radius: var(--radius-lg);
            box-shadow: var(--shadow-lg);
            transition: transform var(--transition-base);
            animation: scaleIn 0.5s ease-out 0.2s backwards;
        }
        
        .login-logo img:hover {
            transform: scale(1.05) rotate(-2deg);
        }
        
        /* Brand Name */
        .brand-name {
            text-align: center;
            margin-bottom: var(--spacing-xs);
        }
        
        .brand-name h1 {
            font-size: var(--font-size-3xl);
            font-weight: 800;
            color: var(--primary-darker);
            letter-spacing: 3px;
            text-transform: uppercase;
            animation: fadeInUp 0.5s ease-out 0.3s backwards;
        }
        
        .brand-tagline {
            text-align: center;
            margin-bottom: var(--spacing-2xl);
            animation: fadeInUp 0.5s ease-out 0.4s backwards;
        }
        
        .brand-tagline p {
            font-size: var(--font-size-sm);
            font-weight: 600;
            color: var(--primary);
            letter-spacing: 4px;
            text-transform: uppercase;
        }
        
        /* Form */
        .login-form {
            animation: fadeInUp 0.5s ease-out 0.5s backwards;
        }
        
        .input-group {
            position: relative;
            margin-bottom: var(--spacing-lg);
        }
        
        .input-group label {
            display: block;
            margin-bottom: var(--spacing-sm);
            font-size: var(--font-size-sm);
            font-weight: 600;
            color: var(--gray-700);
        }
        
        .input-wrapper {
            position: relative;
        }
        
        .input-wrapper .icon {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            font-size: 1.25rem;
            color: var(--gray-400);
            transition: color var(--transition-base);
            pointer-events: none;
        }
        
        .input-wrapper input {
            width: 100%;
            padding: 1rem 1rem 1rem 3rem;
            font-family: var(--font-family);
            font-size: var(--font-size-base);
            color: var(--gray-800);
            background: var(--gray-50);
            border: 2px solid var(--gray-200);
            border-radius: var(--radius-lg);
            transition: all var(--transition-base);
        }
        
        .input-wrapper input:focus {
            outline: none;
            border-color: var(--primary);
            background: var(--white);
            box-shadow: 0 0 0 4px rgba(11, 111, 194, 0.15);
        }
        
        .input-wrapper input:focus + .icon,
        .input-wrapper input:not(:placeholder-shown) + .icon {
            color: var(--primary);
        }
        
        .input-wrapper input::placeholder {
            color: var(--gray-400);
        }
        
        /* Submit Button */
        .login-btn {
            width: 100%;
            padding: 1rem;
            font-family: var(--font-family);
            font-size: var(--font-size-base);
            font-weight: 700;
            color: var(--white);
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            border: none;
            border-radius: var(--radius-lg);
            cursor: pointer;
            transition: all var(--transition-base);
            box-shadow: 0 4px 20px rgba(11, 111, 194, 0.4);
            text-transform: uppercase;
            letter-spacing: 2px;
            position: relative;
            overflow: hidden;
        }
        
        .login-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
            transition: left 0.5s ease;
        }
        
        .login-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 30px rgba(11, 111, 194, 0.5);
        }
        
        .login-btn:hover::before {
            left: 100%;
        }
        
        .login-btn:active {
            transform: translateY(-1px);
        }
        
        /* Error Message */
        .error-message {
            margin-top: var(--spacing-lg);
            padding: 1rem 1.25rem;
            background: linear-gradient(135deg, var(--danger-light) 0%, #fecaca 100%);
            border-left: 4px solid var(--danger);
            border-radius: var(--radius-lg);
            color: #b91c1c;
            font-size: var(--font-size-sm);
            font-weight: 500;
            animation: shake 0.5s ease-out, fadeIn 0.3s ease-out;
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
        }
        
        .error-message .error-icon {
            font-size: 1.25rem;
        }
        
        /* Footer info */
        .login-footer {
            margin-top: var(--spacing-xl);
            text-align: center;
            animation: fadeIn 0.5s ease-out 0.7s backwards;
        }
        
        .login-footer p {
            font-size: var(--font-size-xs);
            color: var(--gray-500);
        }
        
        /* Responsive */
        @media (max-width: 480px) {
            .login-card {
                padding: 2rem 1.5rem;
                margin: var(--spacing-md);
                border-radius: var(--radius-xl);
            }
            
            .brand-name h1 {
                font-size: var(--font-size-2xl);
                letter-spacing: 2px;
            }
            
            .brand-tagline p {
                font-size: var(--font-size-xs);
                letter-spacing: 3px;
            }
            
            .login-logo img {
                max-width: 100px;
            }
        }
    </style>
</head>
<body>
    <div class="login-page">
        <!-- Animated particles -->
        <div class="particles">
            <div class="particle"></div>
            <div class="particle"></div>
            <div class="particle"></div>
            <div class="particle"></div>
            <div class="particle"></div>
            <div class="particle"></div>
        </div>
        
        <div class="login-card">
            <div class="login-logo">
                <img src="assets/images/LOGO1.png" alt="Logo UltraTech">
            </div>
            
            <div class="brand-name">
                <h1>UltraTech</h1>
            </div>
            
            <div class="brand-tagline">
                <p>Servicio Técnico</p>
            </div>
            
            <form action="login" method="post" class="login-form">
                <div class="input-group">
                    <label for="dni">DNI o RUC</label>
                    <div class="input-wrapper">
                        <input 
                            type="text" 
                            id="dni" 
                            name="dni" 
                            placeholder="Ingrese su DNI o RUC" 
                            required
                            autocomplete="off"
                        >
                        <span class="icon">🪪</span>
                    </div>
                </div>
                
                <button type="submit" class="login-btn">
                    Ingresar
                </button>
            </form>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <span class="error-icon">⚠️</span>
                    <span><%= request.getAttribute("error") %></span>
                </div>
            <% } %>
            
            <div class="login-footer">
                <p>© 2025 UltraTech - Todos los derechos reservados</p>
            </div>
        </div>
    </div>
</body>
</html>