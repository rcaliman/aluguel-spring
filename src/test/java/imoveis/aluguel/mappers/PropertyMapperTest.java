package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class PropertyMapperTest {

    private final PropertyMapper propertyMapper = Mappers.getMapper(PropertyMapper.class);

    @Test
    void shouldMapPropertyToDto() {

        Property property = new Property();
        property.setId(1L);
        property.setAddress("Rua Teste");
        property.setTenant(new Tenant());

        PropertyDtoResponse dto = propertyMapper.toDtoResponse(property);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Rua Teste", dto.address());
        assertNotNull(dto.tenant());

    }

    @Test
    void shouldUpdateEntityFromSource() {

        Property source = new Property();
        source.setAddress("Endereço Novo");
        source.setValue(2000.0);

        Property target = new Property();
        target.setId(1L);
        target.setAddress("Endereço Antigo");

        propertyMapper.updateEntity(source, target);

        assertEquals(1L, target.getId());
        assertEquals("Endereço Novo", target.getAddress());
        assertEquals(2000.0, target.getValue());

    }
}