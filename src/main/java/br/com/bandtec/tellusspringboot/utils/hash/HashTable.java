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
            "y","z","ç","-",
            " "
    };

    public HashTable(){
        this.hash = new ListaLigada[caracteres.length];
        for( int i = 0; i <= caracteres.length; i++ ){
            hash[i] = new ListaLigada(caracteres[i]);
        }
    }

    public int funcaoHash(String resp, int index){
        if(resp.substring(index).equals("ç")) return 26;
        if(resp.substring(index).equals("-")) return 27;
        if(resp.substring(index).equals(" ")) return 28;
        return resp.toLowerCase(Locale.ROOT).toCharArray()[index]-97;
    }

    public void insere(Responsavel value, int pos, ContratoRepository contratoRepo, String cnpj){
        hash[this.funcaoHash(value.getNome(), pos)]
                .insereNode(new ResponsavelCacheModel(
                        value.getNome()
                        , contratoRepo.countAllByFkResponsavel(value)
                        , value.getImagem()
                        , cnpj));
    }

    public void insere(Responsavel value, ContratoRepository contratoRepo){
        this.insere(value, 0, contratoRepo);
    }

    public List<ResponsavelCacheModel> retornaLista(String value, int pos){
        if(pos == 0) return hash[this.funcaoHash(value,pos)].converteLista();
        return hash[this.funcaoHash(value,pos)].filtraLista(value);
    }
}
