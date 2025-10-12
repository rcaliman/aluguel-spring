package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.utils.NumberToWordsConverter;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    default ReceiptDtoResponse dtoResponse(Receipt receipt) {
        return new ReceiptDtoResponse(
                    receipt.getTenant(), 
                    NumberToWordsConverter.convert(receipt.getValue()),
                    receipt.getPropertyType().getDisplayName(), 
                    receipt.getPropertyNumber(), 
                    receipt.getComplement(), 
                    receipt.getLocale(),
                    receipt.getDay(), 
                    receipt.getMonth(), 
                    receipt.getNextMonth(), 
                    receipt.getYear(), 
                    receipt.getNextYear(),
                    receipt.getLandlord(),
                    receipt.getTenantContact()
                    );
    }

    @Mapping(target = "landlord", ignore = true)
    @Mapping(target = "propertyNumber", ignore = true)
    @Mapping(target = "propertyType", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "tenantContact", ignore = true)
    @Mapping(target = "value", ignore = true)
    @Mapping(target = "day", ignore = true)
    Receipt toReceipt(ReceiptDtoRequest receiptDto);

}
