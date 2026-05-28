
package com.ifero.RootWPSP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;


@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String username;  // Este almacena el correo de Gmail
    private String password;  // El hash de BCrypt
    private String nombre;    
    
    private List<Planta> plantas = new ArrayList<>();

    // Constructor vacío obligatorio para Spring Data / MongoDB
    public Usuario() {}

    // Constructor con parámetros
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
        this.nombre = username.split("@")[0]; // Por defecto le asigna la primera parte de su email
    }


    public String getId() { 
        return id; 
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

   
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Planta> getPlantas() { 
        return plantas;
    }
    
    public void setPlantas(List<Planta> plantas) { 
        this.plantas = plantas; 
    }
}