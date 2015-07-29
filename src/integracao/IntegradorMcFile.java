package integracao;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.Log;
import bean.BeanUsuarioLogado;
import bean.Configuracao;
import bean.Constantes;
import bean.DocumentoBean;
import bean.DocumentoInseridoBean;
import bean.ParametroAvancado;
import bean.ValorCampoBean;

import com.google.gson.Gson;

public abstract class IntegradorMcFile {

    protected Configuracao configuracao;

    private int idSessao;

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
     * Verifica novos registros a serem inseridos na base e os insere, caso encontre
     *
     * @throws Exception
     */
    public abstract void verificaRegistrosNovos() throws Exception;

    /**
     * Verifica registros a serem inseridos na base e os atualiza, caso encontre
     *
     * @throws Exception
     */
    public abstract void verificaRegistrosAtualizados() throws Exception;

    /**
     * Verifica registros a serem inseridos na base e os atualiza, caso encontre
     *
     * @throws Exception
     */
    public abstract void verificaRegistrosRemovidos() throws Exception;

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
