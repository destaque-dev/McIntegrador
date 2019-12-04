package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import bean.Configuracao;
import exception.BDException;
import mcfile.crypt.model.Crypt;

public class ConexaoIntegrador {

    private Connection conexao = null;

    public ConexaoIntegrador() throws Exception {

        super();

        String tipoBD = Configuracao.getInstance().getTipoBD();
        int intTipoBD = TipoBD.validacao(tipoBD);

        try {

            Class.forName(getDriver(intTipoBD));
            String url = getUrl(intTipoBD, Configuracao.getInstance().getHost(), Configuracao.getInstance().getPorta(),
                    Configuracao.getInstance().getDataBase());

            this.conexao = DriverManager.getConnection(url, Configuracao.getInstance().getLogin(), Crypt.decryptCipher(Configuracao.getInstance().getSenha()));

        } catch (Exception e) {
            throw new BDException("Erro iniciando conexão", e);
        }

        validaConexao(intTipoBD);
    }

    public ConexaoIntegrador(Connection conexao, int tipoConexao) throws BDException {

        this.conexao = conexao;
        validaConexao(tipoConexao);

    }

    public String getDriver(int tipo) {

        switch (tipo) {
            case TipoBD.SQLSERVER:
                return "net.sourceforge.jtds.jdbc.Driver";
            case TipoBD.ORACLE:
            case TipoBD.ORACLE_7:
            case TipoBD.ORACLE_ODA:
            case TipoBD.ORACLE_WEB:
                return "oracle.jdbc.driver.OracleDriver";
            case TipoBD.POSTGRE:
                return "org.postgresql.Driver";
            default:
                return "";
        }
    }

    public String getUrl(int tipo, String host, String port, String dataBaseName) {

    	String url = "";
    	Log.fatal("TipoBD: " + tipo);
        switch (tipo) {
            case TipoBD.SQLSERVER:
                // Porta default = 1433
                // return "jdbc:microsoft:sqlserver://" + host + ":" + port + ";DatabaseName=" +
                // dataBaseName;
            	url = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + dataBaseName;
            	break;
            case TipoBD.ORACLE_7:
                // Neste caso, o HOST é o nome para acessar o banco do TNSNAMES.ORA
            	url = "jdbc:oracle:" + host + ":@" + dataBaseName;
            	break;
            case TipoBD.ORACLE:
            case TipoBD.ORACLE_ODA:
                // Porta default = 1521
            	url = "jdbc:oracle:thin:@" + host + ":" + port + "/" + dataBaseName;
            	break;
            case TipoBD.ORACLE_WEB:
            	// Porta default = 1521
            	url = "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = " + host + ")(PORT = " + port + "))"
        				+ " (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = " + dataBaseName + ") (UR = A) (UR = A)"
        				+ " (FAILOVER_MODE = (TYPE = select) (METHOD = basic))))";
            	break;
            case TipoBD.POSTGRE:
                // Porta default = 5432
            	url = "jdbc:postgresql://" + host + ":" + port + "/" + dataBaseName;
            	break;
            default:
            	url = "";
            	break;
        }
        
        Log.info("url: " + url);
        return url;
    }

    public Connection getConexao() {

        return conexao;
    }

    private void validaConexao(int tipoBD) throws BDException {

        try {
            String query = null;

            switch (tipoBD) {
                case TipoBD.SQLSERVER:
                    query = "SELECT 1";
                    break;
                case TipoBD.ORACLE:
                case TipoBD.ORACLE_7:
                case TipoBD.ORACLE_ODA:
                case TipoBD.ORACLE_WEB:
                    query = "SELECT 1 FROM DUAL";
                    break;
                case TipoBD.POSTGRE:
                    query = "SELECT 1";
                    break;
            }
            PreparedStatement st = conexao.prepareStatement(query);
            st.executeQuery();
        } catch (SQLException e) {
            throw new BDException(
                    "A conexão como banco de dados foi estabelecida, mas a estrutura do banco está inválida pois não foi possível nem mesmo encontrar a tabela de validação.",
                    e);
        }
    }

    public void close() {

        try {
            if (conexao != null && !conexao.isClosed()) {
                try {
                    conexao.rollback();
                } catch (Exception e) {
                    Log.error("Não deu o rollback ao liberar a conexao", e);
                }
                conexao.close();
            }
        } catch (SQLException e) {
            Log.error("Erro em close de conexão", e);
        }

    }

    public ResultBD executeQuery(String sql, Object... parametros) throws BDException {

        PreparedStatement st = null;
        try {
            st = conexao.prepareStatement(sql);
            for (int i = 0; i < parametros.length; i++) {
                st.setObject(i + 1, parametros[i]);
            }
            return new ResultBD(st.executeQuery());
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao executar :\n" + sql + "\n com parametros "
                    + parametros, e);
        }
    }

    /**
     * Executa a query de update utilizando o PreparedStatement. Neste caso a query será escrita
     * utilizando a notação de parâmetros, e seus parametros deverão ser passados na ordem de
     * preenchimento na query. Ex: sql = 'update TABELA set CAMPO1 = ? where CAMPO2 = ?';
     * executeQuery(sql, VALOR_CAMPO1, VALOR_CAMPO2);
     *
     * @param sql
     *            Query de insert a ser executada
     * @param parametros
     *            Parametros na ordem em que devem ser preenchidos na query
     * @throws BDException
     */
    public int executeUpdate(String sql, Object... parametros) throws BDException {

        PreparedStatement st = null;
        try {
            st = conexao.prepareStatement(sql);
            for (int i = 0; i < parametros.length; i++) {
                st.setObject(i + 1, parametros[i]);
            }
            return st.executeUpdate();
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao executar :\n" + sql + "\n com parametros "
                    + parametros, e);
        } finally {
            closeStatement(st);
        }
    }

    public void closeStatement(Statement st) throws BDException {

        try {
            if (st != null) {
                st.clearWarnings();
                st.clearBatch();
                st.close();
            }
        } catch (SQLException e) {
            Log.error("Falha ao fechar Statement.", e);
        }
    }

    public void closeResultBD(ResultBD res) {

        if (res != null) {
            res.close();
        }
    }

    public boolean isTransacaoAberta() throws BDException {

        try {
            return !conexao.getAutoCommit();
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao verificar transação .", e);
        }
    }

    public void iniciarTransacao() throws BDException {

        try {
            conexao.setAutoCommit(false);
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao iniciar transação .", e);
        }
    }

    public void rollback() throws BDException {

        try {
            if (!conexao.getAutoCommit()) {
                conexao.rollback();
                conexao.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao realizar rollback .", e);
        }
    }

    public void commit() throws BDException {

        try {
            if (!conexao.getAutoCommit()) {
                conexao.commit();
                conexao.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new BDException("Erro (" + e.getMessage() + ") ao realizar commit .", e);
        }
    }
}
