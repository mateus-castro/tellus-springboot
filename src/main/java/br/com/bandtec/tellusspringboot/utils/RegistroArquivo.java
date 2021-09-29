package br.com.bandtec.tellusspringboot.utils;

import br.com.bandtec.tellusspringboot.dominio.*;
import br.com.bandtec.tellusspringboot.repositorio.AlunoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

public class RegistroArquivo {

    private ResponsavelRepository respRepo;

    private AlunoRepository alunoRepo;

    private ContratoRepository contRepo;

    // TODO colocar verificações da Util em todas as partes do código que necessitam (validação de data e de cpf)
    private ArrayList<String> leArquivo(String file, Escola escola, int cont) throws IOException {
        ArrayList<String> messageList = new ArrayList<>();
        BufferedReader arquivo = new BufferedReader(new StringReader(file));
        String linha = arquivo.readLine();

        // Confirma se o arquivo tem a formataçao correta
        if (cont == 0){
            if(!linha
                    .equals("NOME_PAI;DATA_NASC_PAI;EMAIL_PAI;CPF_PAI;SENHA_PAI;TELEFONE_PAI;" +
                            "RA;NOME_ALUNO;DATA_NASC_ALUNO;SERIE_ALUNO;TURMA_ALUNO;" +
                            "VALOR;NUM_PARCELAS;DATA_FIM;DATA_INICIO")) {
                messageList.add("Arquivo com a formatação errada.");
                return messageList;
            }
            String arquivoFormat = file.replace(linha + "\r\n", "");
            leArquivo(arquivoFormat, escola, 1);
        }

        if (linha != null) {
            messageList.add(this.insereRegistro(linha, escola, cont));
            // Removendo a linha lida do arquivo
            String novoArquivo = file.replace(linha + "\r\n", "");
            leArquivo(novoArquivo, escola, ++cont);
        }

        return messageList;

    }

    public ArrayList<String> insereRegistrosDeArquivo(String file, Escola escola, ResponsavelRepository respRepo, AlunoRepository alunoRepo, ContratoRepository contRepo) throws IOException {
        this.respRepo = respRepo;
        this.alunoRepo = alunoRepo;
        this.contRepo = contRepo;
        return this.leArquivo(file, escola, 0);
    }

    private String insereRegistro(String registro, Escola escola, int nReg){
        String nome = this.separaCampo(registro);
        registro = registro.replace(nome+";", "");

        // adicionar função de formatar data
        String dataNascUnf = this.separaCampo(registro);
        String dataNasc = Util.formataData(dataNascUnf);
        registro = registro.replace(dataNascUnf+";", "");

        String email = this.separaCampo(registro);
        registro = registro.replace(email+";", "");

        // adicionar função de validar cpf
        String cpf = this.separaCampo(registro);
        if (Util.validaCpf(cpf).equals("")){
            return "Registro " + nReg + ": CPF do [Responsável] é inválido.";
        }

        registro = registro.replace(cpf+";", "");

        String senha = this.separaCampo(registro);
        registro = registro.replace(senha+";", "");

        String telefone = this.separaCampo(registro);
        registro = registro.replace(telefone+";", "");

        try{
            if(!respRepo.existsByCpf(cpf)){
                Responsavel novoResp = new Responsavel();
                novoResp.setNome(nome);
                novoResp.setDataNasc(dataNasc);
                novoResp.setEmail(email);
                novoResp.setCpf(cpf);
                novoResp.setSenha(senha);
                novoResp.setTelefone(telefone);

                respRepo.save(novoResp);
                return this.verificaAluno(novoResp, escola, registro, nReg);
            } else {
                return this.verificaAluno(respRepo.findResponsavelByCpf(cpf), escola, registro, nReg);
            }
        } catch(Exception e){
            System.out.println(e);
            return "Registro " + nReg + ": Erro ao inserir novo(a) [Responsável] no banco.";
        }
    }

    private String verificaAluno(Responsavel resp, Escola escola, String registro, int nReg){
        // Separando informações do aluno
        String ra = this.separaCampo(registro);
        registro = registro.replace(ra+";", "");

        String nome = this.separaCampo(registro);
        registro = registro.replace(nome+";", "");

        String dataNascUnf = this.separaCampo(registro);
        String dataNasc = Util.formataData(dataNascUnf);
        registro = registro.replace(dataNascUnf+";", "");

        String serie = this.separaCampo(registro);
        registro = registro.replace(serie+";", "");

        String turma = this.separaCampo(registro);
        registro = registro.replace(turma+";", "");

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

    private String criaContrato(Responsavel resp, Aluno aluno, Escola escola, String registro, int nReg){
        //DecimalFormat
        Double valor = Double.parseDouble(this.separaCampo(registro));
        registro = registro.replace(valor+";", "");

        Integer numParcelas = Integer.parseInt(this.separaCampo(registro));
        registro = registro.replace(numParcelas+";", "");

        String dtFim = this.separaCampo(registro);
        registro = registro.replace(dtFim+";", "");

        String dtInicio = this.separaCampo(registro);

        try{
            Contrato novoContrato = new Contrato();
            novoContrato.setFkAluno(aluno);
            novoContrato.setFkResponsavel(resp);
            novoContrato.setFkEscola(escola);
            novoContrato.setValor(valor);
            novoContrato.setNumParcelas(numParcelas);
            novoContrato.setDataFim(dtFim);
            novoContrato.setDataInicio(dtInicio);

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
}