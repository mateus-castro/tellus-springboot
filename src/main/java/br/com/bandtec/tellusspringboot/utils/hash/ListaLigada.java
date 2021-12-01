package br.com.bandtec.tellusspringboot.utils.hash;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.domains.ResponsavelCacheModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaLigada {
    private Node head;

    // Construtor da lista ligada que recebe um caracter para ser o seu indice
    public ListaLigada(String letra){
        ResponsavelCacheModel resp = new ResponsavelCacheModel(letra, 0, null, null, null);
        this.head = new Node(resp);
    }

    // Insere um nó no primeiro indice da lista
    public void insereNode(ResponsavelCacheModel valor){
        Node newNode = new Node(valor);
        newNode.setNext(this.head.getNext());
        this.head.setNext(newNode);
    }

    // Retorna a lista encadeada em forma de lista estática
    public List<ResponsavelCacheModel> converteLista(){
        Node atual = head.getNext();
        List<ResponsavelCacheModel> lista = new ArrayList<>(this.getTamanho());

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

    public List<ResponsavelCacheModel> filtraLista(String value, String cnpj){
        Node atual = this.head.getNext();
        ArrayList<ResponsavelCacheModel> filteredRespList = new ArrayList<>();

        while(atual != null){
            if(atual.getInfo().getNomeResp().toLowerCase(Locale.ROOT).startsWith(value.toLowerCase(Locale.ROOT)) && atual.getInfo().getCnpjEscola().equals(cnpj)) {
                filteredRespList.add(atual.getInfo());
            }
            atual = atual.getNext();
        }
        return filteredRespList;
    }

    public Node getHead() {
        return head;
    }

    public void removeNode(String cpf, String cnpj) {
        Node anterior = this.head;
        Node atual = this.head.getNext();

        while(atual != null){
            if(atual.getInfo().getCpf().equals(cpf) && atual.getInfo().getCnpjEscola().equals(cnpj)) {
                anterior.setNext(atual.getNext());
            }
            anterior = atual;
            atual = atual.getNext();
        }
    }
}
