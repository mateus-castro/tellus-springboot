package br.com.bandtec.tellusspringboot.utils;

public class Util {

    // TODO alterar número de caracteres máximos no registro de cpf no banco para poder contar os caracteres especiais
    public static boolean validaCpf(String cpf){
        if (cpf.matches("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})")){
            return true;
        }
        return false;
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
