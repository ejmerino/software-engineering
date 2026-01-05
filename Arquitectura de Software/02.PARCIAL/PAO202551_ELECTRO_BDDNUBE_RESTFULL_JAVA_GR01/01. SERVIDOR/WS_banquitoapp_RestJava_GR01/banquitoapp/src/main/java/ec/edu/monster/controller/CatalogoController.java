package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.repository.ElectrodomesticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class CatalogoController {

    @Autowired
    private ElectrodomesticoRepository electrodomesticoRepo;

    @GetMapping
    public List<Electrodomestico> listarTodos() {
        return electrodomesticoRepo.findAll();
    }

    @PostMapping
    public Electrodomestico crearProducto(@RequestBody Electrodomestico e) {
        return electrodomesticoRepo.save(e);
    }

    @PutMapping("/{id}")
    public Electrodomestico actualizarProducto(@PathVariable Integer id, @RequestBody Electrodomestico e) {
        e.setIdElectrodomestico(id);
        return electrodomesticoRepo.save(e);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Integer id) {
        electrodomesticoRepo.deleteById(id);
    }
}