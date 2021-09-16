package br.com.bandtec.tellusspringboot.utils;

public class PilhaObjeto<Elemento> {

    private int topo;
    private Elemento[] pilha;

    public PilhaObjeto(int capacidade) {
        topo = -1;
        pilha = (Elemento[]) new Object[capacidade];
    }

    public boolean isEmpty() {
        return topo == -1;

    }

    public boolean isFull() {
        return topo == pilha.length - 1;
    }

    public void push(Elemento info) {
        if (!isFull()) {
            pilha[++topo] = info;

        } else {
            System.out.println("Pilha cheia");
        }
    }

    public Elemento pop() {
        if (!isEmpty()) {

            return pilha[topo--];

        }
        return null;
    }

    public Elemento peek() {
        if (!isEmpty()) {
            return pilha[topo];
        } else {
            return null;
        }
    }

    public void exibe() {
        if (isEmpty()) {
            System.out.println("Pilha vazia");
        } else {
            for (int i = 0; i <= topo; i++) {
                System.out.println(pilha[i]);
            }
        }

    }
}