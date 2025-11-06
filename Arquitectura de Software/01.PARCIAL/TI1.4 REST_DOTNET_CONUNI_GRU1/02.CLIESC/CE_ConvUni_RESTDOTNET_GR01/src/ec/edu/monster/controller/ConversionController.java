package ec.edu.monster.controller;

import ec.edu.monster.model.ConversionModel;
import ec.edu.monster.view.ConversionView;

public class ConversionController {
    private final ConversionView view;
    private final ConversionModel model;

    private final String[] massUnits = {"kg","g","lb","oz"};
    private final String[] lengthUnits = {"m","cm","km","ft"};
    private final String[] tempUnits = {"c","f","k","r"};

    public ConversionController(ConversionView view, ConversionModel model){
        this.view = view;
        this.model = model;

        // Inicializa los combobox "De" y "A"
        resetUnits();

        // Listener de categoría
        view.categoryBox.addActionListener(e -> updateUnits());

        // Listener de botón Convertir
        view.convertButton.addActionListener(e -> convert());
    }

    private void resetUnits() {
        view.fromBox.removeAllItems();
        view.toBox.removeAllItems();
        view.fromBox.addItem("Seleccione una categoría");
        view.toBox.addItem("Seleccione una categoría");
    }

    public void updateUnits() {
        String category = (String)view.categoryBox.getSelectedItem();
        String[] units;

        switch(category){
            case "mass": units = massUnits; break;
            case "length": units = lengthUnits; break;
            case "temperature": units = tempUnits; break;
            default:
                units = null;
        }

        view.fromBox.removeAllItems();
        view.toBox.removeAllItems();

        if(units == null){
            view.fromBox.addItem("Seleccione una categoría");
            view.toBox.addItem("Seleccione una categoría");
            return;
        }

        for(String u : units){
            view.fromBox.addItem(u);
            view.toBox.addItem(u);
        }

        // Fuerza actualización visual
        view.fromBox.revalidate();
        view.fromBox.repaint();
        view.toBox.revalidate();
        view.toBox.repaint();
    }

    private void convert() {
        try {
            String category = (String)view.categoryBox.getSelectedItem();
            String from = (String)view.fromBox.getSelectedItem();
            String to = (String)view.toBox.getSelectedItem();
            String valueStr = view.valueField.getText().trim();

            if(category.equals("Seleccione") || from == null || to == null || valueStr.isEmpty()){
                view.resultLabel.setText("Complete todas las opciones y un valor.");
                return;
            }

            double value = Double.parseDouble(valueStr);

            double result = model.convert(category, from, to, value); // llamar servidor REST
            view.resultLabel.setText(value + " " + from + " = " + result + " " + to);

        } catch(NumberFormatException e){
            view.resultLabel.setText("Ingrese un valor numérico válido.");
        } catch(Exception e){
            view.resultLabel.setText("Error al conectar con el servidor");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
