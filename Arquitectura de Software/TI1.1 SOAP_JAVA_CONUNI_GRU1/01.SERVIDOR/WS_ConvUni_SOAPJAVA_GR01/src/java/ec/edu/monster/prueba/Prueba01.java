/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.prueba;

import ec.edu.monster.service.ConversorUnidadesService;

/**
 *
 * @author ednan
 */
public class Prueba01 {
    public static void main(String[] args){
    //Datos
    int n1= 5000;
    int n2= 4;
    //Proceso
    ConversorUnidadesService service = new ConversorUnidadesService();
    
    int kilometros = service.metros_a_kilometros(n1);
    
    System.out.println("Valor en Metros: "+n1);
    System.out.println(n1+" m son "+kilometros+" km");
    
    
    int metros = service.kilometros_a_metros(n2);
    
    System.out.println("Valor en Kilómetros: "+n2);
    System.out.println(n2+" km son "+metros+" m");
    
    
    }
}
