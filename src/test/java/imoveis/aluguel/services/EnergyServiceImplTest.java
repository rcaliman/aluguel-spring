package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import imoveis.aluguel.repositories.EnergyRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EnergyServiceImplTest {

    @Mock
    private EnergyRepository energyRepository;

    @InjectMocks
    private EnergyServiceImpl energyService;

    private Energy previousEnergy;
    private Energy newEnergy;

    @BeforeEach
    void setUp() {

        previousEnergy = new Energy();

        previousEnergy.setId(1L);
        previousEnergy.setCounter1(1000L);
        previousEnergy.setCounter2(2000L);
        previousEnergy.setCounter3(3000L);

        newEnergy = new Energy();

        newEnergy.setCounter1(1100L);
        newEnergy.setCounter2(2200L);
        newEnergy.setCounter3(3300L);
        newEnergy.setBillAmount(600.0);
        newEnergy.setDate(LocalDate.now());
    }

    @Test
    @DisplayName("calculate - Deve calcular e salvar a leitura de energia com base na anterior")
    void calculate_WhenPreviousReadingExists_ShouldCalculateAndSave() {

        when(energyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.of(List.of(previousEnergy)));
        when(energyRepository.save(any(Energy.class))).thenReturn(newEnergy);

        Energy result = energyService.calculate(newEnergy);

        assertNotNull(result);

        assertEquals(100.0, result.getAmount1());
        assertEquals(200.0, result.getAmount2());
        assertEquals(300.0, result.getAmount3());
        verify(energyRepository, times(1)).save(result);

    }

    @Test
    @DisplayName("calculate - Não deve calcular valores se não houver leitura anterior")
    void calculate_WhenNoPreviousReading_ShouldSaveWithoutAmounts() {

        when(energyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.of(Collections.emptyList()));
        when(energyRepository.save(any(Energy.class))).thenReturn(newEnergy);

        Energy result = energyService.calculate(newEnergy);

        assertNull(result.getAmount1());
        assertNull(result.getAmount2());
        assertNull(result.getAmount3());
        verify(energyRepository, times(1)).save(result);
    }

    @Test
    @DisplayName("edit - Deve recalcular e salvar uma leitura existente")
    void edit_WhenReadingExists_ShouldRecalculateAndSave() {

        Energy readingBeforePrevious = new Energy();
        readingBeforePrevious.setId(1L);
        readingBeforePrevious.setCounter1(1000L);
        readingBeforePrevious.setCounter2(2000L);
        readingBeforePrevious.setCounter3(3000L);

        Energy existingEnergy = new Energy();
        existingEnergy.setId(2L);

        Energy editedData = new Energy();
        editedData.setCounter1(1100L);
        editedData.setCounter2(2200L);
        editedData.setCounter3(3300L);
        editedData.setBillAmount(1200.0);

        when(energyRepository.findById(2L)).thenReturn(Optional.of(existingEnergy));
        when(energyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(new Energy(), readingBeforePrevious)));
        when(energyRepository.save(any(Energy.class))).thenReturn(existingEnergy);

        Energy result = energyService.edit(editedData, 2L);

        assertNotNull(result);

        assertEquals(200.0, result.getAmount1());
        assertEquals(400.0, result.getAmount2());
        assertEquals(600.0, result.getAmount3());
        assertEquals(1100L, result.getCounter1());

        verify(energyRepository, times(1)).save(existingEnergy);

    }

    @Test
    @DisplayName("edit - Deve lançar EntityNotFoundException se a leitura não existir")
    void edit_WhenReadingNotFound_ShouldThrowException() {

        when(energyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            energyService.edit(new Energy(), 99L);
        });

        verify(energyRepository, never()).save(any(Energy.class));

    }
}