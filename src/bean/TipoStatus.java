package bean;

public enum TipoStatus {

    ADICIONADO("A"), MODIFICADO("A"), REMOVIDO("A"), IMPORTADO("A");

    private String status;

    private TipoStatus(String status) {

        this.status = status;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

}
