package br.com.bandtec.tellusspringboot.utils;

import br.com.bandtec.tellusspringboot.dominio.*;
import br.com.bandtec.tellusspringboot.repositorio.AlunoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.lang.reflect.Array;
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
        registro = registro.replace(dataNasc+";", "");

        String email = this.separaCampo(registro);
        registro = registro.replace(email+";", "");

        // adicionar função de validar cpf
        String cpf = this.separaCampo(registro);
        if (!Util.validaCpf(cpf)){
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

        String dataNasc = this.separaCampo(registro);
        registro = registro.replace(dataNasc+";", "");

        String serie = this.separaCampo(registro);
        registro = registro.replace(serie+";", "");

        String turma = this.separaCampo(registro);
        registro = registro.replace(turma+";", "");

        try {
            if(!alunoRepo.findAlunoByRa(ra)) {
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





//        registro = entrada.readLine();


//        try {
//            registro = entrada.readLine();
//
//            // TODO implementar recursividade no lucar desta iteração (while)
//            while (registro != null) {
//                tipoRegistro = registro.substring(0, 2);
//
//                if (tipoRegistro.equals("00")) {
//                    arquivo += "Header";
//                    arquivo += "Tipo de arquivo: " + registro.substring(2, 12);
//                    int idEscolaa = Integer.parseInt(registro.substring(12, 14));
//                    arquivo += "ID Escola: " + idEscolaa;
//                    arquivo += "Data/hora de geração do arquivo: " + registro.substring(14, 33);
//                    arquivo += "Versão do layout: " + registro.substring(33, 35);
//                } else if (tipoRegistro.equals("01")) {
//                    arquivo += "\nTrailer";
//                    int qtdRegistro = Integer.parseInt(registro.substring(2, 12));
//                    if (qtdRegistro == contRegistro) {
//                        arquivo += "Quantidade de registros gravados compatível com quantidade lida";
//                    } else {
//                        arquivo += "Quantidade de registros gravados não confere com quantidade lida";
//                    }
//                } else if (tipoRegistro.equals("02")) {
//                    if (contRegistro == 0) {
//                        arquivo += "\n%-2s %-45s %-13s %-8s %5s %-8s %-45s\n" + "ID" + "ID ALUNO" + "TIPO DE PAG" + "DATA DE VENCIMENTO" +
//                                "VALOR DA MENSALIDADE" + "SITUACAO" + "ID DA ESCOLA";
//
//                    }
//
//                    id = registro.substring(2, 5);
//                    idAluno = registro.substring(5, 50);
//                    tipoPag = registro.substring(15, 61);
//                    dataVencimento = registro.substring(61, 71);
//                    valorMensalidade = Double.parseDouble(registro.substring(71, 79).replace(',', '.'));
//                    situacao = registro.substring(79, 87);
//                    idEscola = registro.substring(87, 132);
//
//                    arquivo += "%-2s %-45s %-13s %-8s %f5.2 %-8s %-45s\n" + id + idAluno + tipoPag + dataVencimento +
//                            valorMensalidade + situacao + idEscola;
//                    contRegistro++;
//                } else {
//                    System.out.println("Tipo de registro inválido");
//                }
//
//                registro = entrada.readLine();
//            }
//
//            entrada.close();
//        } catch (IOException e) {
//            System.err.printf("Erro ao ler arquivo: %s.\n", e.getMessage());
//        }



