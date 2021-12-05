package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.handlers.PagamentoHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.UUID;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

@RestController
@RequestMapping("/pagamento")
@Api(value = "Question")

public class PagamentoController {
    @Autowired
    private PagamentoRepository pagRepo;

    @Autowired
    private ContratoRepository contRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private ResponsavelRepository responsavelRepository;


    @ApiOperation(value = "Insere um pagamento.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pagamento inserido no banco com sucesso."),
            @ApiResponse(code = 400, message = "Requisição falhou.")
    })
    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        try {
            pagRepo.save(pagamento);
            return ResponseEntity.status(201).build();
        } catch (Error e) {
            System.out.println("[postPagamento] Erro de requisição " + e);
            return ResponseEntity.status(400).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pagamentos de um contrato.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de pagamentos."),
            @ApiResponse(code = 204, message = "Não existe nenhum gerente.")
    })
    @GetMapping
    public ResponseEntity getPagamentoByContrato(@RequestParam("ra") String raAluno) {
        try {
            Aluno aluno = alunoRepo.findAlunoByRa(raAluno);
            Contrato contrato = contRepo.findContratoByFkAluno(aluno);
            List<Pagamento> pagamentos = pagRepo.findAllByFkContrato(contrato);

            return ResponseEntity.status(200).body(pagamentos);
        } catch (Error e) {
            System.out.println(e);
            return ResponseEntity.status(400).build();
        }

    }

    @GetMapping("/download/boleto")
    @ResponseBody
    public ResponseEntity getBoleto(
            @RequestBody Pagamento pagamento, @RequestBody String cnpj, @RequestBody String cpf)
            throws FileNotFoundException {
        String nameBoleto = String.format("Boleto-462cd0f0-557e-11ec-bf63-0242ac130002.pdf");
        PagamentoHandler pagamentoHandler = new PagamentoHandler();
        pagamentoHandler.gerarBoleto(cpf, cnpj, pagamento, nameBoleto);

        File file = new File(nameBoleto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", String.format("attachment; filename=%s", nameBoleto));

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/teste-download")
    @ResponseBody
    public ResponseEntity getBoletoMockado() throws FileNotFoundException {
        PagamentoHandler pagamentoHandler = new PagamentoHandler();
        String nameBoleto = String.format("Boleto-462cd0f0-557e-11ec-bf63-0242ac130002.pdf");
        pagamentoHandler.gerarBoletoMockado(nameBoleto);
        File file = new File(nameBoleto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", String.format("attachment; filename=%s", nameBoleto));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }
}