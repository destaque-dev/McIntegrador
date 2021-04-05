package bean;

import java.util.Base64;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import exception.IntegradorException;
import util.Log;

public class Configuracao {

	private String host;

	private String porta;

	private String tipoBD;

	private String dataBase;

	private String login;

	private String senha;

	private String loginMcFile;

	private String senhaMcFile;

	private String chaveIntegracao;

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
			login = bundle.getString("login");
			senha = new String(Base64.getDecoder().decode(bundle.getString("senha")));

			if(bundle.containsKey("loginMcFile") && bundle.containsKey("senhaMcFile")){
				loginMcFile = bundle.getString("loginMcFile");
				senhaMcFile = new String(Base64.getDecoder().decode(bundle.getString("senhaMcFile")));
			}

			if(bundle.containsKey("chave")){
				chaveIntegracao = new String(Base64.getDecoder().decode(bundle.getString("chave")));
			}

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

	public String getChaveIntegracao() {
		return chaveIntegracao;
	}

	public void setChaveIntegracao(String chaveIntegracao) {
		this.chaveIntegracao = chaveIntegracao;
	}
}
