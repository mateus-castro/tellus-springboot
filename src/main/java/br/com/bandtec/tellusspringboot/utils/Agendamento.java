package br.com.bandtec.tellusspringboot.utils;

import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static br.com.bandtec.tellusspringboot.utils.Requisicao.filaReq;
import static br.com.bandtec.tellusspringboot.controller.UsuarioController.listaReqTratadas;

@Component
public class Agendamento {

    @Autowired
    ResponsavelRepository repository;

    @Scheduled(cron = "*/30 * * * * *")
    public void scheduleAtv() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String now = LocalDateTime.now().format(format);
        if (!filaReq.isEmpty()) {
            Requisicao req = filaReq.poll();
            repository.save(req.getResponsavel());
            listaReqTratadas.adiciona(req);
            System.out.println("Requisição " + req.getProtocolo() + " tratada - " + now);
        } else {
            System.out.println("Nenhum registro encontrado - " + now);
        }
    }
}