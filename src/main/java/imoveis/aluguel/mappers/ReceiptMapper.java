package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    default ReceiptDtoResponse dtoResponse(Receipt receipt) {
        return new ReceiptDtoResponse(
            receipt.getTenant(),
            receipt.getValue(), 
            receipt.getPropertyType().getDescription(), 
            receipt.getPropertyNumber(), 
            receipt.getLocale(), 
            receipt.getDay(), 
            receipt.getMonth(), 
            receipt.getYear(), 
            receipt.getLandlord(), 
            receipt.getTenantContact()
        );
    }
 

    Receipt toReceipt(ReceiptDtoRequest receiptDto);
    
}
