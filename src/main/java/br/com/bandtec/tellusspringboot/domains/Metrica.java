package br.com.bandtec.tellusspringboot.domains;

import java.util.Date;

public class Metrica
{
    Double adesao;
    Double evasao;
    Date dataJuros;
    Double juros;
    Integer qntPago;
    Integer qntAtrasado;
    Date proxPagamento;

    public Double getAdesao() {
        return adesao;
    }

    public void setAdesao(Double adesao) {
        this.adesao = adesao;
    }

    public Double getEvasao() {
        return evasao;
    }

    public void setEvasao(Double evasao) {
        this.evasao = evasao;
    }

    public Date getDataJuros() {
        return dataJuros;
    }

    public void setDataJuros(Date dataJuros) {
        this.dataJuros = dataJuros;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public Integer getQntPago() {
        return qntPago;
    }

    public void setQntPago(Integer qntPago) {
        this.qntPago = qntPago;
    }

    public Integer getQntAtrasado() {
        return qntAtrasado;
    }

    public void setQntAtrasado(Integer qntAtrasado) {
        this.qntAtrasado = qntAtrasado;
    }

    public Date getProxPagamento() {
        return proxPagamento;
    }

    public void setProxPagamento(Date proxPagamento) {
        this.proxPagamento = proxPagamento;
    }
}
