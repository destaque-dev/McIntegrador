package bean;

/**
 * Bean descrevendo campo de um documento. É utilizado para pesquisa.
 * Existem 3 beans que descrevem campo nesse projeto. ParametroAvancado, ValorCampoBean e CampoBean.
 * Foram copiados do McFile para utilizacao por JSon.
 * 
 * @author vinicius
 */
public class ParametroAvancado {

    private String alias;

    private String value;

    public ParametroAvancado(String alias, String value) {

        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

}
