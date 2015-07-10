package bean;

public class DocumentoInseridoBean {

    private int codDoc;

    private int codTipoDoc;

    private String pathDocumento;

    public DocumentoInseridoBean() {

        super();
    }

    public int getCodDoc() {

        return codDoc;
    }

    public void setCodDoc(int codDoc) {

        this.codDoc = codDoc;
    }

    public String getPathDocumento() {

        return pathDocumento;
    }

    public void setPathDocumento(String pathDocumento) {

        this.pathDocumento = pathDocumento;
    }

    public int getCodTipoDoc() {

        return codTipoDoc;
    }

    public void setCodTipoDoc(int codTipoDoc) {

        this.codTipoDoc = codTipoDoc;
    }
}
