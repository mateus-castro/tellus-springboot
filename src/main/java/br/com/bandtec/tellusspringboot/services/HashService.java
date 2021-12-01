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

    public static List<HashFormater> hashList;

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private ContratoRepository contRepo;

    @PostConstruct
    private void postConstruct(){
        System.out.println(new Date() + " - Populando Hash Table em cache...");
        List<Escola> listaEscolas = escolaRepo.findAll();
        hashList = new ArrayList<>();
        listaEscolas.forEach((escola) -> {
            HashTable hash = new HashTable();
            List<Responsavel> listResp = new ResponsavelHandler().pegaRespsDaEscola(escola.getCnpj(), contRepo, escolaRepo);
            listResp.forEach((resp) -> hash.insere(resp, contRepo));
            hashList.add(new HashFormater(hash, escola.getCnpj()));
        });
    }
}
