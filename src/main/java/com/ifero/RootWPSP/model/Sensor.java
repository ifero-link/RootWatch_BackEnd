

package com.ifero.RootWPSP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensores")
public class Sensor {
    @Id
    private String id;
    private String nombre; 
    private String tipo;   
    private boolean activo; 

    public Sensor() {}

    public Sensor(String nombre, String tipo, boolean activo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}