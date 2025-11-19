package ec.edu.monster.utils;

import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Un Servlet de prueba simple para diagnosticar la conexión JNDI,
 * ya que es más básico que JAX-RS.
 */
@WebServlet("/test-servlet-diagnostico") // Esta será la URL
public class DiagnosticoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Prueba de Diagnóstico JNDI</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Diagnóstico del JNDI 'jdbc/banquito'</h1>");
            
            try {
                // 1. Intenta "buscar" el recurso JNDI
                InitialContext ctx = new InitialContext();
                out.println("<p>Buscando 'jdbc/banquito'...</p>");
                
                DataSource ds = (DataSource) ctx.lookup("jdbc/banquito");
                
                out.println("<p>¡ÉXITO! Recurso JNDI encontrado.</p>");
                
                // 2. Intenta usarlo
                out.println("<p>Intentando obtener conexión de la base de datos...</p>");
                ds.getConnection().close(); // Pide la conexión y la cierra
                
                out.println("<h2 style='color:green;'>¡PRUEBA EXITOSA!</h2>");
                out.println("<p>GlassFish se conectó a 'jdbc/banquito' y a la base de datos correctamente.</p>");
                
            } catch (Exception e) {
                // 3. Si algo falla, muestra el error
                out.println("<h2 style='color:red;'>ERROR EN LA PRUEBA</h2>");
                out.println("<p>El despliegue funcionó, pero la conexión JNDI falló.</p>");
                out.println("<p><b>Mensaje del error:</b> " + e.getMessage() + "</p>");
                out.println("<pre>");
                e.printStackTrace(out); // Imprime la traza completa del error
                out.println("</pre>");
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}