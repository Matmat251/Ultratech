package toptech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doLogout(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        System.out.println("🚪 ===== INICIANDO LOGOUT =====");

        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("📋 Invalidando sesión ID: " + session.getId());
            session.invalidate();
        } else {
            System.out.println("ℹ️ No había sesión activa");
        }

        // ✅ Eliminar cookies de sesión también
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                    System.out.println("🧹 Cookie JSESSIONID eliminada");
                }
            }
        }

        // ✅ Prevenir caché completamente
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // ✅ Redirigir con parámetro para evitar volver atrás
        response.sendRedirect("index.jsp?logout=success&ts=" + System.currentTimeMillis());
    }
}
