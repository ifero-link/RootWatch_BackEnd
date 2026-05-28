
package com.ifero.RootWPSP.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ifero.RootWPSP.model.Medicion;
import com.ifero.RootWPSP.repository.MedicionRepository;


@RestController
@RequestMapping("/api/mediciones") // Ruta base: http://localhost:8080/api/mediciones
@CrossOrigin(origins = "*") // Permite que React Native lea los datos
public class MedicionController {

    private final MedicionRepository repository;

    /**
     * Constructor para la inyección del repositorio.
     *
     * @param repository Repositorio de MongoDB.
     */
    public MedicionController(MedicionRepository repository) {
        this.repository = repository;
    }

    /**
     * Guarda una nueva medición enviada por el Arduino .
     *
     * @param medicion Objeto mapeado desde el JSON recibido.
     * @return El objeto guardado.
     */
    @PostMapping
    public Medicion guardar(@RequestBody Medicion medicion) {
        // Guarda en la base de datos y devuelve el resultado
        return repository.save(medicion);
    }

    /**
     * Obtiene el historial completo de todas las mediciones.
     *
     * @return Lista de mediciones (utilizada para el gráfico).
     */
    /*@GetMapping("/todas")
    public List<Medicion> listar() {
        return repository.findAll();
    }*/

    @GetMapping("/all")
    public List<Medicion> listar() {
        // Traemos solo las últimas 20 para que el gráfico sea legible
        //return repository.findTop20ByOrderByFechaDesc();
        return repository.findAll();
    }


    @GetMapping("/ultima")
    public Medicion ultima() {
        // Llama al método personalizado del repositorio
        return repository.findTopByOrderByFechaDesc();
    }
}
