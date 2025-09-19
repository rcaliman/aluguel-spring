package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    ReceiptDtoResponse dtoResponse(Receipt receipt);

    Receipt toReceipt(ReceiptDtoRequest receiptDto);
    
}
