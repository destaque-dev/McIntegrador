package bean;

public enum TipoStatus {

    ADICIONADO("A"), MODIFICADO("M"), REMOVIDO("R"), IMPORTADO("I");

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
