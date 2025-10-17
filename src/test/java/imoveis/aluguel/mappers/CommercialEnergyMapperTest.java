
package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.CommercialEnergyDtoRequest;
import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;

public class CommercialEnergyMapperTest {

    private CommercialEnergyMapper commercialEnergyMapper;

    @BeforeEach
    public void setUp() {
        commercialEnergyMapper = Mappers.getMapper(CommercialEnergyMapper.class);
    }

    @Test
    public void testToEntity() {
        CommercialEnergyDtoRequest dtoRequest = new CommercialEnergyDtoRequest(
                1L, 100.5, 200.5, 300.5, 50.25, 150.75, LocalDate.now());

        CommercialEnergy entity = commercialEnergyMapper.toEntity(dtoRequest);

        assertNotNull(entity);
        assertEquals(100.5, entity.getAmount1());
        assertEquals(200.5, entity.getAmount2());
        assertEquals(300.5, entity.getInternalCounter());
        assertEquals(50.25, entity.getAccountValue());
        assertEquals(150.75, entity.getAccountConsumption());
        assertEquals(LocalDate.now(), entity.getDate());
        assertNull(entity.getCalculatedConsumption1());
        assertNull(entity.getCalculatedConsumption2());
    }

    @Test
    public void testToDtoResponse() {
        CommercialEnergy entity = new CommercialEnergy();
        entity.setId(1L);
        entity.setDate(LocalDate.now());
        entity.setAmount1(100.5);
        entity.setAmount2(200.5);
        entity.setInternalCounter(300.5);
        entity.setAccountValue(50.25);
        entity.setAccountConsumption(150.75);
        entity.setCalculatedConsumption1(50.0);
        entity.setCalculatedConsumption2(100.0);

        CommercialEnergyDtoResponse dtoResponse = commercialEnergyMapper.toDtoResponse(entity, true);

        assertNotNull(dtoResponse);
        assertEquals(1L, dtoResponse.id());
        assertEquals(LocalDate.now(), dtoResponse.date());
        assertEquals(100.5, dtoResponse.amount1());
        assertEquals(200.5, dtoResponse.amount2());
        assertEquals(300.5, dtoResponse.internalCounter());
        assertEquals(50.25, dtoResponse.accountValue());
        assertEquals(150.75, dtoResponse.accountConsumption());
        assertEquals(50.0, dtoResponse.calculatedConsumption1());
        assertEquals(100.0, dtoResponse.calculatedConsumption2());
        assertEquals(true, dtoResponse.isLast());
    }
}
