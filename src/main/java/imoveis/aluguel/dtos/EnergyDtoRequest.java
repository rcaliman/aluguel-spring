package imoveis.aluguel.dtos;

import java.time.LocalDate;

public record EnergyDtoRequest(Long id, Double counter1, Double counter2, Double counter3, Double kwhValue, Double billAmount,
        LocalDate date) {

}
