package imoveis.aluguel.utils;

import java.time.LocalDate;

public class DateUtil {

    private static final String[] MONTHS = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };

    /**
     * Retorna o nome do próximo mês dado o nome do mês atual em português. Se o mês
     * for Dezembro, retorna Janeiro.
     * 
     * @param currentMonth Nome do mês atual (ex: "Janeiro", "Dezembro")
     * @return Nome do próximo mês
     * @throws IllegalArgumentException se o mês fornecido for inválido
     */
    public static String getNextMonth(String currentMonth) {

        if (currentMonth == null || currentMonth.trim().isEmpty()) {
            throw new IllegalArgumentException("Mês não pode ser nulo ou vazio");
        }

        for (int i = 0; i < MONTHS.length; i++) {

            if (MONTHS[i].equalsIgnoreCase(currentMonth.trim())) {
                return MONTHS[(i + 1) % MONTHS.length];
            }

        }

        throw new IllegalArgumentException("Mês inválido: " + currentMonth);
    }

    /**
     * Retorna o nome do mês a partir do número (1-12).
     * 
     * @param monthNumber Número do mês (1 = Janeiro, 12 = Dezembro)
     * @return Nome do mês em português
     * @throws IllegalArgumentException se o número for inválido
     */
    public static String getMonthName(int monthNumber) {

        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException("Número do mês deve estar entre 1 e 12. Recebido: " + monthNumber);
        }

        return MONTHS[monthNumber - 1];

    }

    /**
     * Retorna o número do mês (1-12) a partir do nome.
     * 
     * @param monthName Nome do mês em português
     * @return Número do mês (1-12)
     * @throws IllegalArgumentException se o nome for inválido
     */
    public static int getMonthNumber(String monthName) {

        if (monthName == null || monthName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do mês não pode ser nulo ou vazio");
        }

        for (int i = 0; i < MONTHS.length; i++) {

            if (MONTHS[i].equalsIgnoreCase(monthName.trim())) {
                return i + 1;
            }
            
        }

        throw new IllegalArgumentException("Mês inválido: " + monthName);
    }

    /**
     * Retorna o ano atual.
     * 
     * @return Ano atual
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Retorna o próximo ano a partir do ano fornecido.
     * 
     * @param currentYear Ano atual
     * @return Próximo ano
     */
    public static int getNextYear(int currentYear) {
        return currentYear + 1;
    }

    /**
     * Retorna o próximo ano a partir do ano atual.
     * 
     * @return Próximo ano
     */
    public static int getNextYear() {
        return getNextYear(getCurrentYear());
    }

    /**
     * Retorna todos os meses do ano.
     * 
     * @return Array com os nomes de todos os meses
     */
    public static String[] getAllMonths() {
        return MONTHS.clone();
    }
}