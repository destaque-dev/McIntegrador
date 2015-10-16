package integracao;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import bean.Configuracao;
import bean.Constantes;
import bean.TipoStatus;
import bean.padrao.AssuntoBean;
import bean.padrao.ClienteBean;
import exception.BDException;
import util.ConexaoIntegrador;
import util.Funcoes;
import util.Log;
import util.ParametrosIntegracao;

public abstract class IntegradorBase {

	protected Configuracao configuracao;	

	protected ConexaoIntegrador conexao;

	public void inicializa(Configuracao configuracao) throws Exception {
		this.configuracao = configuracao;

		if (isBD()) {
			conectaBancoDados();
			Log.info("Conectou banco de dados");
		}
	}
	
	/**
	 * Inicia conexao ao banco de dados
	 *
	 * @throws Exception
	 */
	public void conectaBancoDados() throws Exception {

		try {
			conexao = new ConexaoIntegrador();
		} catch (Exception e) {
			Log.error("Erro conectando a banco de dados", e);
			throw e;
		}
	}
	
	/**
	 * Verifica novos registros a serem inseridos na base e os insere, caso encontre
	 *
	 * @throws Exception
	 */
	public void verificaRegistrosNovos() throws Exception {

		List<ClienteBean> clientes = getClientes(TipoStatus.ADICIONADO);
		if (clientes.size() > 0) {
			Log.info("Clientes novos encontrados: " + clientes.size());

			for (ClienteBean clienteJuridicoBean : clientes) {
				adicionaCliente(clienteJuridicoBean);
			}
		}

		List<AssuntoBean> assuntos = getAssuntos(TipoStatus.ADICIONADO);
		if (assuntos.size() > 0) {
			Log.info("assuntos novos encontrados: " + assuntos.size());

			for (AssuntoBean assuntoBean : assuntos) {
				adicionaAssunto(assuntoBean);
			}
		}

	}



	/**
	 * Verifica registros a serem inseridos na base e os atualiza, caso encontre
	 *
	 * @throws Exception
	 */
	public void verificaRegistrosAtualizados() throws Exception {

		List<ClienteBean> clientes = getClientes(TipoStatus.MODIFICADO);
		if (clientes.size() > 0) {
			Log.info("Clientes modificados encontrados: " + clientes.size());

			for (ClienteBean clienteJuridicoBean : clientes) {
				atualizaCliente(clienteJuridicoBean);
			}
		}

		List<AssuntoBean> assuntos = getAssuntos(TipoStatus.MODIFICADO);
		if (assuntos.size() > 0) {
			Log.info("assuntos modificados encontrados: " + assuntos.size());

			for (AssuntoBean assuntoBean : assuntos) {
				atualizaAssunto(assuntoBean);
			}
		}

	}

	/**
	 * Verifica registros a serem inseridos na base e os atualiza, caso encontre
	 *
	 * @throws Exception
	 */
	public void verificaRegistrosRemovidos() throws Exception {

		List<ClienteBean> clientes = getClientes(TipoStatus.REMOVIDO);
		if (clientes.size() > 0) {
			Log.info("Clientes removidos encontrados: " + clientes.size());

			for (ClienteBean clienteJuridicoBean : clientes) {
				removeCliente(clienteJuridicoBean);
			}
		}

		List<AssuntoBean> assuntos = getAssuntos(TipoStatus.REMOVIDO);
		if (assuntos.size() > 0) {
			Log.info("assuntos removidos encontrados: " + assuntos.size());

			for (AssuntoBean assuntoBean : assuntos) {
				removeAssunto(assuntoBean);
			}
		}

	}

	/**
	 * Verifica novos registros a serem inseridos na base e os insere, caso encontre
	 *
	 * @throws Exception
	 */
	public void verificaRegistrosEspeciais() throws Exception {

		List<ClienteBean> clientes = getClientes(TipoStatus.ESPECIAL);
		if (clientes.size() > 0) {
			Log.info("Clientes especiais encontrados: " + clientes.size());

			for (ClienteBean clienteJuridicoBean : clientes) {
				trataClienteEspecial(clienteJuridicoBean);
			}
		}

		List<AssuntoBean> assuntos = getAssuntos(TipoStatus.ESPECIAL);
		if (assuntos.size() > 0) {
			Log.info("assuntos especiais encontrados: " + assuntos.size());

			for (AssuntoBean assuntoBean : assuntos) {
				trataAssuntoEspecial(assuntoBean);
			}
		}

	}

	/**
	 * Adiciona assunto encontrado
	 *
	 * @param assuntoBean
	 * @throws Exception
	 */
	protected void adicionaAssunto(AssuntoBean assuntoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				assuntoIntegrado(assuntoBean);
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.ADICIONADO);

				commit();
			} else {
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.ADICIONADO);
				assuntoIntegrado(assuntoBean);
			}

			Log.info("assunto " + assuntoBean.getCodAssunto() + " do cliente " + assuntoBean.getCodCliente()
			+ "  inserido");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}
	}

	/**
	 * Adiciona cliente encontrado
	 *
	 * @param clienteJuridicoBean
	 * @throws Exception
	 */
	protected void adicionaCliente(ClienteBean clienteJuridicoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				clienteIntegrado(clienteJuridicoBean);
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.ADICIONADO);

				commit();
			} else {
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.ADICIONADO);
				clienteIntegrado(clienteJuridicoBean);
			}

			Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " inserido");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}
	}

	/**
	 * Atualiza assunto encontrado
	 *
	 * @param assuntoBean
	 * @throws Exception
	 */
	protected void atualizaAssunto(AssuntoBean assuntoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				assuntoIntegrado(assuntoBean);
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.MODIFICADO);

				commit();
			} else {
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.MODIFICADO);
				assuntoIntegrado(assuntoBean);
			}
			Log.info("assunto " + assuntoBean.getCodAssunto() + " do cliente " + assuntoBean.getCodCliente()
			+ "  atualizado");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}
	}

	/**
	 * Atualiza cliente encontrado
	 *
	 * @param assuntoBean
	 * @throws Exception
	 */
	protected void atualizaCliente(ClienteBean clienteJuridicoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				clienteIntegrado(clienteJuridicoBean);
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.MODIFICADO);

				commit();
			} else {
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.MODIFICADO);
				clienteIntegrado(clienteJuridicoBean);
			}
			Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " atualizado");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}
	}


	/**
	 * Remove assunto encontrado
	 *
	 * @param assuntoBean
	 * @throws Exception
	 */
	protected void removeAssunto(AssuntoBean assuntoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				assuntoIntegrado(assuntoBean);
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.REMOVIDO);

				commit();
			} else {
				chamaApiIntegracaoAssunto(assuntoBean, TipoStatus.REMOVIDO);
				assuntoIntegrado(assuntoBean);
			}
			Log.info("assunto " + assuntoBean.getCodAssunto() + " do cliente " + assuntoBean.getCodCliente()
			+ "  removido");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}

	}


	/**
	 * Remove cliente encontrado
	 *
	 * @param clienteJuridicoBean
	 * @throws Exception
	 */
	protected void removeCliente(ClienteBean clienteJuridicoBean) throws Exception {

		try {
			if (isBD()) {
				iniciaTransacao();

				clienteIntegrado(clienteJuridicoBean);
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.REMOVIDO);

				commit();
			} else {
				chamaApiIntegracaoCliente(clienteJuridicoBean, TipoStatus.REMOVIDO);
				clienteIntegrado(clienteJuridicoBean);
			}
			Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " removido");
		} catch (Exception e) {
			if (isBD()) {
				rollback();
			}
			Funcoes.trataErro(e, this);
		}

	}

	protected void rollback() throws BDException {

		conexao.rollback();
	}

	protected void commit() throws BDException {

		conexao.commit();
	}

	protected void iniciaTransacao() throws BDException {

		conexao.iniciarTransacao();
	}

	/**
	 * Retorna lista de assuntos com o status passado como parametro
	 *
	 * @param status
	 * @return
	 */
	protected abstract List<AssuntoBean> getAssuntos(TipoStatus status) throws Exception;

	/**
	 * Retorna lista de clientes com o status passado como parametro
	 *
	 * @param status
	 * @return
	 */
	protected abstract List<ClienteBean> getClientes(TipoStatus status) throws Exception;

	/**
	 * Efetua procedimento para informar que o assunto foi integrado com sucesso
	 *
	 * @param nomeTabela
	 * @param codigo
	 * @throws BDException
	 */
	protected abstract void assuntoIntegrado(AssuntoBean assunto) throws BDException;

	/**
	 * Efetua procedimento para informar que cliente foi integrado com sucesso
	 *
	 * @param nomeTabela
	 * @param codigo
	 * @throws BDException
	 */
	protected abstract void clienteIntegrado(ClienteBean cliente) throws BDException;


	protected void trataAssuntoEspecial(AssuntoBean assuntoBean) throws Exception{

	}

	protected void trataClienteEspecial(ClienteBean clienteJuridicoBean) throws Exception{

	}

	/**
	 * Define se o integrador usa acesso a banco de dados
	 *
	 * @return
	 */
	protected abstract boolean isBD();


	protected void chamaApiIntegracaoAssunto(AssuntoBean assuntoBean, TipoStatus status) throws Exception {
		Connection conAssunto = Jsoup.connect(Constantes.URL_MCFILE_SERVICOS);

		conAssunto.data(Constantes.PARAM_CMD, ParametrosIntegracao.CHAMADA_INTEGRA_ASSUNTO);
		conAssunto.data(ParametrosIntegracao.PARAM_CHAVE_INTEGRACAO, configuracao.getChaveIntegracao());
		conAssunto.data(ParametrosIntegracao.PARAM_STATUS, status.getStatus());

		conAssunto.data(ParametrosIntegracao.PARAM_COD_ASSUNTO, assuntoBean.getCodAssunto());
		conAssunto.data(ParametrosIntegracao.PARAM_COD_CLIENTE, assuntoBean.getCodCliente());
		conAssunto.data(ParametrosIntegracao.PARAM_TITULO_ASSUNTO, assuntoBean.getNomeAssunto());
		conAssunto.data(ParametrosIntegracao.PARAM_TIPO_ASSUNTO, assuntoBean.getTipoAssunto());
		
		if(assuntoBean.getParte() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_PARTE, assuntoBean.getParte());
		}
		
		if(assuntoBean.getRamo() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_RAMO, assuntoBean.getRamo());
		}
		
		if(assuntoBean.getNumProcesso() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_NUMERO_PROCESSO, assuntoBean.getNumProcesso());
		}
		
		if(assuntoBean.getVara() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_VARA, assuntoBean.getVara());
		}
		
		if(assuntoBean.getForo() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_FORO, assuntoBean.getForo());
		}
		
		if(assuntoBean.getTipoAcao() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_ACAO, assuntoBean.getTipoAcao());
		}
		
		if(assuntoBean.getPartePrincipal() != null){
			conAssunto.data(ParametrosIntegracao.PARAM_PARTE_PRINCIPAL, assuntoBean.getPartePrincipal());
		}

		conAssunto.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		conAssunto.timeout(0);
		conAssunto.post();
	}


	protected void chamaApiIntegracaoCliente(ClienteBean clienteJuridicoBean, TipoStatus status) throws Exception {
		Connection conCliente = Jsoup.connect(Constantes.URL_MCFILE_SERVICOS);

		conCliente.data(Constantes.PARAM_CMD, ParametrosIntegracao.CHAMADA_INTEGRA_CLIENTE);
		conCliente.data(ParametrosIntegracao.PARAM_CHAVE_INTEGRACAO, configuracao.getChaveIntegracao());
		conCliente.data(ParametrosIntegracao.PARAM_STATUS, status.getStatus());

		conCliente.data(ParametrosIntegracao.PARAM_COD_CLIENTE, clienteJuridicoBean.getCodCliente());
		conCliente.data(ParametrosIntegracao.PARAM_RAZAO_SOCIAL, clienteJuridicoBean.getNomeCliente());
		
		if(clienteJuridicoBean.getNomeFantasia() != null){
			conCliente.data(ParametrosIntegracao.PARAM_NOME_FANTASIA, clienteJuridicoBean.getNomeFantasia());
		}
		
		if(clienteJuridicoBean.getCnpj() != null){
			conCliente.data(ParametrosIntegracao.PARAM_CNPJ, clienteJuridicoBean.getCnpj());
		}
		
		if(clienteJuridicoBean.getTipoCliente() != null){
			conCliente.data(ParametrosIntegracao.PARAM_TIPO_CLIENTE, clienteJuridicoBean.getTipoCliente());
		}
		
		if(clienteJuridicoBean.getTipoPessoa() != null){
			conCliente.data(ParametrosIntegracao.PARAM_TIPO_PESSOA, clienteJuridicoBean.getTipoPessoa());
		}
		
		if(clienteJuridicoBean.getSituacao() != null){
			conCliente.data(ParametrosIntegracao.PARAM_SITUACAO, clienteJuridicoBean.getSituacao());
		}
		

		conCliente.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		conCliente.timeout(0);
		conCliente.post();		
	}

}
