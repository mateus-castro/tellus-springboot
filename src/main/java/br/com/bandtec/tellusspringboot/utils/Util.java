package br.com.bandtec.tellusspringboot.utils;

public class Util {

    public static String formataCpf(String cpf){
        if (cpf.matches("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})")){
            cpf = cpf
                    .replaceAll("\\.", "")
                    .replace("-", "")
                    .replace("\\/", "");
            return cpf;
        } else if (cpf.length() == 11){
            return cpf;
        } else {
            return "";
        }
    }

    // TODO ajustar e testar esse método
    public static String formataCnpj(String cnpj){
        if (cnpj.matches("(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)")){
            cnpj = cnpj
                    .replaceAll("\\.", "")
                    .replace("-", "")
                    .replace("\\/", "");
            return cnpj;
        } else if (cnpj.length() == 11){
            return cnpj;
        } else {
            return "";
        }
    }

    public static String formataData(String data){
        if (data.matches("\\d{4}-\\d{2}-\\d{2}")){
            return data;
        } else {
            String dia = data.substring(0, 2);
            String mes = data.substring(3, 5);
            String ano = data.substring(6, 10);

            return ano + "-" + mes + "-" + dia;
        }
    }

    public static String converteSituacao(String sit){
        switch(sit){
            case "1":
                return "novo";
            case "2":
                return "encerrado";
            case "3":
                return "renovado";
            default:
                return "";
        }
    }
}
