package com.ifero.RootWPSP.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${rootwatch.api.key}")
    private String apiKey;

    @Value("${rootwatch.api.header}")
    private String headerName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Aquí usamos las variables 'headerName' y 'apiKey' que inyectamos arriba
        if (request.getRequestURI().equals("/api/mediciones")
                && request.getMethod().equals("POST")) {

            String requestApiKey = request.getHeader(headerName); 

            if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}