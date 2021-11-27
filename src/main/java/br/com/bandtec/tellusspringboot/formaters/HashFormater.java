package br.com.bandtec.tellusspringboot.formaters;

import br.com.bandtec.tellusspringboot.utils.hash.HashTable;

public class HashFormater {
    private HashTable hash;
    private String cnpj;

    public HashFormater(HashTable hash, String cnpj){
        this.hash = hash;
        this.cnpj = cnpj;
    }

    public HashTable getHash() {
        return hash;
    }

    public void setHash(HashTable hash) {
        this.hash = hash;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
