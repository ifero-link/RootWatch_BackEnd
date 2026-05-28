
package com.ifero.RootWPSP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ifero.RootWPSP.model.Medicion;
import java.util.List;

/**
 * Repositorio para la entidad Medicion.
 * Hereda de MongoRepository para obtener operaciones CRUD básicas.
 */
public interface MedicionRepository extends MongoRepository<Medicion, String> {

    /**
     * Consulta derivada de nombre: Busca el primer registro (Top),
     * ordenando por el campo 'fecha' de forma descendente (Desc).
     * * @return El objeto Medicion más reciente en la base de datos.
     */
    Medicion findTopByOrderByFechaDesc();
    
    /**
     * Consulta que devuelve los ultima 20 consuktas
     * @return La Lista con los Objetos Medicion de las últimas 20 consultas
     */
    List<Medicion> findTop20ByOrderByFechaDesc();
}

