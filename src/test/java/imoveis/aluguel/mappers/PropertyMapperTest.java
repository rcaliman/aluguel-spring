package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;

@SpringBootTest
class PropertyMapperTest {

    @Autowired
    private PropertyMapper mapper;

    @Test
    @DisplayName("Deve atualizar entity com dados do source, mantendo id, propertyLogs, createdAt e updatedAt")
    void updateEntity_ShouldUpdateFieldsButKeepIdPropertyLogsAndTimestamps() {
        // Given
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("João Silva");

        Property source = new Property();
        source.setId(999L); // Este ID não deve ser copiado
        source.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        source.setUseType(PropertyUseTypeEnum.RESIDENCIAL);
        source.setAddress("Rua Nova, 123");
        source.setLocation("Centro");
        source.setState("SP");
        source.setCity("São Paulo");
        source.setNumber("456");
        source.setComplement("Apto 202");
        source.setTenant(tenant);
        source.setValue(2500.00);
        source.setObservation("Apartamento reformado");
        source.setPaymentDay("15");

        Property target = new Property();
        target.setId(1L); // Este ID deve ser mantido
        target.setPropertyType(PropertyTypeEnum.LOJA);
        target.setAddress("Endereço Antigo");
        Instant originalCreatedAt = Instant.now();
        Instant originalUpdatedAt = Instant.now();
        target.setCreatedAt(originalCreatedAt);
        target.setUpdatedAt(originalUpdatedAt);

        PropertyLog log = new PropertyLog();
        target.setPropertyLogs(new ArrayList<>());
        target.getPropertyLogs().add(log); // Logs devem ser mantidos

        // When
        mapper.updateEntity(source, target);

        // Then
        assertNotNull(target);
        assertEquals(1L, target.getId()); // ID original mantido
        assertNotEquals(999L, target.getId()); // ID do source não copiado
        assertEquals(PropertyTypeEnum.APARTAMENTO, target.getPropertyType());
        assertEquals(PropertyUseTypeEnum.RESIDENCIAL, target.getUseType());
        assertEquals("Rua Nova, 123", target.getAddress());
        assertEquals("Centro", target.getLocation());
        assertEquals("SP", target.getState());
        assertEquals("São Paulo", target.getCity());
        assertEquals("456", target.getNumber());
        assertEquals("Apto 202", target.getComplement());
        assertEquals(tenant, target.getTenant());
        assertEquals(2500.00, target.getValue());
        assertEquals("Apartamento reformado", target.getObservation());
        assertEquals("15", target.getPaymentDay());
        assertEquals(1, target.getPropertyLogs().size()); // PropertyLogs mantidos
        assertEquals(originalCreatedAt, target.getCreatedAt()); // createdAt mantido
        assertEquals(originalUpdatedAt, target.getUpdatedAt()); // updatedAt mantido
    }

    @Test
    @DisplayName("Deve atualizar entity com campos nulos")
    void updateEntity_WithNullFields_ShouldUpdateCorrectly() {
        // Given
        Property source = new Property();
        source.setPropertyType(PropertyTypeEnum.SALA_COMERCIAL);
        source.setNumber("789");

        Property target = new Property();
        target.setId(2L);
        target.setPropertyType(PropertyTypeEnum.LOJA);
        target.setAddress("Endereço Antigo");
        target.setPropertyLogs(new ArrayList<>());

        // When
        mapper.updateEntity(source, target);

        // Then
        assertEquals(2L, target.getId());
        assertEquals(PropertyTypeEnum.SALA_COMERCIAL, target.getPropertyType());
        assertEquals("789", target.getNumber());
        assertEquals(0, target.getPropertyLogs().size());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos exceto id, propertyLogs, createdAt e updatedAt")
    void updateEntity_ShouldUpdateAllFieldsExceptIgnored() {
        // Given
        Tenant newTenant = new Tenant();
        newTenant.setId(2L);
        newTenant.setName("Maria Santos");

        Property source = new Property();
        source.setPropertyType(PropertyTypeEnum.CONDOMINIO);
        source.setUseType(PropertyUseTypeEnum.COMERCIAL);
        source.setAddress("Av. Paulista, 1000");
        source.setLocation("Bela Vista");
        source.setState("SP");
        source.setCity("São Paulo");
        source.setNumber("1000");
        source.setComplement("Torre A");
        source.setTenant(newTenant);
        source.setValue(5000.00);
        source.setObservation("Sala comercial");
        source.setPaymentDay("05");

        Property target = new Property();
        target.setId(3L);
        Instant createdAt = Instant.now();
        target.setCreatedAt(createdAt);

        // When
        mapper.updateEntity(source, target);

        // Then
        assertEquals(3L, target.getId());
        assertEquals(PropertyTypeEnum.CONDOMINIO, target.getPropertyType());
        assertEquals(PropertyUseTypeEnum.COMERCIAL, target.getUseType());
        assertEquals("Av. Paulista, 1000", target.getAddress());
        assertEquals("Bela Vista", target.getLocation());
        assertEquals("SP", target.getState());
        assertEquals("São Paulo", target.getCity());
        assertEquals("1000", target.getNumber());
        assertEquals("Torre A", target.getComplement());
        assertEquals(newTenant, target.getTenant());
        assertEquals(5000.00, target.getValue());
        assertEquals("Sala comercial", target.getObservation());
        assertEquals("05", target.getPaymentDay());
        assertEquals(createdAt, target.getCreatedAt());
    }
}
