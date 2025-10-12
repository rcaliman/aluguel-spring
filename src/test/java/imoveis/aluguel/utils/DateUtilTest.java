package imoveis.aluguel.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateUtilTest {

    @Test
    @DisplayName("getNextMonth - Deve retornar o próximo mês")
    void getNextMonth_ShouldReturnNextMonth() {
        assertEquals("Fevereiro", DateUtil.getNextMonth("Janeiro"));
        assertEquals("Janeiro", DateUtil.getNextMonth("Dezembro"));
    }

    @Test
    @DisplayName("getNextMonth - Deve lançar exceção para mês inválido")
    void getNextMonth_ShouldThrowExceptionForInvalidMonth() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.getNextMonth("Mês Inválido");
        });
    }

    @Test
    @DisplayName("getMonthName - Deve retornar o nome do mês")
    void getMonthName_ShouldReturnMonthName() {
        assertEquals("Janeiro", DateUtil.getMonthName(1));
        assertEquals("Dezembro", DateUtil.getMonthName(12));
    }

    @Test
    @DisplayName("getMonthName - Deve lançar exceção para número de mês inválido")
    void getMonthName_ShouldThrowExceptionForInvalidMonthNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.getMonthName(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.getMonthName(13);
        });
    }

    @Test
    @DisplayName("getMonthNumber - Deve retornar o número do mês")
    void getMonthNumber_ShouldReturnMonthNumber() {
        assertEquals(1, DateUtil.getMonthNumber("Janeiro"));
        assertEquals(12, DateUtil.getMonthNumber("Dezembro"));
    }

    @Test
    @DisplayName("getMonthNumber - Deve lançar exceção para nome de mês inválido")
    void getMonthNumber_ShouldThrowExceptionForInvalidMonthName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.getMonthNumber("Mês Inválido");
        });
    }

    @Test
    @DisplayName("getCurrentYear - Deve retornar o ano atual")
    void getCurrentYear_ShouldReturnCurrentYear() {
        assertEquals(LocalDate.now().getYear(), DateUtil.getCurrentYear());
    }

    @Test
    @DisplayName("getNextYear - Deve retornar o próximo ano")
    void getNextYear_ShouldReturnNextYear() {
        assertEquals(2026, DateUtil.getNextYear(2025));
    }

    @Test
    @DisplayName("getNextYear - Deve retornar o próximo ano a partir do atual")
    void getNextYear_FromCurrent_ShouldReturnNextYear() {
        int currentYear = LocalDate.now().getYear();
        assertEquals(currentYear + 1, DateUtil.getNextYear());
    }

    @Test
    @DisplayName("getAllMonths - Deve retornar todos os meses")
    void getAllMonths_ShouldReturnAllMonths() {
        String[] allMonths = DateUtil.getAllMonths();
        assertEquals(12, allMonths.length);
        assertEquals("Janeiro", allMonths[0]);
        assertEquals("Dezembro", allMonths[11]);
    }
}
