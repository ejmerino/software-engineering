package ec.edu.monster.prueba;

import ec.edu.monster.service.ConversorUnidadesService;

public class Prueba01 {
    public static void main(String[] args) {
        ConversorUnidadesService service = new ConversorUnidadesService();

        // 🔹 LONGITUD
        double metros = 5000;
        double kilometros = 4;
        double centimetros = 250;

        System.out.println("=== LONGITUD ===");
        System.out.println(metros + " m = " + service.metros_a_kilometros(metros) + " km");
        System.out.println(kilometros + " km = " + service.kilometros_a_metros(kilometros) + " m");
        System.out.println(centimetros + " cm = " + service.centimetros_a_metros(centimetros) + " m");

        // 🔹 TEMPERATURA
        double celsius = 25;
        double fahrenheit = 77;

        System.out.println("\n=== TEMPERATURA ===");
        System.out.println(celsius + " °C = " + service.celsius_a_fahrenheit(celsius) + " °F");
        System.out.println(fahrenheit + " °F = " + service.fahrenheit_a_celsius(fahrenheit) + " °C");
        System.out.println(celsius + " °C = " + service.celsius_a_kelvin(celsius) + " K");

        // 🔹 MASA
        double gramos = 8000;
        double kilogramos = 4;
        double libras = 10;

        System.out.println("\n=== MASA ===");
        System.out.println(gramos + " g = " + service.gramos_a_kilogramos(gramos) + " kg");
        System.out.println(kilogramos + " kg = " + service.kilogramos_a_gramos(kilogramos) + " g");
        System.out.println(libras + " lb = " + service.libras_a_kilogramos(libras) + " kg");
    }
}
