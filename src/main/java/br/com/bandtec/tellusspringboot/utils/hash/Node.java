package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.domains.ResponsavelCacheModel;

public class Node {
    private ResponsavelCacheModel info;
    private Node next;

    public Node(ResponsavelCacheModel info){
        this.info = info;
        this.next = null;
    }

    // GETTERS e SETTERS
    public ResponsavelCacheModel getInfo() {
        return info;
    }

    public Node getNext() {
        return next;
    }

    public void setInfo(ResponsavelCacheModel info) {
        this.info = info;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
