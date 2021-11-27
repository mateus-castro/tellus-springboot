package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.services.HashService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HashTable {
    ArrayList<ListaLigada> hash;
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
        this.hash = new ArrayList<>(caracteres.length);
        for( String letra : caracteres ){
            hash.add(new ListaLigada(letra));
        }
    }

    public boolean funcaoHash(String resp, ListaLigada lista, int index){
        String valueLower = resp.toLowerCase(Locale.ROOT);
        return valueLower.substring(0, 1).equals(lista.getHead().getInfo().getNome());
    }

    public void insere(Responsavel value, int pos){
        for ( ListaLigada index : hash ) {
            if(this.funcaoHash(value.getNome(), index, pos)) {
                index.insereNode(value);
            }
        }
    }

    public List<Responsavel> retornaLista(String value, int pos){
        for ( ListaLigada index : hash ) {
            if(this.funcaoHash(value, index, pos)){
                if(pos == 0) return index.converteLista();
                return index.filtraLista(value);
            }
            cont++;
        }
        cont = 0;
        return null;
    }
}
