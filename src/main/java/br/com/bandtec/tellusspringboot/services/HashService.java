package br.com.bandtec.tellusspringboot.services;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.formaters.HashFormater;
import br.com.bandtec.tellusspringboot.handlers.ResponsavelHandler;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.utils.hash.HashTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;


@Component
public class HashService {

    public static HashTable hashTable;

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private ContratoRepository contRepo;

    @PostConstruct
    private void postConstruct(){
        System.out.println(new Date() + " - Populando Hash Table em cache...");
        List<Escola> listaEscolas = escolaRepo.findAll();
        hashTable = new HashTable();
        listaEscolas.forEach((escola) -> {
            List<Responsavel> listResp = new ResponsavelHandler().pegaRespsDaEscola(escola.getCnpj(), contRepo, escolaRepo);
            listResp.forEach((resp) -> hashTable.insere(resp, contRepo, escola.getCnpj()));
        });
    }

    public void insereEmCache(Responsavel resp, Escola escola){
        if(resp == null || escola == null) throw new IllegalArgumentException("Responsavel ou Escola nulos");
        hashTable.insere(resp, contRepo, escola.getCnpj());
    }

    public void removeEmCache(Responsavel resp, Escola escola){
        if(resp == null || escola == null) throw new IllegalArgumentException("Responsavel ou Escola nulos");
        hashTable.remove(resp, escola.getCnpj());
    }

    public void retornaListaTotal(String cnpj){
        Escola escola = escolaRepo.findByCnpj(cnpj);
        if(cnpj == null) throw new IllegalArgumentException("Escola está nula");
        hashTable.retornaTotalLista(cnpj);

    }
}
