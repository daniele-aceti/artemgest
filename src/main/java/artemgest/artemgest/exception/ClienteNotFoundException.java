package artemgest.artemgest.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("Cliente non trovato con ID: " + id);
    }
}

