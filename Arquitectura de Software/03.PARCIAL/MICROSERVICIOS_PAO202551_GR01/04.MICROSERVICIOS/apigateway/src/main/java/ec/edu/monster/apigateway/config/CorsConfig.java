package ec.edu.monster.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir credenciales (cookies, auth headers)
        config.setAllowCredentials(true);

        // 2. CONFIGURACIÓN "MODO PRESENTACIÓN"
        // En lugar de listar IPs específicas, usamos este patrón comodín.
        // Esto permite que el Frontend se conecte desde CUALQUIER lugar
        // (localhost, IP de la U, Wi-Fi compartido, etc.) sin bloqueos.
        config.addAllowedOriginPattern("*");

        // 3. Permitir todos los headers y métodos (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // 4. Aplicar esta configuración a todas las rutas del Gateway
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}