package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

@SpringBootTest
class EnergyMapperTest {

    @Autowired
    private EnergyMapper mapper;

    @Test
    @DisplayName("Deve converter Energy para EnergyDtoResponse corretamente")
    void toDtoResponse_ShouldMapAllFieldsCorrectly() {
        // Given
        Energy entity = new Energy();
        entity.setId(1L);
        entity.setCounter1(100.0);
        entity.setCounter2(200.0);
        entity.setCounter3(300.0);
        entity.setAmount1(50.0);
        entity.setAmount2(75.0);
        entity.setAmount3(100.0);
        entity.setKwhValue(0.75);
        entity.setBillAmount(225.0);
        entity.setDate(LocalDate.of(2023, 10, 15));

        boolean isLast = true;

        // When
        EnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals(100.0, dto.counter1());
        assertEquals(200.0, dto.counter2());
        assertEquals(300.0, dto.counter3());
        assertEquals(50.0, dto.amount1());
        assertEquals(75.0, dto.amount2());
        assertEquals(100.0, dto.amount3());
        assertEquals(0.75, dto.kwhValue());
        assertEquals(225.0, dto.billAmount());
        assertEquals(LocalDate.of(2023, 10, 15), dto.date());
        assertEquals(true, dto.last());
    }

    @Test
    @DisplayName("Deve converter Energy com isLast=false corretamente")
    void toDtoResponse_WithIsLastFalse_ShouldMapCorrectly() {
        // Given
        Energy entity = new Energy();
        entity.setId(2L);
        entity.setCounter1(150.0);
        entity.setCounter2(250.0);
        entity.setCounter3(350.0);
        entity.setDate(LocalDate.of(2023, 11, 20));

        boolean isLast = false;

        // When
        EnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals(150.0, dto.counter1());
        assertEquals(250.0, dto.counter2());
        assertEquals(350.0, dto.counter3());
        assertEquals(LocalDate.of(2023, 11, 20), dto.date());
        assertEquals(false, dto.last());
    }

    @Test
    @DisplayName("Deve converter Energy com campos opcionais nulos corretamente")
    void toDtoResponse_WithNullOptionalFields_ShouldMapCorrectly() {
        // Given
        Energy entity = new Energy();
        entity.setId(3L);
        entity.setCounter1(100.0);
        entity.setCounter2(200.0);
        entity.setCounter3(300.0);

        boolean isLast = true;

        // When
        EnergyDtoResponse dto = mapper.toDtoResponse(entity, isLast);

        // Then
        assertNotNull(dto);
        assertEquals(3L, dto.id());
        assertEquals(100.0, dto.counter1());
        assertEquals(200.0, dto.counter2());
        assertEquals(300.0, dto.counter3());
        assertEquals(true, dto.last());
    }
}
