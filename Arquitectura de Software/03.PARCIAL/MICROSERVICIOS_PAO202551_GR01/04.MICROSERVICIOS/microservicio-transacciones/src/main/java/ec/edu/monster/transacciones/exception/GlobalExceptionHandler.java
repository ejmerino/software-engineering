package ec.edu.monster.transacciones.exception; // Ajusta el paquete según tu estructura

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", ex.getMessage(), "status", 500));
    }

    @ExceptionHandler(CannotAcquireLockException.class)
    public ResponseEntity<?> handleLock(CannotAcquireLockException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("mensaje", "La cuenta está siendo procesada por otra ventanilla. Reintente.", "status", 503));
    }
}