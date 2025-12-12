package ultratech.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getParameter("path");
        System.out.println("=================================");
        System.out.println("🖼️ ImageServlet - REQUEST RECIBIDO");
        System.out.println("=================================");
        System.out.println("📥 Path solicitado: '" + path + "'");
        
        if (path == null || path.isEmpty()) {
            System.out.println("❌ ERROR: path es null o vacío");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'path' requerido");
            return;
        }
        
        // ✅ RUTA BASE DE TOMCAT
        String basePath = "C:/apache-tomcat-11.0.11/apache-tomcat-11.0.11/webapps/toptechweb";
        
        // ✅ CONSTRUIR RUTA COMPLETA CORRECTAMENTE
        String fullPath;
        if (path.startsWith("uploads/")) {
            // El path YA incluye "uploads/pagos/archivo.jpg"
            fullPath = basePath + "/" + path;
        } else if (path.startsWith("/uploads/")) {
            // El path tiene "/" al inicio
            fullPath = basePath + path;
        } else {
            // Solo tiene el nombre del archivo
            fullPath = basePath + "/uploads/pagos/" + path;
        }
        
        System.out.println("📂 Ruta base: " + basePath);
        System.out.println("🔗 Path limpio: " + path);
        System.out.println("📍 Ruta completa: " + fullPath);
        
        File file = new File(fullPath);
        System.out.println("📁 Archivo existe: " + file.exists());
        System.out.println("📁 Es archivo: " + file.isFile());
        System.out.println("📁 Puede leer: " + file.canRead());
        
        if (!file.exists()) {
            System.out.println("❌ ARCHIVO NO ENCONTRADO");
            System.out.println("💡 Verifica que exista en: " + fullPath);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, 
                "Imagen no encontrada en: " + fullPath);
            return;
        }
        
        if (!file.isFile()) {
            System.out.println("❌ LA RUTA NO ES UN ARCHIVO");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                "La ruta no apunta a un archivo válido");
            return;
        }
        
        // Determinar tipo MIME
        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "image/jpeg"; // Default
        }
        
        System.out.println("📄 MIME Type: " + mimeType);
        System.out.println("📦 Tamaño archivo: " + file.length() + " bytes");
        
        // Configurar headers
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        response.setHeader("Cache-Control", "max-age=3600");
        
        // Servir archivo
        System.out.println("📤 Enviando archivo...");
        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            int totalBytes = 0;
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            
            System.out.println("✅ Imagen servida EXITOSAMENTE");
            System.out.println("📊 Bytes enviados: " + totalBytes);
            
        } catch (IOException e) {
            System.out.println("❌ ERROR al servir imagen: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        System.out.println("=================================\n");
    }
}