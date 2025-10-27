package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;

@SpringBootTest
class CommercialEnergyMapperTest {

    @Autowired
    private CommercialEnergyMapper mapper;

    @Test
    @DisplayName("Deve converter CommercialEnergy para CommercialEnergyDtoResponse corretamente")
    void toDtoResponse_ShouldMapAllFieldsCorrectly() {
        // Given
        CommercialEnergy entity = new CommercialEnergy();
        entity.setId(1L);
        entity.setDate(LocalDate.of(2023, 10, 15));
        entity.setAmount1(100.50);
        entity.setAmount2(200.75);
        entity.setInternalCounter(50.25);
        entity.setAccountValue(350.00);
        entity.setAccountConsumption(150.00);
        entity.setCalculatedConsumption1(75.00);
        entity.setCalculatedConsumption2(80.00);

        Boolean isLast = true;

        // When
        CommercialEnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals(LocalDate.of(2023, 10, 15), dto.date());
        assertEquals(100.50, dto.amount1());
        assertEquals(200.75, dto.amount2());
        assertEquals(50.25, dto.internalCounter());
        assertEquals(350.00, dto.accountValue());
        assertEquals(150.00, dto.accountConsumption());
        assertEquals(75.00, dto.calculatedConsumption1());
        assertEquals(80.00, dto.calculatedConsumption2());
        assertEquals(true, dto.isLast());
    }

    @Test
    @DisplayName("Deve converter CommercialEnergy com isLast=false corretamente")
    void toDtoResponse_WithIsLastFalse_ShouldMapCorrectly() {
        // Given
        CommercialEnergy entity = new CommercialEnergy();
        entity.setId(2L);
        entity.setDate(LocalDate.of(2023, 11, 20));
        entity.setAmount1(150.00);

        Boolean isLast = false;

        // When
        CommercialEnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals(LocalDate.of(2023, 11, 20), dto.date());
        assertEquals(150.00, dto.amount1());
        assertEquals(false, dto.isLast());
    }

    @Test
    @DisplayName("Deve converter CommercialEnergy com campos nulos corretamente")
    void toDtoResponse_WithNullFields_ShouldMapCorrectly() {
        // Given
        CommercialEnergy entity = new CommercialEnergy();
        entity.setId(3L);

        Boolean isLast = true;

        // When
        CommercialEnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(3L, dto.id());
        assertEquals(true, dto.isLast());
    }
}
