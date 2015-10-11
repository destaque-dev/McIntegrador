package integracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.ConexaoIntegrador;
import util.Funcoes;
import util.Log;
import bean.BeanUsuarioLogado;
import bean.Configuracao;
import bean.Constantes;
import bean.DocumentoBean;
import bean.DocumentoInseridoBean;
import bean.ParametroAvancado;
import bean.TipoStatus;
import bean.ValorCampoBean;
import bean.padrao.AssuntoBean;
import bean.padrao.ClienteBean;

import com.google.gson.Gson;

import exception.BDException;
import exception.IntegradorException;

public abstract class IntegradorBase {

    protected Configuracao configuracao;

    private int idSessao;

    protected ConexaoIntegrador conexao;

    public IntegradorBase() throws Exception {

        if (isBD()) {
            conectaBancoDados();
            Log.info("Conectou banco de dados");
        }

        if (isLogin()) {
            loginMcFile();
        }
    }

    /**
     * Loga no McFile
     *
     * @throws Exception
     */
    public void loginMcFile() throws Exception {

        String urlEnviaEmail = Constantes.URL_MCFILE + Constantes.CTRL_SERVICOS;

        Connection con = Jsoup.connect(urlEnviaEmail);

        con.data(Constantes.PARAM_CMD, Constantes.ATV_LOGIN_CLIENT_COM_SESSAO);
        con.data(Constantes.PARAM_LOGIN, configuracao.getLoginMcFile());
        con.data(Constantes.PARAM_SENHA, configuracao.getSenhaMcFile());

        con.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        con.method(Method.POST).ignoreContentType(true);

        con.timeout(0);
        Document doc = con.post();

        BeanUsuarioLogado beanUsuarioLogado = new Gson().fromJson(doc.body().text(), BeanUsuarioLogado.class);

        idSessao = beanUsuarioLogado.getIdSessao();

        Log.info("Logou McFile");
    }

    /**
     * Invoca McFile API para pesquisar no McFile
     *
     * @param parametros
     *            parametros de pesquisa
     * @param level
     *            nivel da arvore
     * @param treeID
     *            codigo da arvore
     * @return
     * @throws IOException
     */
    protected DocumentoBean[] chamaPesquisaMcFile(List<ParametroAvancado> parametros, int level, int treeID,
            Integer codPai) throws IOException {

        String url = Constantes.URL_MCFILE + Constantes.CTRL_SERVICOS;

        Connection con = Jsoup.connect(url);

        con.data(Constantes.PARAM_CMD, Constantes.ATV_PESQUISA_CLIENTE);
        con.data(Constantes.PARAM_AVANCADOS, new Gson().toJson(parametros));
        con.data(Constantes.PARAM_ID_SESSAO, String.valueOf(idSessao));
        con.data(Constantes.PARAM_NIVEL_ARVORE, String.valueOf(level));
        con.data(Constantes.PARAM_ARVORE, String.valueOf(treeID));

        if (codPai != null && codPai > 0) {
            con.data(Constantes.PARAM_CODIGO_PAI, String.valueOf(codPai));

        }

        con.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        con.timeout(0);

        Document doc = con.post();

        DocumentoBean[] docsEncontrados = new Gson().fromJson(doc.body().text(), DocumentoBean[].class);

        return docsEncontrados;
    }

    /**
     * Invoca McFile API para inserir um documento no McFile
     *
     * @param listaCampos
     * @return
     * @throws Exception
     */
    protected int chamaInsereDocumentoMcFile(List<ValorCampoBean> listaCampos) throws Exception {

        String url = Constantes.URL_MCFILE + Constantes.CTRL_SERVICOS;

        Connection con = Jsoup.connect(url);

        con.data(Constantes.PARAM_CMD, Constantes.ATV_INSERE_DOCUMENTO);
        con.data(Constantes.PARAM_VALORES_CAMPOS, new Gson().toJson(listaCampos));
        con.data(Constantes.PARAM_ID_SESSAO, String.valueOf(idSessao));

        con.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        con.timeout(0);

        Document doc = con.post();

        DocumentoInseridoBean docInserido = new Gson().fromJson(doc.body().text(), DocumentoInseridoBean.class);
        return docInserido.getCodDoc();

    }

    /**
     * Invoca McFile API para atulizar um documento no McFile
     *
     * @param codDoc
     * @param listaCampos
     * @return
     * @throws Exception
     */
    protected void chamaAtualizaDocumentoMcFile(int codDoc, int codTipoDoc, List<ValorCampoBean> listaCampos)
            throws Exception {

        String url = Constantes.URL_MCFILE + Constantes.CTRL_SERVICOS;

        Connection con = Jsoup.connect(url);

        con.data(Constantes.PARAM_CMD, Constantes.ATUALIZA_DOCUMENTO);
        con.data(Constantes.PARAM_VALORES_CAMPOS, new Gson().toJson(listaCampos));
        con.data(Constantes.PARAM_ID_SESSAO, String.valueOf(idSessao));
        con.data(Constantes.PARAM_COD_DOC, String.valueOf(codDoc));
        con.data(Constantes.PARAM_COD_TIPO_DOC, String.valueOf(codTipoDoc));

        con.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        con.timeout(0);

        con.post();

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
                insereassuntoMcFile(assuntoBean);

                commit();
            } else {
                insereassuntoMcFile(assuntoBean);
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
                insereClienteMcFile(clienteJuridicoBean);

                commit();
            } else {
                insereClienteMcFile(clienteJuridicoBean);
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
    protected void atualizaassunto(AssuntoBean assuntoBean) throws Exception {

        try {
            if (isBD()) {
                iniciaTransacao();

                assuntoIntegrado(assuntoBean);
                atualizaassuntoMcFile(assuntoBean);

                commit();
            } else {
                atualizaassuntoMcFile(assuntoBean);
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
                atualizaClienteMcFile(clienteJuridicoBean);

                commit();
            } else {
                atualizaClienteMcFile(clienteJuridicoBean);
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
    protected void removeassunto(AssuntoBean assuntoBean) throws Exception {

        try {
            if (isBD()) {
                iniciaTransacao();

                assuntoIntegrado(assuntoBean);
                removeassuntoMcFile(assuntoBean);

                commit();
            } else {
                removeassuntoMcFile(assuntoBean);
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
                removeClienteMcFile(clienteJuridicoBean);

                commit();
            } else {
                removeClienteMcFile(clienteJuridicoBean);
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

    private void rollback() throws BDException {

        conexao.rollback();
    }

    private void commit() throws BDException {

        conexao.commit();
    }

    private void iniciaTransacao() throws BDException {

        conexao.iniciarTransacao();
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
                atualizaassunto(assuntoBean);
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
                removeassunto(assuntoBean);
            }
        }

    }

    /**
     * Insere um assunto no McFile
     *
     * @param assunto
     * @return
     * @throws Exception
     */
    protected void insereassuntoMcFile(AssuntoBean assunto) throws Exception {

        try {
            List<ValorCampoBean> listaCamposassunto = montaListaCamposassunto(assunto);

            int codDocCliente = pesquisaCliente(assunto.getCodCliente(), assunto.getCodArea());
            ValorCampoBean campoILCliente = new ValorCampoBean("CLIENTE", codDocCliente);
            listaCamposassunto.add(campoILCliente);

            ValorCampoBean campoTipoDoc = new ValorCampoBean("codTipoDoc", AssuntoBean.COD_TIPO_DOC);
            listaCamposassunto.add(campoTipoDoc);

            if (assunto.getCodArea() > 0) {
                ValorCampoBean campoCodArea = new ValorCampoBean("codArea", assunto.getCodArea());
                listaCamposassunto.add(campoCodArea);
            }

            chamaInsereDocumentoMcFile(listaCamposassunto);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Insere um cliente no McFile
     *
     * @param cliente
     * @return
     * @throws Exception
     */
    protected void insereClienteMcFile(ClienteBean cliente) throws Exception {

        try {
            List<ValorCampoBean> listaCamposCliente = montaListaCamposClienteJuridico(cliente);

            ValorCampoBean campoTipoDoc = new ValorCampoBean("codTipoDoc", ClienteBean.COD_TIPO_DOC);
            listaCamposCliente.add(campoTipoDoc);

            if (cliente.getCodArea() > 0) {
                ValorCampoBean campoCodArea = new ValorCampoBean("codArea", cliente.getCodArea());
                listaCamposCliente.add(campoCodArea);
            }

            chamaInsereDocumentoMcFile(listaCamposCliente);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Pesquisa um cliente no McFile utilizando seu codigo
     *
     * @param codCliente
     * @return
     * @throws IOException
     * @throws IntegradorException
     */
    protected int pesquisaCliente(String codCliente, int codArea) throws IOException, IntegradorException {

        List<ParametroAvancado> parametros = new ArrayList<ParametroAvancado>();

        ParametroAvancado parametroCodCliente = new ParametroAvancado("NUMERO", codCliente);
        parametros.add(parametroCodCliente);

        DocumentoBean[] docsEncontrados = chamaPesquisaMcFile(parametros, Constantes.NIVEL_CLIENTE_JURIDICO,
                Constantes.ARVORE_JURIDICA, null);

        if (docsEncontrados.length < 1) {
            throw new IntegradorException("Não encontrado cliente o código " + codCliente);
        } else if (docsEncontrados.length > 1) {
            Log.info("Encontrado mais de um cliente com o código " + codCliente + ". Será usado o primeiro retornado.");
        }

        return Integer.parseInt(docsEncontrados[0].getCodigo());

    }

    /**
     * Pesquisa um assunto no McFile utilizado seu codigo
     *
     * @param codassunto
     * @return
     * @throws IOException
     * @throws IntegradorException
     */
    protected int pesquisaAssunto(String codassunto, int codDocCliente, int codArea) throws IOException,
            IntegradorException {

        List<ParametroAvancado> parametros = new ArrayList<ParametroAvancado>();

        ParametroAvancado parametroCodassunto = new ParametroAvancado("NUMERO", codassunto);
        parametros.add(parametroCodassunto);

        DocumentoBean[] docsEncontrados = chamaPesquisaMcFile(parametros, Constantes.NIVEL_ASSUNTO_JURIDICO,
                Constantes.ARVORE_JURIDICA, codDocCliente);

        if (docsEncontrados.length < 1) {
            throw new IntegradorException("Não encontrado assunto com código " + codassunto + " do cliente "
                    + codDocCliente);
        } else if (docsEncontrados.length > 1) {
            Log.info("Encontrado mais de um cliente com o código " + codassunto + " do cliente " + codDocCliente
                    + ". Será usado o primeiro retornado.");
        }

        return Integer.parseInt(docsEncontrados[0].getCodigo());
    }

    /**
     * Atualiza um assunto no McFile
     *
     * @param assuntoBean
     * @throws Exception
     */
    protected void atualizaassuntoMcFile(AssuntoBean assuntoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(assuntoBean.getCodCliente(), assuntoBean.getCodArea());
            int codDocassunto = pesquisaAssunto(assuntoBean.getCodAssunto(), codDocCliente, assuntoBean.getCodArea());

            List<ValorCampoBean> campos = montaListaCamposassunto(assuntoBean);

            chamaAtualizaDocumentoMcFile(codDocassunto, AssuntoBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Atualiza um cliente juridico no McFile
     *
     * @param clienteJuridicoBean
     * @throws Exception
     */
    protected void atualizaClienteMcFile(ClienteBean clienteJuridicoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(clienteJuridicoBean.getCodCliente(), clienteJuridicoBean.getCodArea());

            List<ValorCampoBean> campos = montaListaCamposClienteJuridico(clienteJuridicoBean);

            chamaAtualizaDocumentoMcFile(codDocCliente, ClienteBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Remove um assunto no McFile
     *
     * @param assuntoBean
     * @throws Exception
     */
    protected void removeassuntoMcFile(AssuntoBean assuntoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(assuntoBean.getCodCliente(), assuntoBean.getCodArea());
            int codDocassunto = pesquisaAssunto(assuntoBean.getCodAssunto(), codDocCliente, assuntoBean.getCodArea());

            List<ValorCampoBean> campos = new ArrayList<ValorCampoBean>();
            ValorCampoBean valorCampo = new ValorCampoBean("ativo", false);
            campos.add(valorCampo);

            chamaAtualizaDocumentoMcFile(codDocassunto, AssuntoBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Atualiza um cliente juridico no McFile
     *
     * @param clienteJuridicoBean
     * @throws Exception
     */
    protected void removeClienteMcFile(ClienteBean clienteJuridicoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(clienteJuridicoBean.getCodCliente(), clienteJuridicoBean.getCodArea());

            List<ValorCampoBean> campos = new ArrayList<ValorCampoBean>();

            ValorCampoBean valorCampo = new ValorCampoBean("ativo", false);
            campos.add(valorCampo);

            chamaAtualizaDocumentoMcFile(codDocCliente, ClienteBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            throw e;
        }
    }

    private List<ValorCampoBean> montaListaCamposClienteJuridico(ClienteBean cliente) {

        List<ValorCampoBean> listaCamposCliente = new ArrayList<ValorCampoBean>();

        ValorCampoBean campoCodCliente = new ValorCampoBean("NUMERO", cliente.getCodCliente());
        listaCamposCliente.add(campoCodCliente);

        ValorCampoBean campoNomeCliente = new ValorCampoBean("NOME", cliente.getNomeCliente());
        listaCamposCliente.add(campoNomeCliente);

        ValorCampoBean campoNomeFantasia = new ValorCampoBean("NOME_FANTASIA", cliente.getNomeFantasia());
        listaCamposCliente.add(campoNomeFantasia);

        ValorCampoBean campoCnpj = new ValorCampoBean("CNPJ", cliente.getCnpj());
        listaCamposCliente.add(campoCnpj);

        ValorCampoBean campoTipoCliente = new ValorCampoBean("TIPO_CLIENTE", cliente.getTipoCliente());
        listaCamposCliente.add(campoTipoCliente);

        ValorCampoBean campoTipoPessoa = new ValorCampoBean("TIPO_PESSOA", cliente.getTipoPessoa());
        listaCamposCliente.add(campoTipoPessoa);

        ValorCampoBean campoSituacao = new ValorCampoBean("SITUACAO", cliente.getSituacao());
        listaCamposCliente.add(campoSituacao);

        return listaCamposCliente;
    }

    private List<ValorCampoBean> montaListaCamposassunto(AssuntoBean assunto) {

        List<ValorCampoBean> listaCamposassunto = new ArrayList<ValorCampoBean>();

        ValorCampoBean campoTipoassunto = new ValorCampoBean("TIPO", assunto.getTipoAssunto());
        listaCamposassunto.add(campoTipoassunto);

        ValorCampoBean campoNumeroContrato = new ValorCampoBean("NUMERO", assunto.getCodAssunto());
        listaCamposassunto.add(campoNumeroContrato);

        ValorCampoBean campoTituloContrato = new ValorCampoBean("TITULO", assunto.getNomeAssunto());
        listaCamposassunto.add(campoTituloContrato);

        ValorCampoBean campoIdProcesso = new ValorCampoBean("ID_PROCESSO", "");
        listaCamposassunto.add(campoIdProcesso);

        ValorCampoBean campoTipoAcao = new ValorCampoBean("ACAO", assunto.getTipoAcao());
        listaCamposassunto.add(campoTipoAcao);

        ValorCampoBean campoForo = new ValorCampoBean("FORO", assunto.getForo());
        listaCamposassunto.add(campoForo);

        ValorCampoBean campoVara = new ValorCampoBean("VARA", assunto.getVara());
        listaCamposassunto.add(campoVara);

        ValorCampoBean campoNumProcesso = new ValorCampoBean("NUM_PROCESSO", assunto.getNumProcesso());
        listaCamposassunto.add(campoNumProcesso);

        ValorCampoBean campoParte = new ValorCampoBean("PARTE", assunto.getParte());
        listaCamposassunto.add(campoParte);

        ValorCampoBean campoNomeContrato = new ValorCampoBean("NOME_CONTRATO", assunto.getNomeContrato());
        listaCamposassunto.add(campoNomeContrato);

        ValorCampoBean campoParteContraria = new ValorCampoBean("PARTE_PRINCIPAL", assunto.getPartePrincipal());
        listaCamposassunto.add(campoParteContraria);

        ValorCampoBean campoRamo = new ValorCampoBean("RAMO", assunto.getRamo());
        listaCamposassunto.add(campoRamo);

        return listaCamposassunto;
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

    /**
     * Define se o integrador usa acesso a banco de dados
     *
     * @return
     */
    protected abstract boolean isBD();

    /**
     * Define se o integrador fará login no sistema
     *
     * @return
     */
    protected abstract boolean isLogin();

    public int getIdSessao() {

        return idSessao;
    }

    public Configuracao getConfiguracao() {

        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {

        this.configuracao = configuracao;
    }
}
