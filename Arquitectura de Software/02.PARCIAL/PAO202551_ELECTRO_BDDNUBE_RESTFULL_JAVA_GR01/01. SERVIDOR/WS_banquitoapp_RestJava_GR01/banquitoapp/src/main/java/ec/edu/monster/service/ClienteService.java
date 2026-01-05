package ec.edu.monster.service;

import ec.edu.monster.model.Cliente;
import ec.edu.monster.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorCedula(String cedula) {
        return clienteRepository.findById(cedula).orElse(null);
    }

    public Cliente guardar(Cliente c) {
        return clienteRepository.save(c);
    }

    public void eliminar(String cedula) {
        clienteRepository.deleteById(cedula);
    }
}