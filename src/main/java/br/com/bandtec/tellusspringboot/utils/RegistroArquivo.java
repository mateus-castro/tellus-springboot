package br.com.bandtec.tellusspringboot.utils;

import br.com.bandtec.tellusspringboot.dominio.Pagamento;

import java.io.*;
import java.util.*;

public class RegistroArquivo {

    public static String leArquivo(String nomeArq) {
        BufferedReader entrada = null;
        String registro;
        String tipoRegistro;
        String id, idAluno, tipoPag, dataVencimento, situacao, idEscola;
        double valorMensalidade;
        int contRegistro = 0;

        String arquivo = "";

        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        try {
            registro = entrada.readLine();

            // TODO implementar recursividade no lucar desta iteração (while)
            while (registro != null) {
                tipoRegistro = registro.substring(0, 2);

                if (tipoRegistro.equals("00")) {
                    arquivo += "Header";
                    arquivo += "Tipo de arquivo: " + registro.substring(2, 12);
                    int idEscolaa = Integer.parseInt(registro.substring(12, 14));
                    arquivo += "ID Escola: " + idEscolaa;
                    arquivo += "Data/hora de geração do arquivo: " + registro.substring(14, 33);
                    arquivo += "Versão do layout: " + registro.substring(33, 35);
                } else if (tipoRegistro.equals("01")) {
                    arquivo += "\nTrailer";
                    int qtdRegistro = Integer.parseInt(registro.substring(2, 12));
                    if (qtdRegistro == contRegistro) {
                        arquivo += "Quantidade de registros gravados compatível com quantidade lida";
                    } else {
                        arquivo += "Quantidade de registros gravados não confere com quantidade lida";
                    }
                } else if (tipoRegistro.equals("02")) {
                    if (contRegistro == 0) {
                        arquivo += "\n%-2s %-45s %-13s %-8s %5s %-8s %-45s\n" + "ID" + "ID ALUNO" + "TIPO DE PAG" + "DATA DE VENCIMENTO" +
                                "VALOR DA MENSALIDADE" + "SITUACAO" + "ID DA ESCOLA";

                    }

                    id = registro.substring(2, 5);
                    idAluno = registro.substring(5, 50);
                    tipoPag = registro.substring(15, 61);
                    dataVencimento = registro.substring(61, 71);
                    valorMensalidade = Double.parseDouble(registro.substring(71, 79).replace(',', '.'));
                    situacao = registro.substring(79, 87);
                    idEscola = registro.substring(87, 132);

                    arquivo += "%-2s %-45s %-13s %-8s %f5.2 %-8s %-45s\n" + id + idAluno + tipoPag + dataVencimento +
                            valorMensalidade + situacao + idEscola;
                    contRegistro++;
                } else {
                    System.out.println("Tipo de registro inválido");
                }

                registro = entrada.readLine();
            }

            entrada.close();
        } catch (IOException e) {
            System.err.printf("Erro ao ler arquivo: %s.\n", e.getMessage());
        }

        return arquivo;

    }

    public static void gravaArquivo(String nomeArq, String registro) {
        BufferedWriter saida = null;
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        try {
            saida.append(registro + "\n");
            saida.close();

        } catch (IOException e) {
            System.err.printf("Erro ao gravar arquivo: %s.\n", e.getMessage());
        }
    }

    public static String leBaixaArquivo(String nomeArquivo) {
        FileReader arq = null;
        Scanner entrada = null;
        boolean deuRuim = false;
        String arquivo = "";

        nomeArquivo += ".csv";


        try {
            arq = new FileReader(nomeArquivo);
            entrada = new Scanner(arq).useDelimiter(";|\\r\\n");
        } catch (FileNotFoundException erro) {
            System.err.println("Arquivo não encontrado");
            System.exit(1);
        }


        try {

            arquivo += "ID;" + "NOME;" + "CPF;" + "DATA DE VENCIMENTO;" + "VALOR DA MENSALIDADE;" + "SITUACAO;" + "NOME DA ESCOLA\n";//TODO Arrumar os parametros pra receber o csv

            while (entrada.hasNext()) {
                Integer id = entrada.nextInt();
                String nome = entrada.next();
                String cpf = entrada.next();
                String dataVencimento = entrada.next();
                Double valorMensalidade = entrada.nextDouble();
                String situacao = entrada.next();
                String nomeEscola = entrada.next();
                arquivo += String.format("%-2s;%-45s;%-13s;%-8s;%f5.2;%-8s;%-45s\n", id, nome, cpf, dataVencimento,
                        valorMensalidade, situacao, nomeEscola);
            }
        } catch (NoSuchElementException erro) {
            System.err.println("Arquivo com problemas.");
            deuRuim = true;
        } catch (IllegalStateException erro) {
            System.err.println("Erro na leitura do arquivo.");
            deuRuim = true;
        } finally {
            entrada.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.err.println("Erro ao fechar arquivo.");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }

        return arquivo;
    }


    public static void gravaListaPagamento(List<Pagamento> lista, String nomeArq) {


        FileWriter arq = null;
        Formatter saida = null;
        boolean deuRuim = false;
        String nomeArquivo = nomeArq + ".csv";

        try {
            arq = new FileWriter(nomeArquivo, true);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.err.println("Erro ao abrir arquivo");
            System.exit(1);
        }

        try {
            for (int i = 0; i < lista.size(); i++) {
                Pagamento p = lista.get(i);
                saida.format("%s;%s;%s;%s;%2f;%s;%s%n", p.getId(), p.getFkContrato().getFkAluno().getId(), p.getTipo(), p.getDataVenc(),
                        p.getValor(), p.getSituacao(), p.getFkContrato().getFkEscola().getId()); //TODO ajustar com os dados da modelagem do banco
            }
        } catch (FormatterClosedException erro) {
            System.err.println("Erro ao gravar no arquivo");
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.err.println("Erro ao fechar arquivo.");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }
}
