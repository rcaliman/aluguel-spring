package imoveis.aluguel.dtos;

import java.util.List;

public record ReceiptDtoRequest(List<Long> propertyIds, Long landlordId, String month, String year) {

}
