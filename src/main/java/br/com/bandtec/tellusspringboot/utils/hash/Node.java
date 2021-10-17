package br.com.bandtec.tellusspringboot.utils.hash;

public class Node {
    private Integer info;
    private Node next;

    public Node(Integer info){
        this.info = info;
        this.next = null;
    }

    // GETTERS e SETTERS
    public Integer getInfo() {
        return info;
    }

    public Node getNext() {
        return next;
    }

    public void setInfo(Integer info) {
        this.info = info;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
