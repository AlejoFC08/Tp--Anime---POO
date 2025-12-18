package exception;

public class PersistenciaException extends RuntimeException { // Unchecked para no ensuciar tanto el código
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}