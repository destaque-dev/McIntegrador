package integracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.ConexaoIntegrador;
import util.Funcoes;
import util.Log;
import bean.Constantes;
import bean.DocumentoBean;
import bean.ParametroAvancado;
import bean.TipoStatus;
import bean.ValorCampoBean;
import bean.padrao.AssuntoBean;
import bean.padrao.ClienteBean;
import exception.BDException;
import exception.IntegradorException;

public abstract class IntegradorBase extends IntegradorMcFile {

    protected ConexaoIntegrador conexao;

    public IntegradorBase() throws Exception {

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
     * Adiciona assunto encontrado
     *
     * @param assuntoBean
     * @throws Exception
     */
    protected void adicionaAssunto(AssuntoBean assuntoBean) throws Exception {

        iniciaTransacao();
        try {
            assuntoIntegrado(assuntoBean.getCodassunto());
            insereassuntoMcFile(assuntoBean);
            commit();
            Log.info("assunto " + assuntoBean.getCodassunto() + " do cliente " + assuntoBean.getCodCliente()
                    + "  inserido");
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    /**
     * Adiciona cliente encontrado
     *
     * @param clienteJuridicoBean
     * @throws Exception
     */
    protected void adicionaCliente(ClienteBean clienteJuridicoBean) throws Exception {

        iniciaTransacao();
        try {
            clienteIntegrado(clienteJuridicoBean.getCodCliente());
            insereClienteMcFile(clienteJuridicoBean);
            commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " inserido");
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    protected void atualizaassunto(AssuntoBean assuntoBean) throws Exception {

        iniciaTransacao();
        try {
            assuntoIntegrado(assuntoBean.getCodassunto());
            atualizaassuntoMcFile(assuntoBean);
            commit();
            Log.info("assunto " + assuntoBean.getCodassunto() + " do cliente " + assuntoBean.getCodCliente()
                    + "  atualizado");
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    /**
     * Atualiza cliente encontrado
     *
     * @param assuntoBean
     * @throws Exception
     */
    protected void atualizaCliente(ClienteBean clienteJuridicoBean) throws Exception {

        iniciaTransacao();
        try {
            clienteIntegrado(clienteJuridicoBean.getCodCliente());
            atualizaClienteMcFile(clienteJuridicoBean);
            commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " atualizado");
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    /**
     * Remove assunto encontrado
     *
     * @param assuntoBean
     * @throws Exception
     */
    protected void removeassunto(AssuntoBean assuntoBean) throws Exception {

        iniciaTransacao();
        try {
            assuntoIntegrado(assuntoBean.getCodassunto());
            removeassuntoMcFile(assuntoBean);
            commit();
            Log.info("assunto " + assuntoBean.getCodassunto() + " do cliente " + assuntoBean.getCodCliente()
                    + "  removido");
        } catch (Exception e) {
            rollback();
            throw e;
        }

    }

    /**
     * Remove cliente encontrado
     *
     * @param clienteJuridicoBean
     * @throws Exception
     */
    protected void removeCliente(ClienteBean clienteJuridicoBean) throws Exception {

        iniciaTransacao();
        try {
            clienteIntegrado(clienteJuridicoBean.getCodCliente());
            removeClienteMcFile(clienteJuridicoBean);
            commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " removido");
        } catch (Exception e) {
            rollback();
            throw e;
        }

    }

    /**
     * Efetua procedimento para informar que o assunto foi integrado com sucesso
     *
     * @param nomeTabela
     * @param codigo
     * @throws BDException
     */
    protected abstract void assuntoIntegrado(int codigo) throws BDException;

    /**
     * Efetua procedimento para informar que cliente foi integrado com sucesso
     *
     * @param nomeTabela
     * @param codigo
     * @throws BDException
     */
    protected abstract void clienteIntegrado(int codigo) throws BDException;

    /**
     * Define se o integrador usa acesso a banco de dados
     *
     * @return
     */
    protected abstract boolean isBD();

    private void rollback() throws BDException {

        if (isBD()) {
            conexao.rollback();
        }
    }

    private void commit() throws BDException {

        if (isBD()) {
            conexao.commit();
        }
    }

    private void iniciaTransacao() throws BDException {

        if (isBD()) {
            conexao.iniciarTransacao();
        }
    }

    @Override
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

    @Override
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

    @Override
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
     * Insere um assunto no McFile
     *
     * @param assunto
     * @return
     * @throws Exception
     */
    protected void insereassuntoMcFile(AssuntoBean assunto) throws Exception {

        try {
            List<ValorCampoBean> listaCamposassunto = montaListaCamposassunto(assunto);

            int codDocCliente = pesquisaCliente(assunto.getCodCliente());
            ValorCampoBean campoILCliente = new ValorCampoBean("CLIENTE", codDocCliente);
            listaCamposassunto.add(campoILCliente);

            ValorCampoBean campoTipoDoc = new ValorCampoBean("codTipoDoc", AssuntoBean.COD_TIPO_DOC);
            listaCamposassunto.add(campoTipoDoc);

            // ValorCampoBean campoCodArea = new ValorCampoBean("codArea",
            // Integer.parseInt(configuracao.getArea()));
            // listaCamposassunto.add(campoCodArea);

            chamaInsereDocumentoMcFile(listaCamposassunto);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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

            // ValorCampoBean campoCodArea = new ValorCampoBean("codArea",
            // Integer.parseInt(configuracao.getArea()));
            // listaCamposCliente.add(campoCodArea);

            chamaInsereDocumentoMcFile(listaCamposCliente);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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
    private int pesquisaCliente(int codCliente) throws IOException, IntegradorException {

        List<ParametroAvancado> parametros = new ArrayList<ParametroAvancado>();

        ParametroAvancado parametroCodCliente = new ParametroAvancado("NUMERO", String.valueOf(codCliente));
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
    private int pesquisaassunto(int codassunto, int codDocCliente) throws IOException, IntegradorException {

        List<ParametroAvancado> parametros = new ArrayList<ParametroAvancado>();

        ParametroAvancado parametroCodassunto = new ParametroAvancado("NUMERO", String.valueOf(codassunto));
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
            int codDocCliente = pesquisaCliente(assuntoBean.getCodCliente());
            int codDocassunto = pesquisaassunto(assuntoBean.getCodassunto(), codDocCliente);

            List<ValorCampoBean> campos = montaListaCamposassunto(assuntoBean);

            chamaAtualizaDocumentoMcFile(codDocassunto, AssuntoBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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
            int codDocCliente = pesquisaCliente(clienteJuridicoBean.getCodCliente());

            List<ValorCampoBean> campos = montaListaCamposClienteJuridico(clienteJuridicoBean);

            chamaAtualizaDocumentoMcFile(codDocCliente, ClienteBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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
            int codDocCliente = pesquisaCliente(assuntoBean.getCodCliente());
            int codDocassunto = pesquisaassunto(assuntoBean.getCodassunto(), codDocCliente);

            List<ValorCampoBean> campos = new ArrayList<ValorCampoBean>();
            ValorCampoBean valorCampo = new ValorCampoBean("ativo", false);
            campos.add(valorCampo);

            chamaAtualizaDocumentoMcFile(codDocassunto, AssuntoBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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
            int codDocCliente = pesquisaCliente(clienteJuridicoBean.getCodCliente());

            List<ValorCampoBean> campos = new ArrayList<ValorCampoBean>();

            ValorCampoBean valorCampo = new ValorCampoBean("ativo", false);
            campos.add(valorCampo);

            chamaAtualizaDocumentoMcFile(codDocCliente, ClienteBean.COD_TIPO_DOC, campos);
        } catch (Exception e) {
            Funcoes.trataErro(e, this);
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

        return listaCamposCliente;
    }

    private List<ValorCampoBean> montaListaCamposassunto(AssuntoBean assunto) {

        List<ValorCampoBean> listaCamposassunto = new ArrayList<ValorCampoBean>();

        ValorCampoBean campoTipoassunto = new ValorCampoBean("TIPO", assunto.getTipoAssunto());
        listaCamposassunto.add(campoTipoassunto);

        ValorCampoBean campoNumeroContrato = new ValorCampoBean("NUMERO", assunto.getCodassunto());
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

        return listaCamposassunto;
    }

}
