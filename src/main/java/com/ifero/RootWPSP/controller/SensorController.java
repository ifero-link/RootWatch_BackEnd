package com.ifero.RootWPSP.controller;

import com.ifero.RootWPSP.model.Sensor;
import com.ifero.RootWPSP.model.Medicion;
import com.ifero.RootWPSP.repository.SensorRepository;
import com.ifero.RootWPSP.repository.MedicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensores")
@CrossOrigin(origins = "*") // Permite la comunicación limpia con React Native
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MedicionRepository medicionRepository; // Inyección de tu repositorio de mediciones

    // Obtener solo sensores activos para el dropdown  en el FrontEnd
    @GetMapping("/activos")
    public ResponseEntity<List<Sensor>> obtenerSensoresActivos() {
        return ResponseEntity.ok(sensorRepository.findByActivoTrue());
    }

    // Crear un nuevo sensor
    @PostMapping("/agregar")
    public ResponseEntity<Sensor> agregarSensor(@RequestBody Sensor sensor) {
        return ResponseEntity.ok(sensorRepository.save(sensor));
    }
    
    // Obtener la lectura REAL de la última medición registrada en MongoDB
    @GetMapping("/actual")
    public ResponseEntity<?> obtenerLecturaActual(@RequestParam String id) {
        String tipoBuscado = "";
        String unidad = "";
        
        // Mapeamos el ID enviado por React Native al campo "tipo" de tu base de datos
        switch (id) {
            case "TEMP_01":
                tipoBuscado = "Temperatura";
                unidad = " °C";
                break;
            case "HUM_SUELO_01":
                tipoBuscado = "Humedad Suelo";
                unidad = " %";
                break;
            case "LUZ_01":
                tipoBuscado = "Luz";
                unidad = " Lux";
                break;
            case "HUM_AMB_01":
                tipoBuscado = "Humedad Ambiental";
                unidad = " %";
                break;
            default:
                tipoBuscado = id; 
                break;
        }

        final String tipoFiltro = tipoBuscado;

        // Comprobamos si el sensor existe y está activo
        Optional<Sensor> sensorOpt = sensorRepository.findByActivoTrue().stream()
                .filter(s -> s.getTipo().equalsIgnoreCase(tipoFiltro))
                .findFirst();

        Map<String, Object> respuesta = new HashMap<>();

        if (sensorOpt.isPresent()) {
            Sensor sensor = sensorOpt.get();
            
            // Extraemos la última medición de la colección de MongoDB
            Medicion ultimaMedicion = medicionRepository.findTopByOrderByFechaDesc();
            
            Object valorReal = "---";
            String estadoReal = "Sin datos";

            if (ultimaMedicion != null) {
                estadoReal = "Óptimo";
                
                // Mapeo riguroso según los atributos reales de tu modelo Medicion
                switch (id) {
                    case "TEMP_01":
                        valorReal = ultimaMedicion.getTemperatura(); // double temperatura
                        break;
                    case "HUM_SUELO_01":
                        valorReal = ultimaMedicion.getHumedadSuelo(); // double humedadSuelo
                        break;
                    case "LUZ_01":
                        valorReal = ultimaMedicion.getLuz(); // double luz
                        break;
                    case "HUM_AMB_01":
                        valorReal = ultimaMedicion.getHumedad(); // double humedad (ambiental)
                        break;
                    default:
                        valorReal = "N/A";
                        break;
                }
            }

            respuesta.put("valor", valorReal + unidad);
            respuesta.put("estado", sensor.isActivo() ? estadoReal : "Inactivo");
            
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("valor", "N/A");
            respuesta.put("estado", "No configurado");
            return ResponseEntity.ok(respuesta);
        }
    }
}