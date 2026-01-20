package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;

import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.utils.NumberToWordsConverter;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

  default ReceiptDtoResponse dtoResponse(Receipt receipt) {
    return new ReceiptDtoResponse(
        receipt.getTenant(),
        receipt.getTenantDocument(),
        NumberToWordsConverter.convert(receipt.getValue()),
        receipt.getPropertyType(),
        receipt.getPropertyNumber(),
        receipt.getObservation(),
        receipt.getLocale(),
        receipt.getDay(),
        receipt.getMonth(),
        receipt.getNextMonth(),
        receipt.getYear(),
        receipt.getNextYear(),
        receipt.getLandlord(),
        receipt.getLandlordCpf(),
        receipt.getTenantContact());
  }

}
