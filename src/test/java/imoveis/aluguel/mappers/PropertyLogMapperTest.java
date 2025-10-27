package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.PropertyTypeEnum;

@SpringBootTest
class PropertyLogMapperTest {

    @Autowired
    private PropertyLogMapper mapper;

    @Test
    @DisplayName("Deve converter Property com Tenant para PropertyLog corretamente")
    void toPropertyLog_WithTenant_ShouldMapAllFieldsCorrectly() {
        // Given
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("João Silva");
        tenant.setCpfCnpj("12345678901");

        Property property = new Property();
        property.setId(1L);
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setNumber("123");
        property.setAddress("Rua A");
        property.setValue(1500.00);
        property.setComplement("Apto 101");
        property.setObservation("Observação teste");
        property.setPaymentDay("10");
        property.setTenant(tenant);

        // When
        PropertyLog propertyLog = mapper.toPropertyLog(property);

        // Then
        assertNotNull(propertyLog);
        assertNull(propertyLog.getId()); // ID deve ser ignorado
        assertEquals(property, propertyLog.getProperty());
        assertEquals(PropertyTypeEnum.APARTAMENTO, propertyLog.getPropertyType());
        assertEquals("123", propertyLog.getNumber());
        assertEquals("Rua A", propertyLog.getAddress());
        assertEquals(1500.00, propertyLog.getValue());
        assertEquals("Apto 101", propertyLog.getComplement());
        assertEquals("Observação teste", propertyLog.getObservation());
        assertEquals("10", propertyLog.getPaymentDay());
        assertEquals("João Silva", propertyLog.getTenantName());
        assertEquals("12345678901", propertyLog.getTenantCpfCnpj());
    }

    @Test
    @DisplayName("Deve converter Property sem Tenant para PropertyLog corretamente")
    void toPropertyLog_WithoutTenant_ShouldMapCorrectly() {
        // Given
        Property property = new Property();
        property.setId(2L);
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setNumber("456");
        property.setAddress("Av. B");
        property.setValue(2000.00);
        property.setTenant(null);

        // When
        PropertyLog propertyLog = mapper.toPropertyLog(property);

        // Then
        assertNotNull(propertyLog);
        assertNull(propertyLog.getId());
        assertEquals(property, propertyLog.getProperty());
        assertEquals(PropertyTypeEnum.APARTAMENTO, propertyLog.getPropertyType());
        assertEquals("456", propertyLog.getNumber());
        assertEquals("Av. B", propertyLog.getAddress());
        assertEquals(2000.00, propertyLog.getValue());
        assertNull(propertyLog.getTenantName());
        assertNull(propertyLog.getTenantCpfCnpj());
    }

    @Test
    @DisplayName("Deve converter Property com campos mínimos para PropertyLog")
    void toPropertyLog_WithMinimalFields_ShouldMapCorrectly() {
        // Given
        Property property = new Property();
        property.setId(3L);
        property.setPropertyType(PropertyTypeEnum.QUITINETE);

        // When
        PropertyLog propertyLog = mapper.toPropertyLog(property);

        // Then
        assertNotNull(propertyLog);
        assertNull(propertyLog.getId());
        assertEquals(property, propertyLog.getProperty());
        assertEquals(PropertyTypeEnum.QUITINETE, propertyLog.getPropertyType());
    }

    @Test
    @DisplayName("Deve mapear todas as propriedades de Property para PropertyLog")
    void toPropertyLog_ShouldMapAllProperties() {
        // Given
        Tenant tenant = new Tenant();
        tenant.setName("Maria Santos");
        tenant.setCpfCnpj("98765432100");

        Property property = new Property();
        property.setId(4L);
        property.setPropertyType(PropertyTypeEnum.SALA_COMERCIAL);
        property.setNumber("789");
        property.setAddress("Rua C, 100");
        property.setValue(3000.00);
        property.setComplement("Casa dos fundos");
        property.setObservation("Propriedade comercial");
        property.setPaymentDay("05");
        property.setTenant(tenant);

        // When
        PropertyLog propertyLog = mapper.toPropertyLog(property);

        // Then
        assertNotNull(propertyLog);
        assertEquals("789", propertyLog.getNumber());
        assertEquals("Rua C, 100", propertyLog.getAddress());
        assertEquals(3000.00, propertyLog.getValue());
        assertEquals("Casa dos fundos", propertyLog.getComplement());
        assertEquals("Propriedade comercial", propertyLog.getObservation());
        assertEquals("05", propertyLog.getPaymentDay());
        assertEquals("Maria Santos", propertyLog.getTenantName());
        assertEquals("98765432100", propertyLog.getTenantCpfCnpj());
    }
}
