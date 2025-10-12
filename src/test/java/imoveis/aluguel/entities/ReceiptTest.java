package imoveis.aluguel.entities;

import imoveis.aluguel.enums.PropertyTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    @Test
    @DisplayName("Deve construir um objeto Receipt com os valores corretos usando o Builder")
    void builder_ShouldCreateObjectWithCorrectValues() {

        String tenantName = "João da Silva";
        Double rentValue = 1500.50;
        String landlordName = "Sr. Locador";

        Receipt receipt = Receipt.builder().tenant(tenantName).value(rentValue).landlord(landlordName)
                .propertyType(PropertyTypeEnum.APARTAMENTO).build();

        assertNotNull(receipt);
        assertEquals(tenantName, receipt.getTenant());
        assertEquals(rentValue, receipt.getValue());
        assertEquals(landlordName, receipt.getLandlord());
        assertEquals(PropertyTypeEnum.APARTAMENTO, receipt.getPropertyType());

    }
}