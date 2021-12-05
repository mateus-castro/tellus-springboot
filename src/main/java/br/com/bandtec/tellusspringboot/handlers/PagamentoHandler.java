package br.com.bandtec.tellusspringboot.handlers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Pagamento;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class PagamentoHandler {
    private ResponsavelRepository responsavelRepository;
    private EscolaRepository escolaRepository;

    public void gerarBoleto(String cpf, String cnpj, Pagamento pagamento, String nameBoleto) {
        Responsavel responsavel = responsavelRepository.findResponsavelByCpf(cpf);
        Escola escola = escolaRepository.findByCnpj(cnpj);

        Document doc = new Document();
        try {
//            UUID uuid = UUID.randomUUID();
//            String nameBoleto = String.format("Boleto-%s.pdf", uuid);

            PdfWriter.getInstance(doc, new FileOutputStream(nameBoleto));
            doc.open();

            // Pripriedade de table, para modelar boleto
            PdfPTable linha1 = new PdfPTable(1);
            PdfPTable linha2 = new PdfPTable(2);
            PdfPTable linha3 = new PdfPTable(2);
            PdfPTable linha4 = new PdfPTable(4);
            PdfPTable linha5 = new PdfPTable(3);
            PdfPTable linha6 = new PdfPTable(2);
            PdfPTable linha7 = new PdfPTable(1);

            Font fontBody = new Font(Font.FontFamily.HELVETICA, 10.0f, Font.UNDEFINED, BaseColor.BLACK);
            Font fontBodyBold = new Font(Font.FontFamily.HELVETICA, 10.0f, Font.BOLD, BaseColor.BLACK);

            // Celulas do boleto
            PdfPCell cellCode = new PdfPCell(new Paragraph(String.format("%s", pagamento.getNumProtocolo()), fontBodyBold));
            PdfPCell cellLocalPagamento = new PdfPCell(new Paragraph(String.format("Local do Pagamento\n%s", "Qualquer banco até a data de vencimento."), fontBody));
            PdfPCell cellDataVencimento = new PdfPCell(new Paragraph(String.format("Vencimento\n%s", pagamento.getDataVenc()), fontBody));
            PdfPCell cellBeneficiario = new PdfPCell(new Paragraph(String.format("Beneficiário\n%s", escola.getRazaoSocial()), fontBody));
            PdfPCell cellAgencia = new PdfPCell(new Paragraph(String.format("Agência / Código do Beneficiário\n%s", "4218/1234567"), fontBody));
            PdfPCell cellNumDoc = new PdfPCell(new Paragraph(String.format("Número Documento\n%s", "54646"), fontBody));
            PdfPCell cellEspecieDoc = new PdfPCell(new Paragraph(String.format("Espécie Doc.\n%s", "ND"), fontBody));
            PdfPCell cellAceite = new PdfPCell(new Paragraph(String.format("Aceite\n%s", "S"), fontBody));
            PdfPCell cellNumDocumento = new PdfPCell(new Paragraph(String.format("Nosso Número\n%s", "0000089-2"), fontBody));
            PdfPCell cellCarteira = new PdfPCell(new Paragraph(String.format("Carteira\n%s", "102"), fontBody));
            PdfPCell cellEspecie = new PdfPCell(new Paragraph(String.format("Espécie\n%s", "R$"), fontBody));
            PdfPCell cellValor = new PdfPCell(new Paragraph(String.format("(=) Valor do Documento\n%s", pagamento.getValor()), fontBody));
            PdfPCell cellName = new PdfPCell(new Paragraph(String.format("Pagador\n%s", responsavel.getNome()), fontBody));
            PdfPCell cellCpf = new PdfPCell(new Paragraph(String.format("CPF\n%s", responsavel.getCpf()), fontBody));
            Image img = Image.getInstance("codebar.png");
            img.scaleAbsolute(350f, 40f);

            PdfPCell cellImage = new PdfPCell(img);
            cellImage.setPadding(1);
            cellImage.setBorderColorBottom(new BaseColor(255, 255, 255, 0));
            cellImage.setBorderColorLeft(new BaseColor(255, 255, 255, 0));
            cellImage.setBorderColorRight(new BaseColor(255, 255, 255, 0));

            // Adicionando celular a tabela
            linha1.addCell(cellCode);
            linha2.addCell(cellLocalPagamento);
            linha2.addCell(cellDataVencimento);
            linha3.addCell(cellBeneficiario);
            linha3.addCell(cellAgencia);
            linha4.addCell(cellNumDoc);
            linha4.addCell(cellEspecieDoc);
            linha4.addCell(cellAceite);
            linha4.addCell(cellNumDocumento);
            linha5.addCell(cellCarteira);
            linha5.addCell(cellEspecie);
            linha5.addCell(cellValor);
            linha6.addCell(cellName);
            linha6.addCell(cellCpf);
            linha7.addCell(cellImage);

            // Adicionando tabelas ao arquivo
            doc.add(linha1);
            doc.add(linha2);
            doc.add(linha3);
            doc.add(linha4);
            doc.add(linha5);
            doc.add(linha6);
            doc.add(linha7);

            // Fechando documento
            doc.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gerarBoletoMockado(String nameBoleto){
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(nameBoleto));
            doc.open();

            // Pripriedade de table, para modelar boleto
            PdfPTable linha1 = new PdfPTable(1);
            PdfPTable linha2 = new PdfPTable(2);
            PdfPTable linha3 = new PdfPTable(2);
            PdfPTable linha4 = new PdfPTable(4);
            PdfPTable linha5 = new PdfPTable(3);
            PdfPTable linha6 = new PdfPTable(2);
            PdfPTable linha7 = new PdfPTable(1);

            Font fontBody = new Font(Font.FontFamily.HELVETICA, 10.0f, Font.UNDEFINED, BaseColor.BLACK);
            Font fontBodyBold = new Font(Font.FontFamily.HELVETICA, 10.0f, Font.BOLD, BaseColor.BLACK);

            // Celulas do boleto
            PdfPCell cellCode = new PdfPCell(new Paragraph(String.format("%s", "36490.00019 00014.978100 00000.490482 7 88000000010990"), fontBodyBold));
            PdfPCell cellLocalPagamento = new PdfPCell(new Paragraph(String.format("Local do Pagamento\n%s", "Qualquer banco até a data de vencimento."), fontBody));
            PdfPCell cellDataVencimento = new PdfPCell(new Paragraph(String.format("Vencimento\n%s", "teste"), fontBody));
            PdfPCell cellBeneficiario = new PdfPCell(new Paragraph(String.format("Beneficiário\n%s", "teste"), fontBody));
            PdfPCell cellAgencia = new PdfPCell(new Paragraph(String.format("Agência / Código do Beneficiário\n%s", "4218/1234567"), fontBody));
            PdfPCell cellNumDoc = new PdfPCell(new Paragraph(String.format("Número Documento\n%s", "54646"), fontBody));
            PdfPCell cellEspecieDoc = new PdfPCell(new Paragraph(String.format("Espécie Doc.\n%s", "ND"), fontBody));
            PdfPCell cellAceite = new PdfPCell(new Paragraph(String.format("Aceite\n%s", "S"), fontBody));
            PdfPCell cellNumDocumento = new PdfPCell(new Paragraph(String.format("Nosso Número\n%s", "0000089-2"), fontBody));
            PdfPCell cellCarteira = new PdfPCell(new Paragraph(String.format("Carteira\n%s", "102"), fontBody));
            PdfPCell cellEspecie = new PdfPCell(new Paragraph(String.format("Espécie\n%s", "R$"), fontBody));
            PdfPCell cellValor = new PdfPCell(new Paragraph(String.format("(=) Valor do Documento\n%s", "teste"), fontBody));
            PdfPCell cellName = new PdfPCell(new Paragraph(String.format("Pagador\n%s", "teste"), fontBody));
            PdfPCell cellCpf = new PdfPCell(new Paragraph(String.format("CPF\n%s", "teste"), fontBody));
            Image img = Image.getInstance("codebar.png");
            img.scaleAbsolute(350f, 40f);

            PdfPCell cellImage = new PdfPCell(img);
            cellImage.setPadding(1);
            cellImage.setBorderColorBottom(new BaseColor(255, 255, 255, 0));
            cellImage.setBorderColorLeft(new BaseColor(255, 255, 255, 0));
            cellImage.setBorderColorRight(new BaseColor(255, 255, 255, 0));

            // Adicionando celular a tabela
            linha1.addCell(cellCode);
            linha2.addCell(cellLocalPagamento);
            linha2.addCell(cellDataVencimento);
            linha3.addCell(cellBeneficiario);
            linha3.addCell(cellAgencia);
            linha4.addCell(cellNumDoc);
            linha4.addCell(cellEspecieDoc);
            linha4.addCell(cellAceite);
            linha4.addCell(cellNumDocumento);
            linha5.addCell(cellCarteira);
            linha5.addCell(cellEspecie);
            linha5.addCell(cellValor);
            linha6.addCell(cellName);
            linha6.addCell(cellCpf);
            linha7.addCell(cellImage);

            // Adicionando tabelas ao arquivo
            doc.add(linha1);
            doc.add(linha2);
            doc.add(linha3);
            doc.add(linha4);
            doc.add(linha5);
            doc.add(linha6);
            doc.add(linha7);

            // Fechando documento
            doc.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
