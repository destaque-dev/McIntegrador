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
import bean.juridico.CasoBean;
import bean.juridico.ClienteBean;
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
     * Adiciona caso encontrado
     *
     * @param casoBean
     * @throws Exception
     */
    protected void adicionaCaso(CasoBean casoBean) throws BDException, Exception {

        iniciaTransacao();
        try {
            casoIntegrado(casoBean.getCodCaso());
            insereCasoMcFile(casoBean);
            commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  inserido");
        } catch (Exception e) {
            rollback();
        }
    }

    /**
     * Adiciona cliente encontrado
     *
     * @param clienteJuridicoBean
     * @throws Exception
     */
    protected void adicionaCliente(ClienteBean clienteJuridicoBean) throws BDException, Exception {

        iniciaTransacao();
        try {
            clienteIntegrado(clienteJuridicoBean.getCodCliente());
            insereClienteMcFile(clienteJuridicoBean);
            commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " inserido");
        } catch (Exception e) {
            rollback();
        }
    }

    protected void atualizaCaso(CasoBean casoBean) throws BDException, Exception {

        iniciaTransacao();
        try {
            casoIntegrado(casoBean.getCodCaso());
            atualizaCasoMcFile(casoBean);
            commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  atualizado");
        } catch (Exception e) {
            rollback();
        }
    }

    /**
     * Atualiza cliente encontrado
     *
     * @param casoBean
     * @throws Exception
     */
    protected void atualizaCliente(ClienteBean clienteJuridicoBean) throws BDException, Exception {

        iniciaTransacao();
        try {
            clienteIntegrado(clienteJuridicoBean.getCodCliente());
            atualizaClienteMcFile(clienteJuridicoBean);
            commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " atualizado");
        } catch (Exception e) {
            rollback();
        }
    }

    /**
     * Remove caso encontrado
     *
     * @param casoBean
     * @throws Exception
     */
    protected void removeCaso(CasoBean casoBean) throws Exception {

        iniciaTransacao();
        try {
            casoIntegrado(casoBean.getCodCaso());
            removeCasoMcFile(casoBean);
            commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  removido");
        } catch (Exception e) {
            rollback();
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
        }

    }

    /**
     * Efetua procedimento para informar que o caso foi integrado com sucesso
     *
     * @param nomeTabela
     * @param codigo
     * @throws BDException
     */
    protected abstract void casoIntegrado(int codigo) throws BDException;

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

        List<CasoBean> casos = getCasos(TipoStatus.ADICIONADO);
        if (casos.size() > 0) {
            Log.info("Casos novos encontrados: " + casos.size());

            for (CasoBean casoBean : casos) {
                adicionaCaso(casoBean);
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

        List<CasoBean> casos = getCasos(TipoStatus.MODIFICADO);
        if (casos.size() > 0) {
            Log.info("Casos modificados encontrados: " + casos.size());

            for (CasoBean casoBean : casos) {
                atualizaCaso(casoBean);
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

        List<CasoBean> casos = getCasos(TipoStatus.REMOVIDO);
        if (casos.size() > 0) {
            Log.info("Casos removidos encontrados: " + casos.size());

            for (CasoBean casoBean : casos) {
                removeCaso(casoBean);
            }
        }

    }

    /**
     * Retorna lista de casos com o status passado como parametro
     *
     * @param status
     * @return
     */
    protected abstract List<CasoBean> getCasos(TipoStatus status) throws Exception;

    /**
     * Retorna lista de clientes com o status passado como parametro
     *
     * @param status
     * @return
     */
    protected abstract List<ClienteBean> getClientes(TipoStatus status) throws Exception;

    /**
     * Insere um caso no McFile
     *
     * @param caso
     * @return
     * @throws Exception
     */
    protected void insereCasoMcFile(CasoBean caso) throws Exception {

        try {
            List<ValorCampoBean> listaCamposCaso = montaListaCamposCaso(caso);

            int codDocCliente = pesquisaCliente(caso.getCodCliente());
            ValorCampoBean campoILCliente = new ValorCampoBean("CLIENTE", codDocCliente);
            listaCamposCaso.add(campoILCliente);

            ValorCampoBean campoTipoDoc = new ValorCampoBean("codTipoDoc", CasoBean.COD_TIPO_DOC);
            listaCamposCaso.add(campoTipoDoc);

            ValorCampoBean campoCodArea = new ValorCampoBean("codArea", Integer.parseInt(configuracao.getArea()));
            listaCamposCaso.add(campoCodArea);

            chamaInsereDocumentoMcFile(listaCamposCaso);
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

            ValorCampoBean campoCodArea = new ValorCampoBean("codArea", Integer.parseInt(configuracao.getArea()));
            listaCamposCliente.add(campoCodArea);

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
     * Pesquisa um caso no McFile utilizado seu codigo
     *
     * @param codCaso
     * @return
     * @throws IOException
     * @throws IntegradorException
     */
    private int pesquisaCaso(int codCaso, int codDocCliente) throws IOException, IntegradorException {

        List<ParametroAvancado> parametros = new ArrayList<ParametroAvancado>();

        ParametroAvancado parametroCodCaso = new ParametroAvancado("NUMERO", String.valueOf(codCaso));
        parametros.add(parametroCodCaso);

        DocumentoBean[] docsEncontrados = chamaPesquisaMcFile(parametros, Constantes.NIVEL_CASO_JURIDICO,
                Constantes.ARVORE_JURIDICA, codDocCliente);

        if (docsEncontrados.length < 1) {
            throw new IntegradorException("Não encontrado caso com código " + codCaso + " do cliente " + codDocCliente);
        } else if (docsEncontrados.length > 1) {
            Log.info("Encontrado mais de um cliente com o código " + codCaso + " do cliente " + codDocCliente
                    + ". Será usado o primeiro retornado.");
        }

        return Integer.parseInt(docsEncontrados[0].getCodigo());
    }

    /**
     * Atualiza um caso no McFile
     *
     * @param casoBean
     * @throws Exception
     */
    protected void atualizaCasoMcFile(CasoBean casoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(casoBean.getCodCliente());
            int codDocCaso = pesquisaCaso(casoBean.getCodCaso(), codDocCliente);

            List<ValorCampoBean> campos = montaListaCamposCaso(casoBean);

            chamaAtualizaDocumentoMcFile(codDocCaso, CasoBean.COD_TIPO_DOC, campos);
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
     * Remove um caso no McFile
     *
     * @param casoBean
     * @throws Exception
     */
    protected void removeCasoMcFile(CasoBean casoBean) throws Exception {

        try {
            int codDocCliente = pesquisaCliente(casoBean.getCodCliente());
            int codDocCaso = pesquisaCaso(casoBean.getCodCaso(), codDocCliente);

            List<ValorCampoBean> campos = new ArrayList<ValorCampoBean>();
            ValorCampoBean valorCampo = new ValorCampoBean("ativo", false);
            campos.add(valorCampo);

            chamaAtualizaDocumentoMcFile(codDocCaso, CasoBean.COD_TIPO_DOC, campos);
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

    private List<ValorCampoBean> montaListaCamposCaso(CasoBean caso) {

        List<ValorCampoBean> listaCamposCaso = new ArrayList<ValorCampoBean>();

        ValorCampoBean campoTipoCaso = new ValorCampoBean("TIPO", caso.getTipoCaso());
        listaCamposCaso.add(campoTipoCaso);

        ValorCampoBean campoNumeroContrato = new ValorCampoBean("NUMERO", caso.getCodCaso());
        listaCamposCaso.add(campoNumeroContrato);

        ValorCampoBean campoTituloContrato = new ValorCampoBean("TITULO", caso.getNomeCaso());
        listaCamposCaso.add(campoTituloContrato);

        ValorCampoBean campoIdProcesso = new ValorCampoBean("ID_PROCESSO", "");
        listaCamposCaso.add(campoIdProcesso);

        ValorCampoBean campoTipoAcao = new ValorCampoBean("ACAO", caso.getTipoAcao());
        listaCamposCaso.add(campoTipoAcao);

        ValorCampoBean campoForo = new ValorCampoBean("FORO", caso.getForo());
        listaCamposCaso.add(campoForo);

        ValorCampoBean campoVara = new ValorCampoBean("VARA", caso.getVara());
        listaCamposCaso.add(campoVara);

        ValorCampoBean campoNumProcesso = new ValorCampoBean("NUM_PROCESSO", caso.getNumProcesso());
        listaCamposCaso.add(campoNumProcesso);

        ValorCampoBean campoParte = new ValorCampoBean("PARTE", caso.getParte());
        listaCamposCaso.add(campoParte);

        ValorCampoBean campoNomeContrato = new ValorCampoBean("NOME_CONTRATO", caso.getNomeContrato());
        listaCamposCaso.add(campoNomeContrato);

        ValorCampoBean campoParteContraria = new ValorCampoBean("PARTE_PRINCIPAL", caso.getPartePrincipal());
        listaCamposCaso.add(campoParteContraria);

        return listaCamposCaso;
    }

}
