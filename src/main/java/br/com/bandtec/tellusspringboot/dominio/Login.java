package br.com.bandtec.tellusspringboot.dominio;

public class Login {
    private String cpf;
    private String senha;
    private boolean responsavel;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isResponsavel() {
        return responsavel;
    }

    public void setResponsavel(boolean responsavel) {
        this.responsavel = responsavel;
    }
}
