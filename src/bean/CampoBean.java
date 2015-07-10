package bean;

/**
 * Bean descrevendo Campo de um documento. É retornado quando é efetuada uma pesquisa no McFile via
 * McFile API
 * Existem 3 beans que descrevem campo nesse projeto. ParametroAvancado, ValorCampoBean e CampoBean.
 * Foram copiados do McFile para utilizacao por JSon.
 * 
 * @author vinicius
 */
public class CampoBean {

    private int codigo;

    private int codCampoRelac;

    private String nome;

    private String alias;

    private String rotulo;

    private Object valor;

    public CampoBean(String nome, Object valor) {

        this.nome = nome;
        this.valor = valor;
    }

    public CampoBean(int codigo, int codCampoRelac, String nome, String rotulo) {

        this.codigo = codigo;
        this.codCampoRelac = codCampoRelac;
        this.nome = nome;
        this.rotulo = rotulo;
    }

    public CampoBean(int codigo, int codCampoRelac, String nome, String rotulo, String alias) {

        this(codigo, codCampoRelac, nome, rotulo);
        this.alias = alias;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public Object getValor() {

        return valor;
    }

    public void setValor(Object valor) {

        this.valor = valor;
    }

    public String getRotulo() {

        return rotulo;
    }

    public void setRotulo(String rotulo) {

        this.rotulo = rotulo;
    }

    public int getCodigo() {

        return codigo;
    }

    public void setCodigo(int codigo) {

        this.codigo = codigo;
    }

    public int getCodCampoRelac() {

        return codCampoRelac;
    }

    public void setCodCampoRelac(int codCampoRelac) {

        this.codCampoRelac = codCampoRelac;
    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }
}
