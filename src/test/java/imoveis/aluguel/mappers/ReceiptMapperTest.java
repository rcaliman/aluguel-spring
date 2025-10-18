package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.enums.PropertyTypeEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptMapperTest {

    private final ReceiptMapper receiptMapper = Mappers.getMapper(ReceiptMapper.class);

    @Test
    void shouldMapReceiptToDtoResponse() {

        Receipt receipt = Receipt.builder().propertyType(PropertyTypeEnum.APARTAMENTO).tenant("Inquilino A").build();

        ReceiptDtoResponse dto = receiptMapper.dtoResponse(receipt);

        assertNotNull(dto);
        assertEquals(PropertyTypeEnum.APARTAMENTO, dto.propertyType());
        assertEquals("Inquilino A", dto.tenant());

    }
}