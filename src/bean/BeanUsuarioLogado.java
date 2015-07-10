package bean;

import java.util.List;

public class BeanUsuarioLogado {

    private String conta;

    private String nome;

    private int codUsuario;

    private int codArvore;

    private int codEscritorio;

    private String authToken;

    private String email;

    private String emailDestinoOutlook;

    private int idSessao;

    private String urlEscritorio;

    private List<Integer> grupos;

    private boolean alterarSenha;

    private int codAreaCadastroDefault;

    private int codTipoDocDefaultMcOffice;

    public BeanUsuarioLogado() {

        super();
    }

    public String getConta() {

        return conta;
    }

    public void setConta(String conta) {

        this.conta = conta;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public int getCodUsuario() {

        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {

        this.codUsuario = codUsuario;
    }

    public int getCodEscritorio() {

        return codEscritorio;
    }

    public void setCodEscritorio(int codEscritorio) {

        this.codEscritorio = codEscritorio;
    }

    public String getAuthToken() {

        return authToken;
    }

    public void setAuthToken(String authToken) {

        this.authToken = authToken;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public int getIdSessao() {

        return idSessao;
    }

    public void setIdSessao(int idSessao) {

        this.idSessao = idSessao;
    }

    public String getUrlEscritorio() {

        return urlEscritorio;
    }

    public void setUrlEscritorio(String urlEscritorio) {

        this.urlEscritorio = urlEscritorio;
    }

    public boolean isAlterarSenha() {

        return alterarSenha;
    }

    public void setAlterarSenha(boolean alterarSenha) {

        this.alterarSenha = alterarSenha;
    }

    public List<Integer> getGrupos() {

        return grupos;
    }

    public void setGrupos(List<Integer> grupos) {

        this.grupos = grupos;
    }

    public int getCodAreaCadastroDefault() {

        return codAreaCadastroDefault;
    }

    public void setCodAreaCadastroDefault(int codAreaCadastroDefault) {

        this.codAreaCadastroDefault = codAreaCadastroDefault;
    }

    public int getCodArvore() {

        return codArvore;
    }

    public void setCodArvore(int codArvore) {

        this.codArvore = codArvore;
    }

    public String getEmailDestinoOutlook() {

        return emailDestinoOutlook;
    }

    public void setEmailDestinoOutlook(String emailDestinoOutlook) {

        this.emailDestinoOutlook = emailDestinoOutlook;
    }

    public int getCodTipoDocDefaultMcOffice() {

        return codTipoDocDefaultMcOffice;
    }

    public void setCodTipoDocDefaultMcOffice(int codTipoDocDefaultMcOffice) {

        this.codTipoDocDefaultMcOffice = codTipoDocDefaultMcOffice;
    }
}
