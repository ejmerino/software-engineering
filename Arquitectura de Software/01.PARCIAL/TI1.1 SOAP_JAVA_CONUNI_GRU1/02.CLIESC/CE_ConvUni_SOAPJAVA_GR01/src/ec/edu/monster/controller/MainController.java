package ec.edu.monster.controller;

import ec.edu.monster.view.MainFrame;
import ec.edu.monster.model.ConversorModel;

public class MainController {

    private MainFrame view;
    private ConversorModel model;

    public MainController(MainFrame view) {
        this.view = view;
        this.model = new ConversorModel();

        view.setConvertAction(e -> convertir());
    }

    private void convertir() {
        try {
            String tipo = view.getTipoSeleccionado();
            double valor = view.getValorIngresado();
            double resultado = model.convertir(tipo, valor);
            view.mostrarResultado(resultado);
        } catch(Exception ex) {
            view.mostrarError("Error al conectar con el SOAP o valor inválido");
            ex.printStackTrace();
        }
    }
}
