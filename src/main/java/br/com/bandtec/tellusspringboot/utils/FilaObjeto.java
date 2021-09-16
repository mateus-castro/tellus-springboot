package br.com.bandtec.tellusspringboot.utils;

public class FilaObjeto<T> {

    private int tamanho;
    private T[] fila;

    public FilaObjeto(int capacidade) {
        tamanho = 0;
        fila = (T[]) new Object[capacidade];
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {
        if (isFull()) {
            System.out.println("Fila cheia");
        } else {
            fila[tamanho++] = info;
        }
    }

    public T peek() {
        return fila[0];
    }

    public T poll() {
        T primeiro = peek();

        if (!isEmpty()) {

            for (int i = 0; i < tamanho - 1; i++) {
                fila[i] = fila[i + 1];
            }

            fila[tamanho - 1] = null;

            tamanho--;
        }

        return primeiro;
    }

    public void exibe() {
        if (isEmpty()) {
            System.out.println("Fila vazia");
        } else {
            for (int i = 0; i < tamanho; i++) {
                System.out.println(fila[i]);
            }
        }
    }
}
