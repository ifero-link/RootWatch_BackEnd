

package com.ifero.RootWPSP.controller;

import com.ifero.RootWPSP.model.Actividad;
import com.ifero.RootWPSP.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {

    @Autowired
    private ActividadRepository actividadRepository;

    // Obtener la agenda del usuario
    @GetMapping("/mis-actividades")
    public ResponseEntity<List<Actividad>> obtenerActividades(@RequestParam String username) {
        return ResponseEntity.ok(actividadRepository.findByUsername(username));
    }

    // Guardar una nueva actividad/recordatorio manual
    @PostMapping("/guardar")
    public ResponseEntity<Actividad> guardarActividad(@RequestBody Actividad actividad) {
        return ResponseEntity.ok(actividadRepository.save(actividad));
    }

    // Eliminar un evento de la agenda
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarActividad(@RequestParam String id) {
        actividadRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    // Modificar/Actualizar una actividad existente siguiendo estándares REST
    @PutMapping("/actualizar")  
    public ResponseEntity<Actividad> actualizarActividad(@RequestBody Actividad actividad) {
        // Verificación de seguridad: si no envían ID, no se puede actualizar
        if (actividad.getId() == null || actividad.getId().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
    
       
        return ResponseEntity.ok(actividadRepository.save(actividad));
}
}