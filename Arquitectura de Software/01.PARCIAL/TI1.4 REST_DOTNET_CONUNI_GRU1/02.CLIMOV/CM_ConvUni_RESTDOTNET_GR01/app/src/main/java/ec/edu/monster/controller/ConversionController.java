package ec.edu.monster.controller;

import ec.edu.monster.model.ConversionModel;

public class ConversionController {

    public double convert(String category, String from, String to, double value) {
        try {
            // Intentar conversión desde el servidor (modelo)
            return ConversionModel.convert(category, from, to, value);
        } catch (Exception e) {
            System.out.println("⚠️ Error al conectar con el servidor, usando modo local...");
            return localConvert(category, from, to, value);
        }
    }

    private double localConvert(String category, String from, String to, double value) {
        switch (category.toLowerCase()) {
            case "mass":
                return convertMass(from, to, value);
            case "length":
                return convertLength(from, to, value);
            case "temperature":
                return convertTemperature(from, to, value);
            default:
                return 0.0;
        }
    }

    private double convertMass(String from, String to, double value) {
        double kg;
        switch (from) {
            case "kg": kg = value; break;
            case "g": kg = value / 1000; break;
            case "lb": kg = value * 0.453592; break;
            case "oz": kg = value * 0.0283495; break;
            default: return 0;
        }
        switch (to) {
            case "kg": return kg;
            case "g": return kg * 1000;
            case "lb": return kg / 0.453592;
            case "oz": return kg / 0.0283495;
            default: return 0;
        }
    }

    private double convertLength(String from, String to, double value) {
        double m;
        switch (from) {
            case "m": m = value; break;
            case "cm": m = value / 100; break;
            case "km": m = value * 1000; break;
            case "in": m = value * 0.0254; break;
            default: return 0;
        }
        switch (to) {
            case "m": return m;
            case "cm": return m * 100;
            case "km": return m / 1000;
            case "in": return m / 0.0254;
            default: return 0;
        }
    }

    private double convertTemperature(String from, String to, double value) {
        double celsius;
        switch (from) {
            case "C": celsius = value; break;
            case "F": celsius = (value - 32) * 5/9; break;
            case "K": celsius = value - 273.15; break;
            case "R": celsius = (value - 491.67) * 5/9; break;
            default: return 0;
        }
        switch (to) {
            case "C": return celsius;
            case "F": return celsius * 9/5 + 32;
            case "K": return celsius + 273.15;
            case "R": return (celsius + 273.15) * 9/5;
            default: return 0;
        }
    }
}
