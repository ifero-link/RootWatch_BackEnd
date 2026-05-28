/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ifero.RootWPSP;


import com.ifero.RootWPSP.model.Usuario;
import com.ifero.RootWPSP.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Si no hay usuarios en la base de datos, creamos uno por defecto
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Encriptamos la contraseña "1234" antes de guardarla
            admin.setPassword(passwordEncoder.encode("1234"));
            
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario inicial creado: admin / 1234");
        }
    }
}

