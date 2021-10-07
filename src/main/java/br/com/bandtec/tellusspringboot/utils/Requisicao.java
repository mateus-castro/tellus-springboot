package br.com.bandtec.tellusspringboot.utils;

import br.com.bandtec.tellusspringboot.domains.Responsavel;

import java.time.LocalDateTime;

public class Requisicao {
    private String protocolo;
    private Responsavel responsavel;
    private LocalDateTime previsao;
    public static FilaObjeto<Requisicao> filaReq = new FilaObjeto<>(20);

    public Requisicao(String protocolo, Responsavel responsavel, LocalDateTime previsao) {
        this.protocolo = protocolo;
        this.responsavel = responsavel;
        this.previsao = previsao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDateTime getPrevisao() {
        return previsao;
    }

    public void setPrevisao(LocalDateTime previsao) {
        this.previsao = previsao;
    }

    public static FilaObjeto<Requisicao> getFilaReq() {
        return filaReq;
    }

    public static void setFilaReq(FilaObjeto<Requisicao> filaReq) {
        Requisicao.filaReq = filaReq;
    }
}
