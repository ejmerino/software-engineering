/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.gr03.controller;

import ec.edu.eurekabank.model.Movimiento;
import ec.edu.eurekabank.model.Cuenta;
import ec.edu.eurekabank.service.EurekaService;
import java.util.ArrayList;
import java.util.List;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;

/**
 *
 * @author DELL
 * WebService SOAP para integración con Bonita BPM
 */
@WebService(serviceName = "WSEureka")
public class WSEureka {

    /**
     * Consulta los movimientos de una cuenta
     * Operación requerida por BPMN: consultarMovimientos
     * @param cuenta Número de cuenta
     * @return Lista de movimientos de la cuenta
     */
    @WebMethod(operationName = "consultarMovimientos")
    @WebResult(name = "movimiento")
    public List<Movimiento> consultarMovimientos(@WebParam(name = "cuenta") String cuenta) {
        List<Movimiento> lista;
        try {
            EurekaService service = new EurekaService();
            lista = service.leerMovimientos(cuenta);
        } catch (Exception e) {
            lista = new ArrayList<>();
        }
        return lista;
    }

    /**
     * Consulta el saldo de una cuenta específica
     * Operación requerida por BPMN para validación antes de retiro/transferencia
     * @param cuenta Número de cuenta
     * @return Saldo actual de la cuenta, -1 si hay error
     */
    @WebMethod(operationName = "consultarSaldo")
    @WebResult(name = "saldo")
    public double consultarSaldo(@WebParam(name = "cuenta") String cuenta) {
        try {
            EurekaService service = new EurekaService();
            return service.obtenerSaldo(cuenta);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Valida si una cuenta existe y está activa
     * Operación requerida por BPMN para validación de datos de entrada
     * @param cuenta Número de cuenta a validar
     * @return true si la cuenta es válida, false en caso contrario
     */
    @WebMethod(operationName = "validarCuenta")
    @WebResult(name = "valida")
    public boolean validarCuenta(@WebParam(name = "cuenta") String cuenta) {
        try {
            EurekaService service = new EurekaService();
            return service.validarCuentaActiva(cuenta);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Realiza un depósito en una cuenta
     * Operación requerida por BPMN: realizarDeposito
     * @param cuenta Número de cuenta destino
     * @param monto Monto a depositar (debe ser > 0)
     * @return Estado: 1 = éxito, -1 = error
     */
    @WebMethod(operationName = "realizarDeposito")
    @WebResult(name = "estado")
    public int realizarDeposito(@WebParam(name = "cuenta") String cuenta, @WebParam(name = "monto") double monto) {
        int estado;
        String codEmp = "0001";
        try {
            // Validación de regla de negocio: monto > 0
            if (monto <= 0) {
                return -1;
            }
            EurekaService service = new EurekaService();
            service.registrarDeposito(cuenta, monto, codEmp);
            estado = 1;
        } catch (Exception e) {
            estado = -1;
        }
        return estado;
    }

    /**
     * Realiza un retiro de una cuenta
     * Operación requerida por BPMN: realizarRetiro
     * @param cuenta Número de cuenta origen
     * @param monto Monto a retirar (debe ser > 0 y <= saldo disponible)
     * @return Estado: 1 = éxito, -1 = error, -2 = saldo insuficiente
     */
    @WebMethod(operationName = "realizarRetiro")
    @WebResult(name = "estado")
    public int realizarRetiro(@WebParam(name = "cuenta") String cuenta, @WebParam(name = "monto") double monto) {
        int estado;
        String codEmp = "0001";
        try {
            // Validación de regla de negocio: monto > 0
            if (monto <= 0) {
                return -1;
            }
            EurekaService service = new EurekaService();
            // Validar saldo suficiente antes de retiro
            double saldoActual = service.obtenerSaldo(cuenta);
            if (saldoActual < monto) {
                return -2; // Saldo insuficiente
            }
            service.registrarRetiro(cuenta, monto, codEmp);
            estado = 1;
        } catch (Exception e) {
            estado = -1;
        }
        return estado;
    }

    /**
     * Realiza una transferencia entre cuentas
     * Operación requerida por BPMN: realizarTransferencia
     * @param cuentaOrigen Cuenta de origen
     * @param cuentaDestino Cuenta de destino
     * @param monto Monto a transferir (debe ser > 0)
     * @return Estado: 1 = éxito, -1 = error, -2 = saldo insuficiente, -3 = cuentas iguales
     */
    @WebMethod(operationName = "realizarTransferencia")
    @WebResult(name = "estado")
    public int realizarTransferencia(
        @WebParam(name = "cuentaOrigen") String cuentaOrigen,
        @WebParam(name = "cuentaDestino") String cuentaDestino,
        @WebParam(name = "monto") double monto) {
        int estado;
        String codEmp = "0001";
        try {
            // Validación de regla de negocio: monto > 0
            if (monto <= 0) {
                return -1;
            }
            // Validación: cuentas distintas
            if (cuentaOrigen.equals(cuentaDestino)) {
                return -3; // Cuentas iguales
            }
            EurekaService service = new EurekaService();
            // Validar saldo suficiente en cuenta origen
            double saldoOrigen = service.obtenerSaldo(cuentaOrigen);
            if (saldoOrigen < monto) {
                return -2; // Saldo insuficiente
            }
            service.registrarTransferencia(cuentaOrigen, cuentaDestino, monto, codEmp);
            estado = 1;
        } catch (Exception e) {
            estado = -1;
        }
        return estado;
    }
    
    
    @WebMethod(operationName = "login")
    public boolean login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        EurekaService service = new EurekaService();
        return service.login(username, password);
    }
    
    /**
     * Web service operation
     * @return Retorna la lista de todas las cuentas activas con sus balances
     */
    @WebMethod(operationName = "traerBalances")
    @WebResult(name = "cuenta")
    public List<Cuenta> traerBalances() {
        List<Cuenta> lista;
        try {
            EurekaService service = new EurekaService();
            lista = service.leerBalances();
        } catch (Exception e) {
            lista = new ArrayList<>();
        }
        return lista;
    }
}
