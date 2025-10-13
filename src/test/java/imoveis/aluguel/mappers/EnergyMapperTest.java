package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.EnergyDtoResponseList;
import imoveis.aluguel.entities.Energy;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class EnergyMapperTest {

    private final EnergyMapper energyMapper = Mappers.getMapper(EnergyMapper.class);

    @Test
    void shouldMapEnergyToDtoResponseList() {

        Energy energy = new Energy();
        energy.setId(1L);
        energy.setCounter1(1000.0);

        EnergyDtoResponseList resultForLast = energyMapper.toDtoResponseList(energy, true);
        EnergyDtoResponseList resultForNotLast = energyMapper.toDtoResponseList(energy, false);

        assertNotNull(resultForLast);
        assertEquals(1L, resultForLast.id());
        assertEquals(1000.0, resultForLast.counter1());
        assertTrue(resultForLast.last());

        assertNotNull(resultForNotLast);
        assertFalse(resultForNotLast.last());

    }
}