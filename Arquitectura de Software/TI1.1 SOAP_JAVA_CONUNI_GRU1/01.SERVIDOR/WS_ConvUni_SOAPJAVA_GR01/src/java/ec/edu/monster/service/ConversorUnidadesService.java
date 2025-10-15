package ec.edu.monster.service;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@javax.jws.HandlerChain(file = "handler-chain.xml")
@WebService(serviceName = "ConversorUnidadesService")
public class ConversorUnidadesService {
    // =======================
    // 🔹 LONGITUD
    // =======================

    @WebMethod(operationName = "metros_a_kilometros")
    public double metros_a_kilometros(@WebParam(name = "metros") double metros) {
        return metros / 1000.0;
    }

    @WebMethod(operationName = "kilometros_a_metros")
    public double kilometros_a_metros(@WebParam(name = "kilometros") double kilometros) {
        return kilometros * 1000.0;
    }

    @WebMethod(operationName = "centimetros_a_metros")
    public double centimetros_a_metros(@WebParam(name = "centimetros") double centimetros) {
        return centimetros / 100.0;
    }

    // =======================
    // 🔹 TEMPERATURA
    // =======================

    @WebMethod(operationName = "celsius_a_fahrenheit")
    public double celsius_a_fahrenheit(@WebParam(name = "celsius") double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    @WebMethod(operationName = "fahrenheit_a_celsius")
    public double fahrenheit_a_celsius(@WebParam(name = "fahrenheit") double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    @WebMethod(operationName = "celsius_a_kelvin")
    public double celsius_a_kelvin(@WebParam(name = "celsius") double celsius) {
        return celsius + 273.15;
    }

    // =======================
    // 🔹 MASA
    // =======================

    @WebMethod(operationName = "gramos_a_kilogramos")
    public double gramos_a_kilogramos(@WebParam(name = "gramos") double gramos) {
        return gramos / 1000.0;
    }

    @WebMethod(operationName = "kilogramos_a_gramos")
    public double kilogramos_a_gramos(@WebParam(name = "kilogramos") double kilogramos) {
        return kilogramos * 1000.0;
    }

    @WebMethod(operationName = "libras_a_kilogramos")
    public double libras_a_kilogramos(@WebParam(name = "libras") double libras) {
        return libras * 0.45359237;
    }
}
