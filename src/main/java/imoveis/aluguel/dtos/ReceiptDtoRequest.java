package imoveis.aluguel.dtos;

import java.util.List;

public record ReceiptDtoRequest(List<Long> propertyIds, Long landlordId, String locale, String day, String month, String year) {
    
}
