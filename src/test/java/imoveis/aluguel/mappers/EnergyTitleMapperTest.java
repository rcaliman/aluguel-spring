
package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.EnergyTitleDtoResponse;
import imoveis.aluguel.entities.EnergyTitle;

public class EnergyTitleMapperTest {

    private EnergyTitleMapper energyTitleMapper;

    @BeforeEach
    public void setUp() {
        energyTitleMapper = Mappers.getMapper(EnergyTitleMapper.class);
    }

    @Test
    public void testToDtoResponse() {
        EnergyTitle entity = new EnergyTitle();
        entity.setId(1L);
        entity.setTitleAmount1("Title 1");
        entity.setTitleAmount2("Title 2");
        entity.setTitleAmount3("Title 3");
        entity.setTitleAmount4("Title 4");
        entity.setTitleAmount5("Title 5");

        EnergyTitleDtoResponse dtoResponse = energyTitleMapper.toDtoResponse(entity);

        assertNotNull(dtoResponse);
        assertEquals(1L, dtoResponse.id());
        assertEquals("Title 1", dtoResponse.titleAmount1());
        assertEquals("Title 2", dtoResponse.titleAmount2());
        assertEquals("Title 3", dtoResponse.titleAmount3());
        assertEquals("Title 4", dtoResponse.titleAmount4());
        assertEquals("Title 5", dtoResponse.titleAmount5());
    }
}
