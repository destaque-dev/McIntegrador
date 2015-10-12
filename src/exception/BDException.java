package exception;

public class BDException extends Exception {

    private static final long serialVersionUID = 7401993256644989333L;

    public BDException(String message) {

        super("Problemas na conexao com o banco de dados.\n" + message);
    }

    /**
     * booleano criado para não colocar mensagem padrão de problemas na conexão.
     * 
     * @param message
     * @param tarefaException
     */
    public BDException(String message, boolean tarefaException) {

        super(message);

    }

    public BDException(String sql, String message) {

        super("Problemas com a sql:\n" + sql + "\n" + message);
    }

    public BDException(String message, Throwable e) {

        super(message, e);
    }

}
