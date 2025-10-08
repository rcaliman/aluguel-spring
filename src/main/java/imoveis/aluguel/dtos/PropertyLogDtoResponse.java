package imoveis.aluguel.dtos;

import java.time.Instant;

import imoveis.aluguel.enums.PropertyTypeEnum;

public record PropertyLogDtoResponse(PropertyTypeEnum propertyType, String number, Double value, String paymentDay, String tenantName, Instant createdAt) {
    
}
