package br.com.bandtec.tellusspringboot.utils.hash;

import java.util.ArrayList;

public class HashTable {
    ArrayList<ListaLigada> hash;
    int cont = 0;

    public HashTable(int tamVet){
        this.hash = new ArrayList<>(tamVet);
        for(int i = 0; i < tamVet; i++){
            hash.add(new ListaLigada());
        }
    }

    public int funcaoHash(int x){
        return x%hash.size();
    }

    public boolean insere(int num){
        for ( ListaLigada index : hash ) {
            if(funcaoHash(num) == cont){
                index.insereNode(num);
                cont = 0;
                return true;
            }
            cont++;
        }
        cont = 0;
        return false;
    }

    public boolean busca(int num){
        for ( ListaLigada index : hash ) {
            if(funcaoHash(num) == cont){
                if(index.buscaNode(num) != null){
                    cont = 0;
                    return true;
                }
            }
            cont++;
        }
        cont = 0;
        return false;
    }

    public boolean remove(int num){
        for ( ListaLigada index : hash ) {
            if(funcaoHash(num) == cont){
                cont = 0;
                return index.removeNode(num);
            }
            cont++;
        }
        cont = 0;
        return false;
    }
}
