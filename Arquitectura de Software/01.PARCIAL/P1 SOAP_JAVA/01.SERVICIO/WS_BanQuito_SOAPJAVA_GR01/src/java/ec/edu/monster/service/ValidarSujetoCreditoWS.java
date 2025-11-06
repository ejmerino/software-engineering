package ec.edu.monster.service;

import ec.edu.monster.controller.CreditoController;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName = "ValidarSujetoCreditoWS")
public class ValidarSujetoCreditoWS {

    @WebMethod(operationName = "esSujetoCredito")
    public boolean esSujetoCredito(String cedula) {
        CreditoController controller = new CreditoController();
        return controller.esSujetoCredito(cedula);
    }
}
