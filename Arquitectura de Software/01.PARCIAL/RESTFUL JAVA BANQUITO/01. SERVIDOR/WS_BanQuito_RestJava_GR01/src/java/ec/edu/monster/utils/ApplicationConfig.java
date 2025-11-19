package ec.edu.monster.utils;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configura JAX-RS.
 * @ApplicationPath("api") establece que todos los servicios
 * estarán bajo la URL /api/*
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {
    // No se necesita nada más aquí.
}