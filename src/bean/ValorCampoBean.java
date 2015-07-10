package bean;

/**
 * Bean descrevendo campo de um documento. É utilizado para inserçao e atualizacao de um documento.
 * Existem 3 beans que descrevem campo nesse projeto. ParametroAvancado, ValorCampoBean e CampoBean.
 * Foram copiados do McFile para utilizacao por JSon.
 * 
 * @author vinicius
 */
public class ValorCampoBean {

    private String alias;

    private Object value;

    public ValorCampoBean(String alias, Object value) {

        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public Object getValue() {

        return value;
    }

    public void setValue(Object value) {

        this.value = value;
    }
}
