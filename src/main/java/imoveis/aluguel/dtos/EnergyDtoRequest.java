package imoveis.aluguel.dtos;

import java.time.LocalDate;

public record EnergyDtoRequest(Long id, Long counter1, Long counter2, Long counter3, Double kwhValue, Double billAmount, LocalDate date) {
    
}
