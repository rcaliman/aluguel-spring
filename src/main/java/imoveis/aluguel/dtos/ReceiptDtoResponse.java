package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.PropertyTypeEnum;

public record ReceiptDtoResponse(String tenant, Double value, PropertyTypeEnum propertyType, String propertyNumber, String locale, String day, String month, String year, String landlord, String tenantContact) {
    
}
