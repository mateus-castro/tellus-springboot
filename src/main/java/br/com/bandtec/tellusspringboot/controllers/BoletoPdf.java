package br.com.bandtec.tellusspringboot.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BoletoPdf {

    public BoletoPdf() {
    }

    public String gerarPdf() {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("boleto.pdf"));
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
            PdfPCell cellDataVencimento = new PdfPCell(new Paragraph(String.format("Vencimento\n%s", "23/12/2021"), fontBody));
            PdfPCell cellBeneficiario = new PdfPCell(new Paragraph(String.format("Beneficiário\n%s", "Tellus"), fontBody));
            PdfPCell cellAgencia = new PdfPCell(new Paragraph(String.format("Agência / Código do Beneficiário\n%s", "4218/1234567"), fontBody));
            PdfPCell cellNumDoc = new PdfPCell(new Paragraph(String.format("Número Documento\n%s", "54646"), fontBody));
            PdfPCell cellEspecieDoc = new PdfPCell(new Paragraph(String.format("Espécie Doc.\n%s", "ND"), fontBody));
            PdfPCell cellAceite = new PdfPCell(new Paragraph(String.format("Aceite\n%s", "S"), fontBody));
            PdfPCell cellNumDocumento = new PdfPCell(new Paragraph(String.format("Nosso Número\n%s", "0000089-2"), fontBody));
            PdfPCell cellCarteira = new PdfPCell(new Paragraph(String.format("Carteira\n%s", "102"), fontBody));
            PdfPCell cellEspecie = new PdfPCell(new Paragraph(String.format("Espécie\n%s", "R$"), fontBody));
            PdfPCell cellValor = new PdfPCell(new Paragraph(String.format("(=) Valor do Documento\n%s", "950,00"), fontBody));
            PdfPCell cellName = new PdfPCell(new Paragraph(String.format("Pagador\n%s", "Lucas Mikelvin"), fontBody));
            PdfPCell cellCpf = new PdfPCell(new Paragraph(String.format("CPF\n%s", "496.683.528-31"), fontBody));
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

            // Abre documento
            Desktop.getDesktop().open(new File("boleto.pdf"));
            return "success!";
        } catch (DocumentException de) {
            return de.getMessage();
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
