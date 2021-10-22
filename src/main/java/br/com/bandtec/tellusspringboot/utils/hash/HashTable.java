package br.com.bandtec.tellusspringboot.utils.hash;

import java.util.ArrayList;
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

    public HashTable(int tamVet){
        this.hash = new ArrayList<>(tamVet);
        for( String letra : caracteres ){
            hash.add(new ListaLigada(letra));
        }
    }

    public boolean funcaoHash(String value, ListaLigada lista, int index){
        String valueLower = value.toLowerCase(Locale.ROOT);
            if (valueLower.substring(index, index+1).equals(lista.getHead().getInfo())) {
                return true;
            }
        return false;
    }

    public boolean insere(String value, int pos){
        for ( ListaLigada index : hash ) {
            if(this.funcaoHash(value, index, pos)) {
                index.insereNode(value);
                return true;
            }
        }
        return false;
    }

    public ListaLigada retornaLista(String value, int pos){
        for ( ListaLigada index : hash ) {
            if(this.funcaoHash(value, index, pos)){
                return index;
            }
            cont++;
        }
        cont = 0;
        return null;
    }
}
