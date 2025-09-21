package imoveis.aluguel.dtos;


public record ReceiptDtoResponse(String tenant, Double value, String propertyType, String propertyNumber, String locale, String day, String month, String year, String landlord, String tenantContact) {
    
}
