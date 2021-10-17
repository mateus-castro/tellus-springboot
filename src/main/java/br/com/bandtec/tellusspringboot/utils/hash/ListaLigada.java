package br.com.bandtec.tellusspringboot.utils.hash;

public class ListaLigada {
    private Node head;

    public ListaLigada(){
        this.head = new Node(0);
    }

    // TODO Insere um nó no primeiro indice da lista
    public void insereNode(int valor){
        Node newNode = new Node(valor);
        newNode.setNext(this.head.getNext());
        this.head.setNext(newNode);
    }

    // TODO Exibe a lista encadeada
    public void exibe(){
        Node atual = head.getNext();

        while(atual != null){
            System.out.println(atual.getInfo());
            atual = atual.getNext();
        }
    }

    // TODO Percorre a lista procurando se existe um valor igual ao parâmetro
    public Node buscaNode(int valor){
        Node atual = head.getNext();

        while(atual != null){
            if(atual.getInfo() == valor){
                return atual;
            }
            atual = atual.getNext();
        }

        return null;
    }

    public boolean removeNode(int valor){
        Node atual = head.getNext();
        Node anterior = head;

        while(atual != null){
            if(atual.getInfo() == valor) {
                anterior.setNext(atual.getNext());
                return true;
            }
            anterior = atual;
            atual = atual.getNext();
        }

        return false;
    }

    public int getTamanho(){
        Node atual = head.getNext();
        int tam = 0;

        while(atual != null){
            tam++;
            atual = atual.getNext();
        }

        return tam;
    }

    // GETTER e SETTER
    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }
}
