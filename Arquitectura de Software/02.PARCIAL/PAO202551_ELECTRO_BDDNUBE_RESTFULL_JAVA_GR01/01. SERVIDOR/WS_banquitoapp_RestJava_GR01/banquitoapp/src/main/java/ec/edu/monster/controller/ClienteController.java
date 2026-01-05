package ec.edu.monster.controller;

import ec.edu.monster.model.Cliente;
import ec.edu.monster.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") // Permite que cualquier frontend (Angular, React, etc.) se conecte
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listar();
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<Cliente> buscarPorCedula(@PathVariable String cedula) {
        Cliente c = clienteService.buscarPorCedula(cedula);
        if (c != null) {
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Cliente guardar(@RequestBody Cliente cliente) {
        return clienteService.guardar(cliente);
    }

    @DeleteMapping("/{cedula}")
    public void eliminar(@PathVariable String cedula) {
        clienteService.eliminar(cedula);
    }
}