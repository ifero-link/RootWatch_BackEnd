

package com.ifero.RootWPSP.repository;

import com.ifero.RootWPSP.model.Actividad;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface ActividadRepository extends MongoRepository<Actividad, String> {
    // Busca todas las actividades registradas por un usuario específico
    List<Actividad> findByUsername(String username);
    
    void deleteByUsername(String username);
}