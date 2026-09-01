package ec.edu.monster.transacciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // <--- OBLIGATORIO
public class MicroservicioTransaccionesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroservicioTransaccionesApplication.class, args);
	}
}