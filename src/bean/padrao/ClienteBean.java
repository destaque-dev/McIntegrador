package bean.padrao;

public class ClienteBean {

    public static int COD_TIPO_DOC = 1553944;

    private int codCliente;

    private String nomeCliente;

    private String nomeFantasia;

    private String cnpj;

    private String tipoCliente;

    private String tipoPessoa;

    public int getCodCliente() {

        return codCliente;
    }

    public void setCodCliente(int codCliente) {

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

}
