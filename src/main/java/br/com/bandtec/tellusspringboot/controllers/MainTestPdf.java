package br.com.bandtec.tellusspringboot.controllers;

public class MainTestPdf {

    public static void main(String[] args) {
        System.out.println("chamando metodo de gerar pdf");
        BoletoPdf boletoPdf = new BoletoPdf();
        boletoPdf.gerarPdf();
    }
}
