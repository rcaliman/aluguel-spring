package imoveis.aluguel.dtos;

public record ReceiptDtoResponse(
        String tenant, 
        String value, 
        String propertyType, 
        String propertyNumber, 
        String observation,
        String locale,
        String day, 
        String month, 
        String nextMonth, 
        String year, 
        String nextYear,
        String landlord, 
        String tenantContact
        ) {

}
