package ru.m4oma.exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException ex) {
            log.warn("Access not allowed: {}", ex.getMessage());
            declareError(response, HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("Illegal argument: {}", ex.getMessage());
            declareError(response, HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (EntityNotFoundException ex) {
            log.warn("Entity wasn't found: {}", ex.getMessage());
            declareError(response, HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (ResponseStatusException ex) {
            log.warn("Response exception: {}", ex.getMessage());
            declareError(response, HttpStatus.FORBIDDEN, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            declareError(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected exception: " + ex.getMessage());
        }
    }

    private void declareError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), error);
    }
}
