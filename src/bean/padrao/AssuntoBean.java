package bean.padrao;

import java.io.File;

public class AssuntoBean {

    public static int COD_TIPO_DOC = 236;

    public static int COD_TIPO_DOC_PASTA = 4784;

    private int idUnicoAssunto;

    private String codAssunto;

    private String nomeAssunto;

    private String codCliente;

    private String parte;

    private String numProcesso;

    private String tipoAssunto;

    private String tipoAcao;

    private String foro;

    private String vara;

    private String nomeContrato;

    private String partePrincipal;

    private String ramo;

    private int codArea;

    private File arquivo;

    private String nomeCliente;

    private String codPasta;

    private String observacao;

    private int codClienteMcFile;
    
	private String codAssuntoRelacionado;
	
	private String codEmpresaRelacionado;

    public String getNomeAssunto() {

        return nomeAssunto;
    }

    public void setNomeAssunto(String nomeassunto) {

        this.nomeAssunto = nomeassunto;
    }

    public String getParte() {

        return parte;
    }

    public void setParte(String parte) {

        this.parte = parte;
    }

    public String getNumProcesso() {

        return numProcesso;
    }

    public void setNumProcesso(String numProcesso) {

        this.numProcesso = numProcesso;
    }

    public String getTipoAssunto() {

        return tipoAssunto;
    }

    public void setTipoAssunto(String tipoassunto) {

        this.tipoAssunto = tipoassunto;
    }

    public String getTipoAcao() {

        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {

        this.tipoAcao = tipoAcao;
    }

    public String getForo() {

        return foro;
    }

    public void setForo(String foro) {

        this.foro = foro;
    }

    public String getVara() {

        return vara;
    }

    public void setVara(String vara) {

        this.vara = vara;
    }

    public String getNomeContrato() {

        return nomeContrato;
    }

    public void setNomeContrato(String nomeContrato) {

        this.nomeContrato = nomeContrato;
    }

    public String getPartePrincipal() {

        return partePrincipal;
    }

    public void setPartePrincipal(String partePrincipal) {

        this.partePrincipal = partePrincipal;
    }

    public int getCodArea() {

        return codArea;
    }

    public void setCodArea(int codArea) {

        this.codArea = codArea;
    }

    public String getRamo() {

        return ramo;
    }

    public void setRamo(String ramo) {

        this.ramo = ramo;
    }

    public File getArquivo() {

        return arquivo;
    }

    public void setArquivo(File arquivo) {

        this.arquivo = arquivo;
    }

    public String getNomeCliente() {

        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {

        this.nomeCliente = nomeCliente;
    }

    public String getCodAssunto() {

        return codAssunto;
    }

    public void setCodAssunto(String codAssunto) {

        this.codAssunto = codAssunto;
    }

    public String getCodCliente() {

        return codCliente;
    }

    public void setCodCliente(String codCliente) {

        this.codCliente = codCliente;
    }

    public int getIdUnicoAssunto() {

        return idUnicoAssunto;
    }

    public void setIdUnicoAssunto(int idUnicoAssunto) {

        this.idUnicoAssunto = idUnicoAssunto;
    }

    public String getCodPasta() {

        return codPasta;
    }

    public void setCodPasta(String codPasta) {

        this.codPasta = codPasta;
    }

    public String getObservacao() {

        return observacao;
    }

    public void setObservacao(String observacao) {

        this.observacao = observacao;
    }

    public int getCodClienteMcFile() {

        return codClienteMcFile;
    }

    public void setCodClienteMcFile(int codClienteMcFile) {

        this.codClienteMcFile = codClienteMcFile;
    }

	public String getCodAssuntoRelacionado() {
		return codAssuntoRelacionado;
	}

	public void setCodAssuntoRelacionado(String codAssuntoRelacionado) {
		this.codAssuntoRelacionado = codAssuntoRelacionado;
	}

	public String getCodEmpresaRelacionado() {
		return codEmpresaRelacionado;
	}

	public void setCodEmpresaRelacionado(String codEmpresaRelacionado) {
		this.codEmpresaRelacionado = codEmpresaRelacionado;
	}
    
}
