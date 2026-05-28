
package com.ifero.RootWPSP.model;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;


@Data
public class Planta {
    private String id; // ID único para la planta
    private String nombrePlanta;
    private List<String> sensoresAsignados = new ArrayList<>();
    private String tipo;    
    private String fechaPlantacion; 
    private String tipoRiego;
    
    // Lista para futuras tareas: riego, fertilización, podas.
    private List<EventoCultivo> historial = new ArrayList<>();
    
    public class EventoCultivo {
        private String tipo;     
        private String fecha;
        private String notas;
    }
    
    
}
    
