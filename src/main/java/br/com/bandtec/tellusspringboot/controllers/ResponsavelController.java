package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.handlers.ResponsavelHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/responsavel")
@Api(value = "Question")

public class ResponsavelController {

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private ContratoRepository contRepo;

    @Autowired
    private PagamentoRepository pagRepo;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getAllRespOfEscola(@RequestParam("cnpj") String cnpj) {
        if (escolaRepo.existsByCnpj(cnpj)) {
            List<Responsavel> listaResp = new ResponsavelHandler().pegaRespsDaEscola(cnpj, contRepo, escolaRepo);
            return ResponseEntity.status(200).body(listaResp);
        } else {
            System.out.println("[getAllRespOfEscola] Escola especificada não encontrada");
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/dependentes")
    public ResponseEntity getDependentesPorResp(@RequestParam("cpf") String cpf) {
        if (respRepo.existsByCpf(cpf)) {
            List<Contrato> contratoList = contRepo.findAllByFkResponsavel(respRepo.findResponsavelByCpf(cpf));
            List<Aluno> alunoList = new ArrayList<Aluno>();
            for (Contrato contrato : contratoList) {
                alunoList.add(contrato.getFkAluno());
            }
            return ResponseEntity.status(200).body(alunoList);
        }
        return ResponseEntity.status(404).body("[getDEpendentesPorResp]");
    }

    // gráfico de valor pago do contrato de responsável
    @CrossOrigin
    @GetMapping("/loadbar")
    public ResponseEntity getValorTotalDeContratoPago(@RequestParam("cpf") String cpf,
                                                      @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);
            Double res = 0.0;
            for (Pagamento pagto : listaPagtos) {
                res += pagto.getValor();
            }
            res = (100 * res) / contrato.getValor();
            return ResponseEntity.status(200).body(res);
        }
        System.out.println("[getValorTotalDeContratoPago] Parâmetros inválidos");
        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @GetMapping("/calendario")
    public ResponseEntity getCalendario(@RequestParam("cpf") String cpf,
                                        @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);


            Double valorJuros = escolaRepo.findById(contrato.getFkEscola().getId()).get().getJuros();
            List<Metrica> lista = new ArrayList<>();
            Metrica metrica = new Metrica();

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);
            LocalDate date = LocalDate.parse("31/12/2099");
            Date dataContrato = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (Pagamento pagto : listaPagtos) {
                if (pagto.getSituacao() == 1 && pagto.getDataVenc().after(dataContrato)) {
                    metrica.setJuros(valorJuros);
                    metrica.setDataJuros(pagto.getDataVenc());

                    lista.add(metrica);
                }
            }
            System.out.println();
            return ResponseEntity.status(200).body(lista);
        }
        System.out.println("[getValorTotalDeContratoPago] Parâmetros inválidos");
        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @GetMapping("/juros")
    public ResponseEntity getJuros(@RequestParam("cpf") String cpf,
                                        @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);

            Double valorJuros = escolaRepo.findById(contrato.getFkEscola().getId()).get().getJuros();
            List<Metrica> lista = new ArrayList<>();
            Metrica metrica = new Metrica();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);
            LocalDate date = LocalDate.parse("31/12/2099",formatter);
            Date dataContrato = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (Pagamento pagto : listaPagtos) {
                if (pagto.getSituacao() == 1 && pagto.getDataVenc().before(dataContrato)) {
                    metrica.setJuros(valorJuros);
                    metrica.setProxPagamento(pagto.getDataVenc());

                    lista.add(metrica);
                }
            }
            System.out.println();
            return ResponseEntity.status(200).body(lista);
        }
        System.out.println("[getValorTotalDeContratoPago] Parâmetros inválidos");
        return ResponseEntity.status(404).build();
    }
}
