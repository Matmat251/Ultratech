<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - TopTech</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        body {
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            padding: 30px;
            text-align: center;
        }
        
        .logo-container {
            margin-bottom: 20px;
        }
        
        .logo {
            max-width: 150px;
            height: auto;
        }
        
        .title {
            color: #333;
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .subtitle {
            color: #666;
            font-size: 18px;
            margin-bottom: 30px;
        }
        
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        input[type="text"] {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            transition: border-color 0.3s;
        }
        
        input[type="text"]:focus {
            border-color: #4a90e2;
            outline: none;
        }
        
        .btn {
            background-color: #4a90e2;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 12px 20px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s;
        }
        
        .btn:hover {
            background-color: #3a7bc8;
        }
        
        .error-message {
            background-color: #ffebee;
            color: #c62828;
            padding: 10px;
            border-radius: 5px;
            margin-top: 20px;
            border-left: 4px solid #c62828;
        }
        
        @media (max-width: 480px) {
            .container {
                padding: 20px;
            }
            
            .title {
                font-size: 24px;
            }
            
            .subtitle {
                font-size: 16px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo-container">
            <img src="assets/images/LOGO.jpg" alt="Logo TOP TECH" class="logo">
        </div>
        
        <h1 class="title">TOP TECH</h1>
        <p class="subtitle">SERVICIO TÉCNICO</p>
        
        <form action="login" method="post">
            <div class="form-group">
                <label for="dni">DNI o RUC</label>
                <input type="text" id="dni" name="dni" placeholder="Ingrese su DNI o RUC" required>
            </div>
            
            <button type="submit" class="btn">Ingresar</button>
        </form>

        <!-- Mostrar mensajes de error -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                Error: <%= request.getAttribute("error") %>
            </div>
        <% } %>
    </div>
</body>
</html>