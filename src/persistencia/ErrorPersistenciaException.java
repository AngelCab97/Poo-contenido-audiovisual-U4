package persistencia;

public class ErrorPersistenciaException extends Exception {

    public ErrorPersistenciaException(String mensaje) {
        super(mensaje);
    }

    public ErrorPersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}