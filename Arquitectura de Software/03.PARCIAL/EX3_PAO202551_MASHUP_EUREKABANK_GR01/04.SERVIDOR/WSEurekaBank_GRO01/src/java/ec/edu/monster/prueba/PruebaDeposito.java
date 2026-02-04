/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.prueba;

/**
 *
 * @author diego
 */
import ec.edu.monster.servicio.EurekaService;

public class PruebaDeposito {

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            //datos
            String cuenta = "00100001";
            double importe = 200;
            String codEmp = "0001";
            //proceso
            EurekaService service = new EurekaService();
            service.registrarDeposito(cuenta, importe, codEmp);
            System.out.println("Proceso ok");
        } catch (Exception e) {
            e.printStackTrace();     }   }}
