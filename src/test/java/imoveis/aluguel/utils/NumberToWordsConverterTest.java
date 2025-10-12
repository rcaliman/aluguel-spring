package imoveis.aluguel.utils;



import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;



class NumberToWordsConverterTest {



    @Test

    @DisplayName("Deve retornar 'zero reais' para valor nulo ou zero")

    void convert_NullAndZero() {

        assertEquals("zero reais", NumberToWordsConverter.convert(null));

        assertEquals("zero reais", NumberToWordsConverter.convert(0.0));

    }



    @Test

    @DisplayName("Deve converter valores com apenas centavos")

    void convert_CentsOnly() {

        assertEquals("um centavo", NumberToWordsConverter.convert(0.01));

        assertEquals("cinquenta centavos", NumberToWordsConverter.convert(0.50));

        assertEquals("noventa e nove centavos", NumberToWordsConverter.convert(0.99));

    }



    @Test

    @DisplayName("Deve converter valores redondos de reais")

    void convert_ReaisOnly() {

        assertEquals("um real", NumberToWordsConverter.convert(1.00));

        assertEquals("dois reais", NumberToWordsConverter.convert(2.00));

    }



    @Test

    @DisplayName("Deve converter valores com reais e centavos")

    void convert_ReaisAndCents() {

        assertEquals("um real e um centavo", NumberToWordsConverter.convert(1.01));

        assertEquals("dezenove reais e noventa e nove centavos",

                NumberToWordsConverter.convert(19.99));

    }



    @Test

    @DisplayName("Deve converter valores na casa das dezenas")

    void convert_Tens() {

        assertEquals("dez reais", NumberToWordsConverter.convert(10.00));

        assertEquals("quinze reais", NumberToWordsConverter.convert(15.00));

        assertEquals("vinte reais", NumberToWordsConverter.convert(20.00));

        // --- CORREÇÃO 3: O valor esperado no teste estava incorreto ---

        assertEquals("trinta e cinco reais e quarenta centavos",

                NumberToWordsConverter.convert(35.40));

    }



    @Test

    @DisplayName("Deve converter valores na casa das centenas")

    void convert_Hundreds() {

        assertEquals("cem reais", NumberToWordsConverter.convert(100.00));

        assertEquals("cento e um reais", NumberToWordsConverter.convert(101.00));

        assertEquals("cento e dez reais", NumberToWordsConverter.convert(110.00));

        assertEquals("duzentos e trinta e quatro reais e cinquenta e seis centavos",

                NumberToWordsConverter.convert(234.56));

        assertEquals("novecentos e noventa e nove reais", NumberToWordsConverter.convert(999.00));

    }



    @Test

    @DisplayName("Deve converter valores na casa dos milhares")

    void convert_Thousands() {

        assertEquals("mil reais", NumberToWordsConverter.convert(1000.00));

        assertEquals("mil e um reais", NumberToWordsConverter.convert(1001.00));

        assertEquals("mil e cem reais", NumberToWordsConverter.convert(1100.00));

        assertEquals("dois mil reais", NumberToWordsConverter.convert(2000.00));

        assertEquals("dez mil e quinhentos reais", NumberToWordsConverter.convert(10500.00));

                assertEquals("quinze mil seiscentos e setenta e oito reais e noventa centavos",

                        NumberToWordsConverter.convert(15678.90));

    }



    @Test

    @DisplayName("Deve converter um valor complexo e grande")

    void convert_LargeComplexNumber() {

        Double valor = 123456.78;

        String esperado = "cento e vinte e três mil quatrocentos e cinquenta e seis reais e setenta e oito centavos";

        assertEquals(esperado, NumberToWordsConverter.convert(valor));

    }

}
