package bean;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import util.Log;
import exception.IntegradorException;

public class Configuracao {

    private String host;

    private String porta;

    private String tipoBD;

    private String dataBase;

    private String nomeTabelaClienteJuridico;

    private String nomeTabelaCaso;

    private String login;

    private String senha;

    private String area;

    private String loginMcFile;

    private String senhaMcFile;

    private String urlMcFile;

    private String tipoModelagem;

    private String tipoIntegracao;

    private String cliente;

    private Configuracao() throws Exception {

        try {
            ResourceBundle bundle = null;
            try {
                bundle = ResourceBundle.getBundle("integrador");
            } catch (MissingResourceException e) {
                try {
                    bundle = ResourceBundle.getBundle("util/integrador");
                } catch (MissingResourceException e1) {
                    throw new IntegradorException("Arquivo de propriedades integrador não encontrado.\n"
                            + e.getMessage(), e1);
                }
            }

            host = bundle.getString("host");
            porta = bundle.getString("porta");
            tipoBD = bundle.getString("tipoBD");
            dataBase = bundle.getString("dataBase");
            nomeTabelaClienteJuridico = bundle.getString("nomeTabelaClienteJuridico");
            nomeTabelaCaso = bundle.getString("nomeTabelaCaso");
            login = bundle.getString("login");
            senha = bundle.getString("senha");
            area = bundle.getString("area");
            loginMcFile = bundle.getString("loginMcFile");
            senhaMcFile = bundle.getString("senhaMcFile");
            urlMcFile = bundle.getString("urlMcFile");

            try {
                tipoModelagem = bundle.getString("tipoModelagem");
            } catch (MissingResourceException e) {
            }

            try {
                tipoIntegracao = bundle.getString("tipoIntegracao");
            } catch (MissingResourceException e) {
            }

            cliente = bundle.getString("cliente");

        } catch (Exception e) {
            Log.error("Erro iniciando properties", e);
            throw e;
        }
    }

    private static Configuracao configuracao;

    public static synchronized Configuracao getInstance() throws Exception {

        if (configuracao == null) {
            configuracao = new Configuracao();
        }

        return configuracao;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public String getPorta() {

        return porta;
    }

    public void setPorta(String porta) {

        this.porta = porta;
    }

    public String getTipoBD() {

        return tipoBD;
    }

    public void setTipoBD(String tipoBD) {

        this.tipoBD = tipoBD;
    }

    public String getDataBase() {

        return dataBase;
    }

    public void setDataBase(String dataBase) {

        this.dataBase = dataBase;
    }

    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {

        this.login = login;
    }

    public String getSenha() {

        return senha;
    }

    public void setSenha(String senha) {

        this.senha = senha;
    }

    public String getArea() {

        return area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public String getLoginMcFile() {

        return loginMcFile;
    }

    public void setLoginMcFile(String loginMcFile) {

        this.loginMcFile = loginMcFile;
    }

    public String getSenhaMcFile() {

        return senhaMcFile;
    }

    public void setSenhaMcFile(String senhaMcFile) {

        this.senhaMcFile = senhaMcFile;
    }

    public String getUrlMcFile() {

        if (urlMcFile == null) {
            return Constantes.URL_MCFILE;
        }
        return urlMcFile;
    }

    public void setUrlMcFile(String urlMcFile) {

        this.urlMcFile = urlMcFile;
    }

    public String getTipoModelagem() {

        return tipoModelagem;
    }

    public void setTipoModelagem(String tipoModelagem) {

        this.tipoModelagem = tipoModelagem;
    }

    public String getTipoIntegracao() {

        return tipoIntegracao;
    }

    public void setTipoIntegracao(String tipoIntegracao) {

        this.tipoIntegracao = tipoIntegracao;
    }

    public String getNomeTabelaClienteJuridico() {

        return nomeTabelaClienteJuridico;
    }

    public void setNomeTabelaClienteJuridico(String nomeTabelaClienteJuridico) {

        this.nomeTabelaClienteJuridico = nomeTabelaClienteJuridico;
    }

    public String getNomeTabelaCaso() {

        return nomeTabelaCaso;
    }

    public void setNomeTabelaCaso(String nomeTabelaCaso) {

        this.nomeTabelaCaso = nomeTabelaCaso;
    }

    public String getCliente() {

        return cliente;
    }

    public void setCliente(String cliente) {

        this.cliente = cliente;
    }

}
