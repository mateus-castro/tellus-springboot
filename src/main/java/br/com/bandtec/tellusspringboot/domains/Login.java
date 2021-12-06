package br.com.bandtec.tellusspringboot.domains;

public class Login {
    private final String email;
    private final String senha;

    public Login(String email, String senha){
        this.email = email;
        this.senha = senha;
    }

}
