package br.com.bandtec.tellusspringboot.utils;

public class ListaObjeto<Tellus> {
    private Tellus[] vetor;
    private int nroElem;

    public ListaObjeto(int tam) {
        vetor = (Tellus[]) new Object[tam];
        nroElem = 0;
    }

    public boolean adiciona(Tellus valor) {
        if (nroElem >= vetor.length) {
            System.out.println("Lista está cheia");
            return false;
        } else {
            vetor[nroElem++] = valor;
            return true;
        }
    }

    public void exibe() {
        System.out.println("\nExibindo elementos da lista:");
        for (int i = 0; i < nroElem; i++) {
            System.out.println(vetor[i] + "\t");
        }
        System.out.println();
    }


    public int busca(String valor) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(valor)) {
                return i;
            }
        }
        return -1;
    }


    public boolean removePeloIndice(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return false;
        } else {
            for (int i = indice; i < nroElem - 1; i++) {
                vetor[i] = vetor[i + 1];
            }
            nroElem--;
            return true;
        }
    }

    public boolean removeElemento(String valor) {
        return removePeloIndice(busca(valor));
    }

    public int getTamanho() {
        return nroElem;
    }

    public Tellus getElemento(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return null;
        } else {
            return vetor[indice];
        }
    }

    public void limpa() {
        nroElem = 0;
    }

    public int getNroElem() {
        return nroElem;
    }

    public void setNroElem(int nroElem) {
        this.nroElem = nroElem;
    }

    public Tellus[] getVetor() {
        return vetor;
    }

    public void setVetor(Tellus[] vetor) {
        this.vetor = vetor;
    }
}
