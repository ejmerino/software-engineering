package ec.edu.monster.test;

import com.google.gson.Gson;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.CatalogoService;
import java.math.BigDecimal;
import java.util.List;

/**
 * Prueba la lógica local del CatalogoService (CRUD del Punto 8).
 */
public class TestCatalogoServicio {

    public static void main(String[] args) {
        CatalogoService servicio = new CatalogoService();
        Gson gson = new Gson();
        
        System.out.println("--- INICIANDO PRUEBA DEL CATÁLOGO ---");
        System.out.println("Usando la conexión: jdbc:mariadb://localhost:3307/db_comercializadora\n");

        try {
            // --- PRUEBA 1: LEER (READ) ---
            System.out.println("--- Prueba 1: Listar Catálogo (GET) ---");
            List<Electrodomestico> catalogo = servicio.listarTodos();
            System.out.println("Electrodomésticos encontrados: " + catalogo.size());
            System.out.println(gson.toJson(catalogo));
            
            // --- PRUEBA 2: CREAR (CREATE) ---
            System.out.println("\n--- Prueba 2: Crear Electrodoméstico (POST) ---");
            Electrodomestico nuevo = new Electrodomestico();
            nuevo.setNombre("Aspiradora Robot");
            nuevo.setDescripcion("Limpia sola, compatible con Alexa");
            nuevo.setPrecioVenta(new BigDecimal("350.75"));
            
            System.out.println("Enviando: " + gson.toJson(nuevo));
            Electrodomestico creado = servicio.crear(nuevo);
            System.out.println("Respuesta (con ID): " + gson.toJson(creado));

        } catch (Exception e) {
            System.err.println("\n--- ERROR EN LA PRUEBA ---");
            e.printStackTrace();
        }
        
        System.out.println("\n--- PRUEBA FINALIZADA ---");
    }
}