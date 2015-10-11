package bean.padrao;

public class ClienteBean {

    public static int COD_TIPO_DOC = 1553944;

    private String codCliente;

    private String nomeCliente;

    private String nomeFantasia;

    private String cnpj;

    private String tipoCliente;

    private String tipoPessoa;

    private String situacao;

    private int codArea;

    public String getCodCliente() {

        return codCliente;
    }

    public void setCodCliente(String codCliente) {

        this.codCliente = codCliente;
    }

    public String getNomeCliente() {

        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {

        this.nomeCliente = nomeCliente;
    }

    public String getNomeFantasia() {

        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {

        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {

        return cnpj;
    }

    public void setCnpj(String cnpj) {

        this.cnpj = cnpj;
    }

    public String getTipoCliente() {

        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {

        this.tipoCliente = tipoCliente;
    }

    public String getTipoPessoa() {

        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {

        this.tipoPessoa = tipoPessoa;
    }

    public int getCodArea() {

        return codArea;
    }

    public void setCodArea(int codArea) {

        this.codArea = codArea;
    }

    public String getSituacao() {

        return situacao;
    }

    public void setSituacao(String situacao) {

        this.situacao = situacao;
    }

}
