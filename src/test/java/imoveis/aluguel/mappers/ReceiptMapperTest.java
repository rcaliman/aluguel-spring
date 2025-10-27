package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.enums.PropertyTypeEnum;

@SpringBootTest
class ReceiptMapperTest {

    @Autowired
    private ReceiptMapper mapper;

    @Test
    @DisplayName("Deve converter Receipt para ReceiptDtoResponse corretamente")
    void dtoResponse_ShouldMapAllFieldsCorrectly() {
        // Given
        Receipt receipt = Receipt.builder()
                .tenant("João Silva")
                .value(1500.00)
                .propertyType(PropertyTypeEnum.APARTAMENTO)
                .propertyNumber("123")
                .locale("São Paulo")
                .day("15")
                .month("outubro")
                .nextMonth("novembro")
                .observation("Referente ao aluguel")
                .year("2023")
                .nextYear("2023")
                .landlord("Maria Santos")
                .tenantContact("11999999999")
                .build();

        // When
        ReceiptDtoResponse dto = mapper.dtoResponse(receipt);

        // Then
        assertNotNull(dto);
        assertEquals("João Silva", dto.tenant());
        assertEquals("mil e quinhentos reais", dto.value());
        assertEquals(PropertyTypeEnum.APARTAMENTO, dto.propertyType());
        assertEquals("123", dto.propertyNumber());
        assertEquals("Referente ao aluguel", dto.observation());
        assertEquals("São Paulo", dto.locale());
        assertEquals("15", dto.day());
        assertEquals("outubro", dto.month());
        assertEquals("novembro", dto.nextMonth());
        assertEquals("2023", dto.year());
        assertEquals("2023", dto.nextYear());
        assertEquals("Maria Santos", dto.landlord());
        assertEquals("11999999999", dto.tenantContact());
    }

    @Test
    @DisplayName("Deve converter valor numérico para extenso corretamente")
    void dtoResponse_ShouldConvertValueToWords() {
        // Given
        Receipt receipt = Receipt.builder()
                .tenant("Pedro Costa")
                .value(2500.50)
                .propertyType(PropertyTypeEnum.SALA_COMERCIAL)
                .propertyNumber("456")
                .locale("Rio de Janeiro")
                .day("10")
                .month("novembro")
                .nextMonth("dezembro")
                .year("2023")
                .nextYear("2023")
                .landlord("Ana Paula")
                .tenantContact("21988888888")
                .build();

        // When
        ReceiptDtoResponse dto = mapper.dtoResponse(receipt);

        // Then
        assertNotNull(dto);
        assertEquals("dois mil e quinhentos reais e cinquenta centavos", dto.value());
        assertEquals("Pedro Costa", dto.tenant());
    }

    @Test
    @DisplayName("Deve converter Receipt com campos opcionais nulos")
    void dtoResponse_WithNullOptionalFields_ShouldMapCorrectly() {
        // Given
        Receipt receipt = Receipt.builder()
                .tenant("Carlos Lima")
                .value(3000.00)
                .propertyType(PropertyTypeEnum.LOJA)
                .propertyNumber("789")
                .locale("Curitiba")
                .day("20")
                .month("dezembro")
                .nextMonth("janeiro")
                .year("2023")
                .nextYear("2024")
                .landlord("Fernando Silva")
                .tenantContact("41977777777")
                .build();

        // When
        ReceiptDtoResponse dto = mapper.dtoResponse(receipt);

        // Then
        assertNotNull(dto);
        assertEquals("Carlos Lima", dto.tenant());
        assertEquals("três mil reais", dto.value());
        assertEquals(PropertyTypeEnum.LOJA, dto.propertyType());
    }

    @Test
    @DisplayName("Deve converter valores decimais complexos para extenso")
    void dtoResponse_WithComplexDecimalValue_ShouldConvertCorrectly() {
        // Given
        Receipt receipt = Receipt.builder()
                .tenant("Lucia Mendes")
                .value(1234.56)
                .propertyType(PropertyTypeEnum.QUITINETE)
                .propertyNumber("101")
                .locale("Belo Horizonte")
                .day("05")
                .month("setembro")
                .nextMonth("outubro")
                .year("2023")
                .nextYear("2023")
                .landlord("Roberto Alves")
                .tenantContact("31966666666")
                .build();

        // When
        ReceiptDtoResponse dto = mapper.dtoResponse(receipt);

        // Then
        assertNotNull(dto);
        assertEquals("mil duzentos e trinta e quatro reais e cinquenta e seis centavos", dto.value());
    }
}
