
package com.ifero.RootWPSP.repository;

import com.ifero.RootWPSP.model.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SensorRepository extends MongoRepository<Sensor, String> {
    List<Sensor> findByActivoTrue(); // Método personalizado para traer solo activos
}