package imoveis.aluguel.mappers;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PropertyLogMapperTest {

    private final PropertyLogMapper propertyLogMapper = Mappers.getMapper(PropertyLogMapper.class);

    @Test
    @DisplayName("Deve mapear de Property para PropertyLog corretamente")
    void shouldMapPropertyToPropertyLog() {

        Tenant tenant = new Tenant();
        tenant.setName("Inquilino Exemplo");
        tenant.setCpfCnpj("123.456.789-00");

        Property property = new Property();
        property.setTenant(tenant);
        property.setValue(1500.0);

        PropertyLog propertyLog = propertyLogMapper.toPropertyLog(property);

        assertNotNull(propertyLog);

        assertEquals("Inquilino Exemplo", propertyLog.getTenantName());
        assertEquals("123.456.789-00", propertyLog.getTenantCpfCnpj());
        assertEquals(1500.0, propertyLog.getValue());
        assertNull(propertyLog.getId());

    }
}