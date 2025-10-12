package imoveis.aluguel.utils;

import java.text.DecimalFormat;

public class NumberToWordsConverter {

    private static final String[] UNIDADES = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito",
            "nove" };
    private static final String[] ESPECIAIS = { "dez", "onze", "doze", "treze", "catorze", "quinze", "dezesseis",
            "dezessete", "dezoito", "dezenove" };
    private static final String[] DEZENAS = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta",
            "oitenta", "noventa" };
    private static final String[] CENTENAS = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos",
            "seiscentos", "setecentos", "oitocentos", "novecentos" };

    public static String convert(Double valor) {

        if (valor == null || valor == 0.0) {
            return "zero reais";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String valorStr = df.format(valor);
        String[] partes = valorStr.replace(".", ",").split(",");

        long reais = Long.parseLong(partes[0]);
        int centavos = Integer.parseInt(partes[1]);

        StringBuilder extenso = new StringBuilder();

        if (reais > 0) {
            extenso.append(convertPart(reais));
            extenso.append(reais == 1 ? " real" : " reais");
        }

        if (centavos > 0) {

            if (extenso.length() > 0) {
                extenso.append(" e ");
            }
            extenso.append(convertPart((long) centavos));
            extenso.append(centavos == 1 ? " centavo" : " centavos");

        }

        return extenso.toString();
    }

    private static String convertPart(long n) {
        if (n == 0)
            return "";
        if (n == 100)
            return "cem";

        if (n >= 1000) {
            long milhar = n / 1000;
            long resto = n % 1000;
            String milharStr = (milhar == 1) ? "mil" : convertPart(milhar) + " mil";

            if (resto > 0) {
                // Debug: descomentar para verificar
                // System.out.println("DEBUG - n: " + n + ", resto: " + resto + ", resto <= 100: " + (resto <= 100) + ", resto % 100 == 0: " + (resto % 100 == 0));
                
                // Se resto <= 100 OU se o resto é uma centena exata (200, 300, etc.)
                // Usa "e" para conectar
                boolean useE = (resto <= 100) || (resto % 100 == 0);
                
                if (useE) {
                    return milharStr + " e " + convertPart(resto);
                } else {
                    return milharStr + " " + convertPart(resto);
                }
            }
            return milharStr;
        }

        if (n >= 100) {
            long centena = n / 100;
            long resto = n % 100;
            return CENTENAS[(int) centena] + (resto > 0 ? " e " + convertPart(resto) : "");
        }

        if (n >= 20) {
            long dezena = n / 10;
            long resto = n % 10;
            return DEZENAS[(int) dezena] + (resto > 0 ? " e " + convertPart(resto) : "");
        }

        if (n >= 10) {
            return ESPECIAIS[(int) (n - 10)];
        }

        return UNIDADES[(int) n];

    }

    // Método para testes
    public static void main(String[] args) {
        System.out.println("1.100,00: " + convert(1100.00));  // mil e cem reais
        System.out.println("1.050,00: " + convert(1050.00));  // mil e cinquenta reais
        System.out.println("1.001,00: " + convert(1001.00));  // mil e um reais
        System.out.println("1.200,00: " + convert(1200.00));  // mil e duzentos reais
        System.out.println("1.234,00: " + convert(1234.00));  // mil duzentos e trinta e quatro reais
        System.out.println("2.500,50: " + convert(2500.50));  // dois mil e quinhentos reais e cinquenta centavos
        System.out.println("100,00: " + convert(100.00));     // cem reais
        System.out.println("200,00: " + convert(200.00));     // duzentos reais
    }
}