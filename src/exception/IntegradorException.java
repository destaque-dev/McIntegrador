package exception;

public class IntegradorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IntegradorException(String message) {

        super("Problemas na conexao com o banco de dados.\n" + message);
    }

    public IntegradorException(String message, Throwable e) {

        super(message, e);
    }
}
