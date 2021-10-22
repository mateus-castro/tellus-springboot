package br.com.bandtec.tellusspringboot.handlers;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelHandler {

    public List<Responsavel> pegaRespsDaEscola(String cnpj, ContratoRepository contRepo,EscolaRepository escolaRepo){
        Escola escola = escolaRepo.findByCnpj(cnpj);
        List<Responsavel> listaResp = new ArrayList<>();
        List<Contrato> listaContrato = contRepo.findAllByFkEscola(escola);

        for ( Contrato contrato : listaContrato ) {
            Responsavel newResp = contrato.getFkResponsavel();
            if(!listaResp.contains(newResp)) {
                listaResp.add(newResp);
            }
        }

        return listaResp;
    }

}
