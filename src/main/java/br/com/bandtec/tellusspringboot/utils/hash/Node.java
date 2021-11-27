package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;

public class Node {
    private Responsavel info;
    private Node next;

    public Node(Responsavel info){
        this.info = info;
        this.next = null;
    }

    // GETTERS e SETTERS
    public Responsavel getInfo() {
        return info;
    }

    public Node getNext() {
        return next;
    }

    public void setInfo(Responsavel info) {
        this.info = info;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
