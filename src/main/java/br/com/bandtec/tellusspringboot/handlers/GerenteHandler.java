package br.com.bandtec.tellusspringboot.handlers;


import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.formaters.HashFormater;
import br.com.bandtec.tellusspringboot.repositories.AlunoRepository;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.services.HashService;
import br.com.bandtec.tellusspringboot.utils.Agendamento;
import br.com.bandtec.tellusspringboot.utils.Util;
import br.com.bandtec.tellusspringboot.utils.hash.HashTable;
import br.com.bandtec.tellusspringboot.utils.hash.ListaLigada;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class GerenteHandler {

    private ResponsavelRepository respRepo;

    private AlunoRepository alunoRepo;

    private ContratoRepository contRepo;

    // Função recursiva que realiza a leitura dos registros
    private ArrayList<String> leArquivo(String file, Escola escola, int cont) throws IOException {
        ArrayList<String> messageList = new ArrayList<>();
        BufferedReader arquivo = new BufferedReader(new StringReader(file));
        String linha = arquivo.readLine();

        // Confirma se o arquivo tem a formataçao correta
        if (cont == 0){
            if(!linha
                    .equals("NOME_PAI;DATA_NASC_PAI;EMAIL_PAI;CPF_PAI;SENHA_PAI;TELEFONE_PAI;RA;NOME_ALUNO;DATA_NASC_ALUNO;SERIE_ALUNO;TURMA_ALUNO;VALOR;NUM_PARCELAS;DATA_INICIO;DATA_FIM;SITUACAO")) {
                messageList.add("Arquivo com a formatação errada.");
                return messageList;
            }
            String arquivoFormat = file.replace(linha + "\r\n", "");
            leArquivo(arquivoFormat, escola, 1);
        }

        // Se existir um próximo registro, a função lê
        if (linha != null) {
            messageList.add(this.insereRegistro(linha, escola, cont));
            // Removendo a linha lida do arquivo
            String novoArquivo = file.replace(linha + "\r\n", "");
            leArquivo(novoArquivo, escola, ++cont);
        }

        // Caso contrário, a lista de mensagens é retornada
        return messageList;

    }

    // Chama a função recursiva pela primeira vez e atribui os repositórios vindo da classe controller
    public ArrayList<String> insereRegistrosDeArquivo(String file, Escola escola, ResponsavelRepository respRepo, AlunoRepository alunoRepo, ContratoRepository contRepo) throws IOException {
        this.respRepo = respRepo;
        this.alunoRepo = alunoRepo;
        this.contRepo = contRepo;
        return this.leArquivo(file, escola, 0);
    }

    // Inicia a verificação do algoritmo pelo registro do responsável
    private String insereRegistro(String registro, Escola escola, int nReg){
        // Separando informações do responsavel e excluíndo da linha
        String nome = this.separaCampo(registro);
        registro = registro.replaceFirst(nome+";", "");

        String dataNascUnf = this.separaCampo(registro);
        String dataNasc = Util.formataData(dataNascUnf);
        registro = registro.replaceFirst(dataNascUnf+";", "");

        String email = this.separaCampo(registro);
        registro = registro.replaceFirst(email+";", "");

        String cpf = this.separaCampo(registro);
        String formattedCpf = Util.formataCpf(cpf);
        if (formattedCpf.equals("")){
            return "Registro " + nReg + ": CPF do [Responsável] é inválido.";
        }
        registro = registro.replaceFirst(cpf+";", "");

        String senha = this.separaCampo(registro);
        registro = registro.replaceFirst(senha+";", "");

        String telefone = this.separaCampo(registro);
        registro = registro.replaceFirst(telefone+";", "");

        try{
            // Caso o responsável seja válido e não exista no banco, insere seus dados
            if(!respRepo.existsByCpf(formattedCpf)){
                Responsavel novoResp = new Responsavel();
                novoResp.setNome(nome);
                novoResp.setDataNasc(dataNasc);
                novoResp.setEmail(email);
                novoResp.setCpf(formattedCpf);
                novoResp.setSenha(senha);
                novoResp.setTelefone(telefone);

                respRepo.save(novoResp);
                return this.verificaAluno(novoResp, escola, registro, nReg);
            } else {
                // Caso contrário, a próxima função é chamada
                return this.verificaAluno(respRepo.findResponsavelByCpf(cpf), escola, registro, nReg);
            }
        } catch(Exception e){
            System.out.println(e);
            return "Registro " + nReg + ": Erro ao inserir novo(a) [Responsável] no banco.";
        }
    }

    // Inicia a verificação dos registros do aluno
    private String verificaAluno(Responsavel resp, Escola escola, String registro, int nReg){
        // Separando informações do aluno e excluíndo da linha
        String ra = this.separaCampo(registro);
        registro = registro.replaceFirst(ra+";", "");

        String nome = this.separaCampo(registro);
        registro = registro.replaceFirst(nome+";", "");

        String dataNascUnf = this.separaCampo(registro);
        String dataNasc = Util.formataData(dataNascUnf);
        registro = registro.replaceFirst(dataNascUnf+";", "");

        String serie = this.separaCampo(registro);
        registro = registro.replaceFirst(serie+";", "");

        String turma = this.separaCampo(registro);
        registro = registro.replaceFirst(turma+";", "");

        try {
            if(!alunoRepo.existsAlunoByRa(ra)) {
                Aluno novoAluno = new Aluno();
                novoAluno.setRa(ra);
                novoAluno.setNome(nome);
                novoAluno.setDataNasc(dataNasc);
                novoAluno.setSerie(serie);
                novoAluno.setTurma(turma);

                alunoRepo.save(novoAluno);

                return this.criaContrato(resp, novoAluno, escola, registro, nReg);
            } else {
                return "Registro " + nReg + ": [Aluno(a)] já foi cadastrado(a) no banco.";
            }
        } catch (Exception e){
            System.out.println(e);
            return "Registro " + nReg + ": Erro ao inserir novo(a) [Aluno(a)] no banco.";
        }
    }

    private String criaContrato(Responsavel resp, Aluno aluno, Escola escola, String registro, int nReg) throws ParseException {
        String valorString = this.separaCampo(registro);
        Double valor = Double.parseDouble(valorString.replace(",", "."));
        registro = registro.replaceFirst(valorString+";", "");

        Integer numParcelas = Integer.parseInt(this.separaCampo(registro));
        registro = registro.replaceFirst(numParcelas+";", "");

        String dataInicioUnf = this.separaCampo(registro);
        String dataInicio = Util.formataData(dataInicioUnf);
        registro = registro.replaceFirst(dataInicioUnf+";", "");

        String dataFimUnf = this.separaCampo(registro);
        String dataFim = Util.formataData(dataFimUnf);
        registro = registro.replaceFirst(dataFimUnf+";", "");

        String situacao = Util.converteSituacao(registro);

        try{
            Contrato novoContrato = new Contrato();
            novoContrato.setFkAluno(aluno);
            novoContrato.setFkResponsavel(resp);
            novoContrato.setFkEscola(escola);
            novoContrato.setValor(valor);
            novoContrato.setNumParcelas(numParcelas);
            novoContrato.setDataFim(dataFim);
            novoContrato.setDataInicio(dataInicio);
            novoContrato.setSituacao(situacao);

            contRepo.save(novoContrato);
            return "";
        } catch (Exception e){
            System.out.println(e);
            return "Registro " + nReg + ": Erro ao inserir novo [Contrato] no banco.";
        }
    }

    private String separaCampo(String registro){
        return registro.substring(0, registro.indexOf(";"));
    }

    public List<Responsavel> pesquisaHash(String value, int pos, String cnpj, ContratoRepository contRepo, EscolaRepository escolaRepo) {
        List<HashFormater> lista = HashService.hashList.stream().filter((item) -> item.getCnpj().equals(cnpj)).collect(Collectors.toList());
        if(lista.size() < 1) throw new NoSuchElementException();
        HashTable tabela = lista.get(0).getHash();
        if(value.length() >= 2){
            return this.pesquisaHash(value, tabela, 1);
        } else {
            return tabela.retornaLista(value, pos);
        }
    }

    public List<Responsavel> pesquisaHash(String value, HashTable tabela, int pos){
        if(value.length() == pos+1) {
            return tabela.retornaLista(value, pos);
        }else{
            return pesquisaHash(value, tabela, pos+1);
        }

    }
}