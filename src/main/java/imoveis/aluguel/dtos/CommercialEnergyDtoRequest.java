package imoveis.aluguel.dtos;

import java.time.LocalDate;

public record CommercialEnergyDtoRequest(Long id, Double amount1, Double amount2, Double internalCounter, Double accountValue, Double accountConsumption, LocalDate date) {
}