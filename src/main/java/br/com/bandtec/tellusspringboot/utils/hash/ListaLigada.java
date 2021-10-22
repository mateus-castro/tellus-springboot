package br.com.bandtec.tellusspringboot.utils.hash;

import java.util.ArrayList;
import java.util.List;

public class ListaLigada {
    private Node head;

    // Construtor da lista ligada que recebe um caracter para ser o seu indice
    public ListaLigada(String letra){
        this.head = new Node(letra);
    }

    // Insere um nó no primeiro indice da lista
    public void insereNode(String valor){
        Node newNode = new Node(valor);
        newNode.setNext(this.head.getNext());
        this.head.setNext(newNode);
    }

    // Retorna a lista encadeada em forma de lista estática
    public List<String> converteLista(){
        Node atual = head.getNext();
        List<String> lista = new ArrayList<String>(this.getTamanho());

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

    public Node getHead() {
        return head;
    }
}
