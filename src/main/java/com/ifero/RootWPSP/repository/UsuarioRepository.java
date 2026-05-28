

package com.ifero.RootWPSP.repository;

import com.ifero.RootWPSP.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Método clave para buscar al usuario durante el login
    Optional<Usuario> findByUsername(String username);
}
