
package com.ifero.RootWPSP.controller;

import com.ifero.RootWPSP.model.Usuario;
import com.ifero.RootWPSP.model.Planta;
import com.ifero.RootWPSP.repository.UsuarioRepository;
import com.ifero.RootWPSP.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;     
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.ifero.RootWPSP.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")

public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleAuthService googleAuthService; 

    // añadimos al constructor para que Spring lo inyecte automáticamente
    public UsuarioController(UsuarioRepository repository, PasswordEncoder passwordEncoder, GoogleAuthService googleAuthService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/registro")
    public String registrar(@RequestBody Usuario nuevoUsuario) {
        if (repository.findByUsername(nuevoUsuario.getUsername()).isPresent()) {
            return "Error: El usuario ya existe";
        }
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        repository.save(nuevoUsuario);
        return "Usuario registrado con éxito";
    }

    // Endpoint para google
    @PostMapping("/login-google")
    public ResponseEntity<?> loginConGoogle(@RequestBody Map<String, String> request) {
        String tokenId = request.get("idToken");
        
        if (tokenId == null || tokenId.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El token de Google es requerido");
        }

        // Validamos el token llamando a nuestro servicio 
        GoogleIdToken.Payload payload = googleAuthService.verificarToken(tokenId);
        
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Token de Google no válido o expirado");
        }

        // Extraemos la información del usuario firmada por Google
        String email = payload.getEmail();
        String nombre = (String) payload.get("name"); // Nombre completo del usuario en su cuenta

        // Buscamos si ya existe en nuestra colección de MongoDB usando el email como username
        java.util.Optional<Usuario> usuarioExistente = repository.findByUsername(email);
        Usuario usuarioFinal;

        if (usuarioExistente.isPresent()) {
            //Ya se había registrado antes con Google. Hacemos Login.
            usuarioFinal = usuarioExistente.get();
            System.out.println("Usuario existente logueado vía Google: " + email);
        } else {
            //Es la primera vez que entra en la app con este Gmail. Registro Automático.
            usuarioFinal = new Usuario();
            usuarioFinal.setUsername(email); // Usamos el correo como el identificador de usuario
            usuarioFinal.setNombre(nombre);
            // Le asignamos una contraseña aleatoria e inexpugnable ya que la seguridad la gestiona Google
            String contrasenaAleatoria = UUID.randomUUID().toString();
            usuarioFinal.setPassword(passwordEncoder.encode(contrasenaAleatoria));
            
            // Guardamos el nuevo usuario en MongoDB
            repository.save(usuarioFinal);
            System.out.println("Nuevo usuario registrado automáticamente vía Google: " + email);
        }

        // Devolvemos al móvil una respuesta exitosa con los datos del usuario para que sepa quién está logueado
        return ResponseEntity.ok(Map.of(
            "mensaje", "Autenticación exitosa con Google",
            "username", usuarioFinal.getUsername(),
            "nombre", usuarioFinal.getNombre()
        ));
    }
    
    // Endpoint para eliminar la cuenta
    @Autowired
    private ActividadRepository actividadRepository; 

    // Endpoint para eliminar la cuenta modificado con borrado en cascada
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarCuenta(@RequestParam String username) {
        java.util.Optional<Usuario> usuario = repository.findByUsername(username);
    
        if (usuario.isPresent()) {
            //  Borramos todas las actividades vinculadas al email del usuario primero
            try {
                actividadRepository.deleteByUsername(username);
                System.out.println("Actividades en cascada eliminadas para: " + username);
            } catch (Exception e) {
                System.out.println("Advertencia/Error al borrar actividades en cascada: " + e.getMessage());
            }

            // Procedemos a borrar el usuario principal
            repository.delete(usuario.get());
            System.out.println("Usuario eliminado de MongoDB: " + username);

            return ResponseEntity.ok(Map.of("mensaje", "Cuenta y datos asociados eliminados con éxito"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: El usuario no existe");
        }
    }
    
    // Endpoint para actualizar el nombre
    @PutMapping("/actualizar-nombre")
    public ResponseEntity<?> actualizarNombre(@RequestParam String username, @RequestParam String nuevoNombre) {
    java.util.Optional<Usuario> usuarioOpt = repository.findByUsername(username);
    
    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();
        usuario.setNombre(nuevoNombre); 
        repository.save(usuario);
        System.out.println("Nombre actualizado en MongoDB para " + username + " -> " + nuevoNombre);
        return ResponseEntity.ok(Map.of("mensaje", "Nombre de usuario actualizado con éxito", "nombre", nuevoNombre));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: El usuario no existe");
        }
    }
    
    // Agregar nueva planta con todos los campos
    @PutMapping("/agregar-planta")
    public ResponseEntity<?> agregarPlanta(@RequestParam String username, @RequestBody Planta nuevaPlanta) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Asignamos un ID único a la planta antes de guardarla
        nuevaPlanta.setId(UUID.randomUUID().toString());
        
        usuario.getPlantas().add(nuevaPlanta);
        repository.save(usuario);
        
        return ResponseEntity.ok(nuevaPlanta); // Devolvemos la planta con su nuevo ID
    }

    // Obtener todas las plantas de un usuario
    @GetMapping("/mis-plantas")
    public ResponseEntity<List<Planta>> obtenerPlantas(@RequestParam String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return ResponseEntity.ok(usuario.getPlantas());
    }
    
    // Eliminar planta
    @DeleteMapping("/eliminar-planta")
    public ResponseEntity<?> eliminarPlanta(@RequestParam String username, @RequestParam String plantaId) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Eliminamos la planta por ID
        boolean eliminada = usuario.getPlantas().removeIf(p -> p.getId().equals(plantaId));
        
        if (!eliminada) {
            return ResponseEntity.status(404).body("Planta no encontrada");
        }
        
        repository.save(usuario);
        return ResponseEntity.ok(usuario.getPlantas()); // Devolvemos la lista actualizada
    }
    // editar plantas
    @PutMapping("/editar-planta")
    public ResponseEntity<?> editarPlanta(@RequestParam String username, @RequestBody Planta plantaEditada) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscamos la planta en la lista y la reemplazamos
        List<Planta> lista = usuario.getPlantas();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(plantaEditada.getId())) {
                lista.set(i, plantaEditada);
                break;
            }
        }

        repository.save(usuario);
    return ResponseEntity.ok(plantaEditada);
    }
    
    
  
}