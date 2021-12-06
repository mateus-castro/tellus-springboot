package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.domains.ResponsavelCacheModel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.services.HashService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HashTable {
    ListaLigada[] hash;
    int cont = 0;
    String[] caracteres = {
            "a","b","c","d",
            "e","f","g","h",
            "i","j","k","l",
            "m","n","o","p",
            "q","r","s","t",
            "u","v","w","x",
            "y","z"
    };

    public HashTable(){
        this.hash = new ListaLigada[caracteres.length];
        for( int i = 0; i < caracteres.length; i++ ){
            hash[i] = new ListaLigada(caracteres[i]);
        }
    }

    public int funcaoHash(String resp){
        return resp.toLowerCase(Locale.ROOT).toCharArray()[0]-97;
    }

    public void insere(Responsavel resp, ContratoRepository contratoRepo, String cnpj){
        hash[this.funcaoHash(resp.getNome())]
                .insereNode(new ResponsavelCacheModel(
                        resp.getNome()
                        , contratoRepo.countAllByFkResponsavel(resp)
                        , resp.getImagem()
                        , cnpj
                        , resp.getCpf()));
    }

    public List<ResponsavelCacheModel> retornaLista(String value, String cnpj){
        return hash[this.funcaoHash(value)].filtraLista(value, cnpj);
    }

    public void remove(Responsavel resp, String cnpj) {
        hash[this.funcaoHash(resp.getNome())].removeNode(resp.getCpf(), cnpj);
    }
}
