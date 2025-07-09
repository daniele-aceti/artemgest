package artemgest.artemgest.exception;

public class FatturaNotFoundException extends RuntimeException {

    public FatturaNotFoundException(Long id) {
        super("Cliente non trovato con ID: " + id);
    }
}
