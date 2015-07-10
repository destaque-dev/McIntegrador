package integracao;

import java.util.List;

import util.ConexaoIntegrador;
import util.Log;
import bean.juridico.CasoBean;
import bean.juridico.ClienteJuridicoBean;
import exception.BDException;

public abstract class IntegradorPadraoTabela extends IntegradorPadrao {

    protected ConexaoIntegrador conexao;

    public IntegradorPadraoTabela() throws Exception {

        conectaBancoDados();
        Log.info("Conectou banco de dados");
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

    @Override
    protected void adicionaCaso(CasoBean casoBean) throws BDException, Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoCaso(casoBean.getCodCaso());
            insereCasoMcFile(casoBean);
            conexao.commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  inserido");
        } catch (Exception e) {
            conexao.rollback();
        }
    }

    @Override
    protected void adicionaClienteJuridico(ClienteJuridicoBean clienteJuridicoBean) throws BDException, Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoClienteJuridico(clienteJuridicoBean.getCodCliente());
            insereClienteMcFile(clienteJuridicoBean);
            conexao.commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " inserido");
        } catch (Exception e) {
            conexao.rollback();
        }
    }

    @Override
    protected void atualizaCaso(CasoBean casoBean) throws BDException, Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoCaso(casoBean.getCodCaso());
            atualizaCasoMcFile(casoBean);
            conexao.commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  atualizado");
        } catch (Exception e) {
            conexao.rollback();
        }
    }

    @Override
    protected void atualizaClienteJuridico(ClienteJuridicoBean clienteJuridicoBean) throws BDException, Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoClienteJuridico(clienteJuridicoBean.getCodCliente());
            atualizaClienteMcFile(clienteJuridicoBean);
            conexao.commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " atualizado");
        } catch (Exception e) {
            conexao.rollback();
        }
    }

    @Override
    protected void removeCaso(CasoBean casoBean) throws Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoCaso(casoBean.getCodCaso());
            removeCasoMcFile(casoBean);
            conexao.commit();
            Log.info("Caso " + casoBean.getCodCaso() + " do cliente " + casoBean.getCodCliente() + "  removido");
        } catch (Exception e) {
            conexao.rollback();
        }

    }

    @Override
    protected void removeClienteJuridico(ClienteJuridicoBean clienteJuridicoBean) throws Exception {

        conexao.iniciarTransacao();
        try {
            atualizaTabelaImportacaoClienteJuridico(clienteJuridicoBean.getCodCliente());
            removeClienteMcFile(clienteJuridicoBean);
            conexao.commit();
            Log.info("Cliente " + clienteJuridicoBean.getCodCliente() + " removido");
        } catch (Exception e) {
            conexao.rollback();
        }

    }

    /**
     * Atualiza tabela de integracao
     *
     * @param nomeTabela
     * @param codigo
     * @throws BDException
     */
    protected abstract void atualizaTabelaImportacaoCaso(int codigo) throws BDException;

    /**
     * Atualiza tabela de integracao
     *
     * @param nomeTabela
     * @param codigo
     * @throws BDException
     */
    protected abstract void atualizaTabelaImportacaoClienteJuridico(int codigo) throws BDException;

    /**
     * Retorna lista de casos com os status passados como parametro
     *
     * @param status
     * @return
     * @throws BDException
     */
    @Override
    protected abstract List<CasoBean> getCasos(String... status) throws BDException;

    /**
     * Retorna lista de clientes juridicos com os status passados como parametro
     *
     * @param status
     * @return
     * @throws BDException
     */
    @Override
    protected abstract List<ClienteJuridicoBean> getClientesJuridicos(String... status) throws BDException;
}
