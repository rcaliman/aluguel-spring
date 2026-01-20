package imoveis.aluguel.utils;

public class DocumentFormatter {

    public static String formatCpfCnpj(String document) {
        if (document == null) {
            return "";
        }

        String cleaned = document.replaceAll("[^0-9]", "");

        if (cleaned.length() == 11) {
            return formatCpf(cleaned);
        } else if (cleaned.length() == 14) {
            return formatCnpj(cleaned);
        } else {
            return document; // Return original if it doesn't match expected lengths
        }
    }

    private static String formatCpf(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static String formatCnpj(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
