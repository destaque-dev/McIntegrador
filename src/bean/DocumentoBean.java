package bean;

import java.util.List;

public class DocumentoBean {

    private String codigo;

    private String codEtapa;

    private List<CampoBean> campos;

    public DocumentoBean(List<CampoBean> campos) {

        this.campos = campos;
    }

    public List<CampoBean> getCampos() {

        return campos;
    }

    public void setCampos(List<CampoBean> campos) {

        this.campos = campos;
    }

    public String getCodigo() {

        return codigo;
    }

    public void setCodigo(String codigo) {

        this.codigo = codigo;
    }

    public String getCodEtapa() {

        return codEtapa;
    }

    public void setCodEtapa(String codEtapa) {

        this.codEtapa = codEtapa;
    }
}