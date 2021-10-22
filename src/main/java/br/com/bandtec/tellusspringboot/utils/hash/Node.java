package br.com.bandtec.tellusspringboot.utils.hash;

public class Node {
    private String info;
    private Node next;

    public Node(String info){
        this.info = info;
        this.next = null;
    }

    // GETTERS e SETTERS
    public String getInfo() {
        return info;
    }

    public Node getNext() {
        return next;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
