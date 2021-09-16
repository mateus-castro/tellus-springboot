package br.com.bandtec.tellusspringboot.dominio;

import javax.persistence.*;

@Entity
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Escola fkEscola;

    @ManyToOne
    private Responsavel fkResponsavel;

    @OneToOne
    private Aluno fkAluno;
    private Double mensalidade;
    private String dataFim;
    private String dataInicio;

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

    public Responsavel getFkResponsavel() {
        return fkResponsavel;
    }

    public void setFkResponsavel(Responsavel fkResponsavel) {
        this.fkResponsavel = fkResponsavel;
    }

    public Aluno getFkAluno() {
        return fkAluno;
    }

    public void setFkAluno(Aluno fkAluno) {
        this.fkAluno = fkAluno;
    }

    public Double getMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(Double mensalidade) {
        this.mensalidade = mensalidade;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
}
