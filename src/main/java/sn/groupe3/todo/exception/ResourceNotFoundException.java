package sn.groupe3.todo.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Constructeur qui permet de passer un message d'erreur personnalisé
    public ResourceNotFoundException(String message) {
        super(message); // Envoie le message à la classe parent RuntimeException
    }
}
