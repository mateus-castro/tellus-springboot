package br.com.bandtec.tellusspringboot.domains;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Gerente {

    @ApiModelProperty(value = "Código do gerente")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "Escola que o gerente trabalha")
    @ManyToOne
    private Escola fkEscola;

    @ApiModelProperty(value = "Nome do gerente")
//    @NotBlank
    private String nome;

    @ApiModelProperty(value = "Data de nascimento do gerente")
//    @Past
    private String dataNasc;

    @ApiModelProperty(value = "Email do gerente")
//    @Email
    private String email;

    @ApiModelProperty(value = "CPF do gerente")
//    @CPF
    private String cpf;

    @ApiModelProperty(value = "Senha do gerente")
//    @NotBlank
    private String senha;

    private String imagem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Escola getFkEscola() {
        return fkEscola;
    }

    public void setFkEscola(Escola fkEscola) {
        this.fkEscola = fkEscola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
