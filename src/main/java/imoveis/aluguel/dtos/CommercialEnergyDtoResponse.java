package imoveis.aluguel.dtos;

import java.time.LocalDate;

public record CommercialEnergyDtoResponse(
    
    Long id, 
    LocalDate date, 
    Double amount1, 
    Double amount2, 
    Double internalCounter, 
    Double accountValue, 
    Double accountConsumption, 
    Double calculatedConsumption1, 
    Double calculatedConsumption2, 
    Boolean isLast) {

}
