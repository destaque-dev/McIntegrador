package integracao;

import java.util.ArrayList;
import java.util.List;

import util.ResultBD;
import bean.TipoStatus;
import bean.padrao.AssuntoBean;
import bean.padrao.ClienteBean;
import exception.BDException;

/**
 * Integrador pré-configurado para acessar tabelas intermediárias. Para mais informações, consulte
 * http://mcfile-developers.helpscoutdocs.com/article/81-o-mcintegrador
 *
 * @author Vinicius
 */
public class IntegradorExemplo extends IntegradorBase {

    public IntegradorExemplo() throws Exception {

        super();
    }

    @Override
    protected void assuntoIntegrado(AssuntoBean assunto) throws BDException {

        String update = "UPDATE TABELA_ASSUNTO_INTEGRACAO SET STATUS = '" + TipoStatus.IMPORTADO.getStatus()
                + "' WHERE CODIGO_ASSUNTO = ?";

        conexao.executeUpdate(update, assunto.getCodAssunto());

    }

    @Override
    protected void clienteIntegrado(ClienteBean cliente) throws BDException {

        String update = "UPDATE TABELA_CLIENTE_INTEGRACAO SET STATUS = '" + TipoStatus.IMPORTADO.getStatus()
                + "' WHERE CODIGO_CLIENTE = ?";

        conexao.executeUpdate(update, cliente.getCodCliente());

    }

    @Override
    protected List<AssuntoBean> getAssuntos(TipoStatus status) throws Exception {

        List<AssuntoBean> casos = new ArrayList<AssuntoBean>();

        ResultBD res = null;
        try {
            res = conexao.executeQuery("SELECT * FROM TABELA_ASSUNTO_INTEGRACAO WHERE STATUS = '" + status.getStatus()
                    + "'");

            while (res.next()) {

                String codCliente = res.getString("CODIGO_CLIENTE");
                String codAssunto = res.getString("CODIGO_ASSUNTO");
                String tipoAssunto = res.getString("TIPO_ASSUNTO");
                String tituloAssunto = res.getString("TITULO_ASSUNTO");

                String numProcesso = res.getString("NUMERO_PROCESSO");
                String tipoAcao = res.getString("ACAO");
                String vara = res.getString("VARA");
                String foro = res.getString("FORO");
                String parte = res.getString("PARTE");
                String partePrincipal = res.getString("PARTE_PRINCIPAL");
                String ramo = res.getString("RAMO");

                if (tituloAssunto == null) {
                    tituloAssunto = parte + " - " + numProcesso;
                }

                if (tipoAssunto == null) {
                    tipoAssunto = "Processo";
                }

                AssuntoBean assuntoBean = new AssuntoBean();
                assuntoBean.setCodAssunto(codAssunto);
                assuntoBean.setCodCliente(codCliente);
                assuntoBean.setNomeAssunto(tituloAssunto);
                assuntoBean.setTipoAcao(tipoAcao);
                assuntoBean.setNumProcesso(numProcesso);
                assuntoBean.setParte(parte);
                assuntoBean.setTipoAssunto(tipoAssunto);
                assuntoBean.setForo(foro);
                assuntoBean.setVara(vara);
                assuntoBean.setPartePrincipal(partePrincipal);
                assuntoBean.setRamo(ramo);

                casos.add(assuntoBean);
            }
        } finally {
            conexao.closeResultBD(res);
        }

        return casos;
    }

    @Override
    protected List<ClienteBean> getClientes(TipoStatus status) throws Exception {

        List<ClienteBean> clientes = new ArrayList<ClienteBean>();

        ResultBD res = null;
        try {
            res = conexao.executeQuery("SELECT * FROM TABELA_CLIENTE_INTEGRACAO WHERE STATUS = '" + status.getStatus()
                    + "'");

            while (res.next()) {

                String codCliente = res.getString("CODIGO_CLIENTE");
                String razaoSocial = res.getString("RAZAO_SOCIAL");
                String nomeFantasia = res.getString("NOME_FANTASIA");
                String cnpj = res.getString("CNPJ");
                String tipoCliente = res.getString("TIPO_CLIENTE");
                String tipoPessoa = res.getString("TIPO_PESSOA");

                ClienteBean cliente = new ClienteBean();

                cliente.setCodCliente(codCliente);
                cliente.setNomeCliente(razaoSocial);
                cliente.setNomeFantasia(nomeFantasia);
                cliente.setCnpj(cnpj);
                cliente.setTipoCliente(tipoCliente);
                cliente.setTipoPessoa(tipoPessoa);

                clientes.add(cliente);
            }
        } finally {
            conexao.closeResultBD(res);
        }

        return clientes;
    }

    @Override
    protected boolean isBD() {

        return true;
    }

}
