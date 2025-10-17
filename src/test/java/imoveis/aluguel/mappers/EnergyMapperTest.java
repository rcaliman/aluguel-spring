package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

class EnergyMapperTest {

    private final EnergyMapper energyMapper = Mappers.getMapper(EnergyMapper.class);

    @Test
    void shouldMapEnergyToDtoResponseList() {

        Energy energy = new Energy();
        energy.setId(1L);
        energy.setCounter1(1000.0);

        EnergyDtoResponse resultForLast = energyMapper.toDtoResponse(energy, true);
        EnergyDtoResponse resultForNotLast = energyMapper.toDtoResponse(energy, false);

        assertNotNull(resultForLast);
        assertEquals(1L, resultForLast.id());
        assertEquals(1000.0, resultForLast.counter1());
        assertTrue(resultForLast.last());

        assertNotNull(resultForNotLast);
        assertFalse(resultForNotLast.last());

    }
}