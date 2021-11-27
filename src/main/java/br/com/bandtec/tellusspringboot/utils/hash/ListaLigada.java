package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaLigada {
    private Node head;

    // Construtor da lista ligada que recebe um caracter para ser o seu indice
    public ListaLigada(String letra){
        Responsavel resp = new Responsavel();
        resp.setNome(letra);
        this.head = new Node(resp);
    }

    // Insere um nó no primeiro indice da lista
    public void insereNode(Responsavel valor){
        Node newNode = new Node(valor);
        newNode.setNext(this.head.getNext());
        this.head.setNext(newNode);
    }

    // Retorna a lista encadeada em forma de lista estática
    public List<Responsavel> converteLista(){
        Node atual = head.getNext();
        List<Responsavel> lista = new ArrayList<>(this.getTamanho());

        while(atual != null){
            lista.add(atual.getInfo());
            atual = atual.getNext();
        }

        return lista;
    }

    // Retorna o tamanho da lista ligada
    public int getTamanho(){
        Node atual = head.getNext();
        int tam = 0;

        while(atual != null){
            tam++;
            atual = atual.getNext();
        }
        return tam;
    }

    public List<Responsavel> filtraLista(String value){
        Node atual = this.head.getNext();
        ArrayList<Responsavel> filteredRespList = new ArrayList<>();

        while(atual != null){
            if(atual.getInfo().getNome().toLowerCase(Locale.ROOT).startsWith(value.toLowerCase(Locale.ROOT))) filteredRespList.add(atual.getInfo());
            atual = atual.getNext();
        }
        return filteredRespList;
    }

    public Node getHead() {
        return head;
    }
}
