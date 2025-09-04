package ru.m4oma.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GlobalExceptionFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            handleException((HttpServletResponse) response, ex);
        }
    }

    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        HttpStatus status;
        String message;

        switch (ex) {
            case AuthenticationException authenticationException -> {
                status = HttpStatus.UNAUTHORIZED;
                message = "Authentication failed: " + ex.getMessage();
            }
            case AccessDeniedException accessDeniedException -> {
                status = HttpStatus.FORBIDDEN;
                message = "Access denied: " + ex.getMessage();
            }
            case EntityNotFoundException entityNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                message = ex.getMessage();
            }
            case IllegalArgumentException illegalArgumentException -> {
                status = HttpStatus.BAD_REQUEST;
                message = ex.getMessage();
            }
            case null, default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "Internal error: " + ex.getMessage();
            }
        }

        log.warn("Global exception: {} - {}", status, message, ex);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());

        objectMapper.writeValue(response.getWriter(), error);
    }
}
