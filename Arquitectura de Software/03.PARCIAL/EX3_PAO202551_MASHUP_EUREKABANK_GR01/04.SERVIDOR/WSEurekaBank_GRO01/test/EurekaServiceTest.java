import org.junit.Test;
import static org.junit.Assert.*;

public class EurekaServiceTest {

    @Test
    public void testValidarCalculoSaldo() {
        // ARRANGE (Preparar el escenario)
        double saldoActual = 100.50;
        double montoDeposito = 50.25;
        double esperado = 150.75;

        // ACT (Ejecutar la acción a probar)
        // Simulamos la operación que hace tu EurekaService
        double resultadoObtenido = saldoActual + montoDeposito;

        // ASSERT (Verificar el resultado)
        // El tercer parámetro (0.001) es el margen de error para decimales
        assertEquals("El cálculo del depósito es incorrecto", esperado, resultadoObtenido, 0.001);
    }
    
    @Test
    public void testValidarSaldoInsuficiente() {
        double saldoActual = 20.00;
        double montoRetiro = 50.00;
        
        // La lógica dice que si el retiro es mayor al saldo, debe fallar
        boolean puedeRetirar = (saldoActual >= montoRetiro);
        
        assertFalse("El sistema permitió retirar más dinero del que existe", puedeRetirar);
    }
}