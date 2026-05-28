package com.ifero.RootWPSP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "actividades")
public class Actividad {

    @Id
    private String id;
    private String username;      
    private String idPlanta;       
    private String nombrePlanta;   
    private LocalDate fecha;       
    private String tipoActividad; 
    private String notas;          
    private boolean recordatorio;  
    private boolean completado;    

    // Constructor por defecto
    public Actividad() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getIdPlanta() { return idPlanta; }
    public void setIdPlanta(String idPlanta) { this.idPlanta = idPlanta; }

    public String getNombrePlanta() { return nombrePlanta; }
    public void setNombrePlanta(String nombrePlanta) { this.nombrePlanta = nombrePlanta; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTipoActividad() { return tipoActividad; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }

    public String getNotas() { return notas; }
    public void setNotas(String notes) { this.notas = notes; }

    public boolean isRecordatorio() { return recordatorio; }
    public void setRecordatorio(boolean recordatorio) { this.recordatorio = recordatorio; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}

