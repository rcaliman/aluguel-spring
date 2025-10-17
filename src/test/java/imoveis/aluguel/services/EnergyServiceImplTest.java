package imoveis.aluguel.services;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.mappers.EnergyMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.EnergyRepository;

@ExtendWith(MockitoExtension.class)
class EnergyServiceImplTest {

    @Mock
    private EnergyRepository energyRepository;

    @Mock
    private EnergyMapper energyMapper;

    @InjectMocks
    private EnergyServiceImpl energyService;

    private Energy previousEnergy;
    private Energy newEnergy;

    @BeforeEach
    void setUp() {

        previousEnergy = new Energy();

        previousEnergy.setId(1L);
        previousEnergy.setCounter1(1000.0);
        previousEnergy.setCounter2(2000.0);
        previousEnergy.setCounter3(3000.0);

        newEnergy = new Energy();

        newEnergy.setCounter1(1100.0);
        newEnergy.setCounter2(2200.0);
        newEnergy.setCounter3(3300.0);
        newEnergy.setBillAmount(600.0);
        newEnergy.setDate(LocalDate.now());
    }

    @Test
    @DisplayName("calculate - Deve calcular e salvar a leitura de energia com base na anterior")
    void calculate_WhenPreviousReadingExists_ShouldCalculateAndSave() {

        when(energyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.of(List.of(previousEnergy)));
        when(energyRepository.save(any(Energy.class))).thenReturn(newEnergy);
        when(energyMapper.toDtoResponse(newEnergy, false)).thenReturn(new EnergyDtoResponse(2L, 1100.0, 2200.0, 3300.0, 100.0, 200.0, 300.0, 0.0, 600.0, LocalDate.now(), false));

        EnergyDtoResponse result = energyService.calculate(newEnergy);

        assertNotNull(result);

        assertEquals(100.0, result.amount1());
        assertEquals(200.0, result.amount2());
        assertEquals(300.0, result.amount3());
        verify(energyRepository, times(1)).save(any(Energy.class));

    }

    @Test
    @DisplayName("calculate - Não deve calcular valores se não houver leitura anterior")
    void calculate_WhenNoPreviousReading_ShouldSaveWithoutAmounts() {

        when(energyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.of(Collections.emptyList()));
        when(energyRepository.save(any(Energy.class))).thenReturn(newEnergy);
        when(energyMapper.toDtoResponse(newEnergy, false)).thenReturn(new EnergyDtoResponse(2L, 1100.0, 2200.0, 3300.0, null, null, null, 0.0, 600.0, LocalDate.now(), false));

        EnergyDtoResponse result = energyService.calculate(newEnergy);

        assertNull(result.amount1());
        assertNull(result.amount2());
        assertNull(result.amount3());
        verify(energyRepository, times(1)).save(any(Energy.class));
    }

    @Test
    @DisplayName("edit - Deve recalcular e salvar uma leitura existente")
    void edit_WhenReadingExists_ShouldRecalculateAndSave() {

        Energy readingBeforePrevious = new Energy();
        readingBeforePrevious.setId(1L);
        readingBeforePrevious.setCounter1(1000.0);
        readingBeforePrevious.setCounter2(2000.0);
        readingBeforePrevious.setCounter3(3000.0);

        Energy existingEnergy = new Energy();
        existingEnergy.setId(2L);

        Energy editedData = new Energy();
        editedData.setCounter1(1100.0);
        editedData.setCounter2(2200.0);
        editedData.setCounter3(3300.0);
        editedData.setBillAmount(1200.0);

        when(energyRepository.findById(2L)).thenReturn(Optional.of(existingEnergy));
        when(energyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(new Energy(), readingBeforePrevious)));
        when(energyRepository.save(any(Energy.class))).thenReturn(existingEnergy);
        when(energyMapper.toDtoResponse(existingEnergy, false)).thenReturn(new EnergyDtoResponse(2L, 1100.0, 2200.0, 3300.0, 200.0, 400.0, 600.0, 0.0, 1200.0, LocalDate.now(), false));

        EnergyDtoResponse result = energyService.edit(editedData, 2L);

        assertNotNull(result);

        assertEquals(200.0, result.amount1());
        assertEquals(400.0, result.amount2());
        assertEquals(600.0, result.amount3());
        assertEquals(1100.0, result.counter1());

        verify(energyRepository, times(1)).save(existingEnergy);

    }

    @Test
    @DisplayName("edit - Deve lançar NotFoundException se a leitura não existir")
    void edit_WhenReadingNotFound_ShouldThrowException() {

        when(energyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            energyService.edit(new Energy(), 99L);
        });

        verify(energyRepository, never()).save(any(Energy.class));

    }
}