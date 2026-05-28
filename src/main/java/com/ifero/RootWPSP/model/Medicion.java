
package com.ifero.RootWPSP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Clase que representa la entidad de una medición ambiental.
 * Se almacena en la colección "mediciones" de MongoDB.
 */
@Document(collection = "mediciones")
public class Medicion {

    @Id
    private String id;

    private double temperatura;
    private double humedad;       // Representa la humedad ambiental
    private double humedadSuelo;  
    private double luz;           
    private LocalDateTime fecha;

    public Medicion() {
        this.fecha = LocalDateTime.now();
    }
    
    public double getHumedadSuelo() {
        return humedadSuelo;
    }

    public void setHumedadSuelo(double humedadSuelo) {
        this.humedadSuelo = humedadSuelo;
    }

    public double getLuz() {
        return luz;
    }

    public void setLuz(double luz) {
        this.luz = luz;
    }

    public String getId() { 
        return id; 
    }
    
    public double getTemperatura() { 
        return temperatura;
    }
    
    public void setTemperatura(double temperatura) { 
        this.temperatura = temperatura; 
    }
    
    public double getHumedad() { 
        return humedad;
    }
    
    public void setHumedad(double humedad) {
        this.humedad = humedad; 
    }
    
    public LocalDateTime getFecha() { 
        return fecha;
    }
}