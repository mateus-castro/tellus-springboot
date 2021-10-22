package br.com.bandtec.tellusspringboot.handlers;

import br.com.bandtec.tellusspringboot.domains.Gerente;
import br.com.bandtec.tellusspringboot.repositories.GerenteRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class EscolaHandler {
    @Autowired
    private GerenteRepository gerRepo;

    public JSONObject formatGerBody(Gerente gerente, JSONObject body, JSONObject newGerente){
        newGerente.put("id", gerente.getId());
        newGerente.put("nome", gerente.getNome());
        newGerente.put("dataNasc", gerente.getDataNasc());
        newGerente.put("email", gerente.getEmail());
        newGerente.put("cpf", gerente.getCpf());
        newGerente.put("senha", gerente.getSenha());
        newGerente.put("imagem", gerente.getImagem());

        body.put("escola", gerente.getFkEscola());
        body.put("gerente", newGerente);
        return body;
    }
}
