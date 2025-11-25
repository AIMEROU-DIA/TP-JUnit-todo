package sn.groupe3.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice   // Permet de gérer globalement les exceptions dans toute l'application
public class GlobalExceptionHandler {

    // Gère l'exception ResourceNotFoundException quand elle est lancée
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {

        // Retourne une réponse HTTP 404 avec le message de l'erreur
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
