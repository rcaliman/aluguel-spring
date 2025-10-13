package imoveis.aluguel.dtos;

import java.time.LocalDate;

public record EnergyDtoResponseList(Long id, Double counter1, Double counter2, Double counter3, Double amount1,
        Double amount2, Double amount3, Double kwhValue, Double billAmount, LocalDate date, Boolean last) {

}