
package com.ifero.RootWPSP.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class GoogleAuthService {
@Value("${google.client.id}")
    private String googleClientId;

    public GoogleIdToken.Payload verificarToken(String tokenId) {
        try {
            // Configuramos el verificador oficial de Google con nuestro Client ID
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), 
                    new GsonFactory()
            )
            .setAudience(Collections.singletonList(googleClientId))
            .build();

            // Validamos el token que nos manda el móvil
            GoogleIdToken idToken = verifier.verify(tokenId);
            
            if (idToken != null) {
                // Si es válido, devolvemos toda la info del usuario 
                return idToken.getPayload();
            } else {
                System.out.println("Token de Google inválido.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el token con Google: " + e.getMessage());
            return null;
        }
    }
}
