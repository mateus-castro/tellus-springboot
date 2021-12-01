package br.com.bandtec.tellusspringboot.domains;

public class ResponsavelCacheModel {
    private String nomeResp;
    private int qtdResps;
    private String img;
    private String cnpjEscola;

    public ResponsavelCacheModel(String nomeResp, int qtdResps, String img, String cnpjEscola) {
        this.nomeResp = nomeResp;
        this.qtdResps = qtdResps;
        this.img = img;
        this.cnpjEscola = cnpjEscola;
    }

    public String getNomeResp() {
        return nomeResp;
    }

    public void setNomeResp(String nomeResp) {
        this.nomeResp = nomeResp;
    }

    public int getQtdResps() {
        return qtdResps;
    }

    public void setQtdResps(int qtdResps) {
        this.qtdResps = qtdResps;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCnpjEscola() {
        return cnpjEscola;
    }

    public void setCnpjEscola(String cnpjEscola) {
        this.cnpjEscola = cnpjEscola;
    }
}
