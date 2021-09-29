package br.com.bandtec.tellusspringboot.utils;

public class Util {

    public static String validaCpf(String cpf){
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
}
