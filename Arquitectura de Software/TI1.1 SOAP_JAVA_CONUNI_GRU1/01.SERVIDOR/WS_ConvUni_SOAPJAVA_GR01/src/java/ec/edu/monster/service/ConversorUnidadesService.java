/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.service;

/**
 *
 * @author ednan
 */
public class ConversorUnidadesService {
    public int metros_a_kilometros(int n1){
        return n1/1000;
    }
    
    public int kilometros_a_metros(int n2){
        return n2*1000;
    }
}
