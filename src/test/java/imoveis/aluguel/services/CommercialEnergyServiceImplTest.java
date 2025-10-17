package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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

import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.CommercialEnergyMapper;
import imoveis.aluguel.repositories.CommercialEnergyRepository;

@ExtendWith(MockitoExtension.class)
class CommercialEnergyServiceImplTest {

    @Mock
    private CommercialEnergyRepository commercialEnergyRepository;

    @Mock
    private CommercialEnergyMapper commercialEnergyMapper;

    @InjectMocks
    private CommercialEnergyServiceImpl commercialEnergyService;

    private CommercialEnergy previousCommercialEnergy;
    private CommercialEnergy newCommercialEnergy;
    private CommercialEnergyDtoResponse commercialEnergyDtoResponse;

    @BeforeEach
    void setUp() {

        previousCommercialEnergy = new CommercialEnergy();
        previousCommercialEnergy.setId(1L);
        previousCommercialEnergy.setInternalCounter(1000.0);
        previousCommercialEnergy.setAccountConsumption(500.0);
        previousCommercialEnergy.setAccountValue(100.0);

        newCommercialEnergy = new CommercialEnergy();
        newCommercialEnergy.setInternalCounter(1100.0);
        newCommercialEnergy.setAccountConsumption(550.0);
        newCommercialEnergy.setAccountValue(110.0);
        newCommercialEnergy.setDate(LocalDate.now());

        commercialEnergyDtoResponse = new CommercialEnergyDtoResponse(
            1L, 
            LocalDate.now(), 
            null, 
            null, 
            1100.0, 
            110.0, 
            550.0, 
            null, 
            null, 
            false
        );
    }

    @Test
    @DisplayName("listLasts - Deve retornar uma lista vazia se não houver registros")
    void listLasts_ShouldReturnEmptyList_WhenNoRecords() {
        when(commercialEnergyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.empty());

        List<CommercialEnergyDtoResponse> result = commercialEnergyService.listLasts();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("listLasts - Deve retornar os últimos 2 registros ordenados por ID")
    void listLasts_ShouldReturnLastTwoRecordsOrderedById() {
        CommercialEnergy oldEnergy = new CommercialEnergy();
        oldEnergy.setId(1L);
        CommercialEnergy latestEnergy = new CommercialEnergy();
        latestEnergy.setId(2L);

        CommercialEnergyDtoResponse oldEnergyDto = new CommercialEnergyDtoResponse(1L, null, null, null, 0.0, 0.0, 0.0, null, null, false);
        CommercialEnergyDtoResponse latestEnergyDto = new CommercialEnergyDtoResponse(2L, null, null, null, 0.0, 0.0, 0.0, null, null, false);

        when(commercialEnergyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(latestEnergy, oldEnergy)));
        when(commercialEnergyMapper.toDtoResponse(oldEnergy, false)).thenReturn(oldEnergyDto);
        when(commercialEnergyMapper.toDtoResponse(latestEnergy, true)).thenReturn(latestEnergyDto);

        List<CommercialEnergyDtoResponse> result = commercialEnergyService.listLasts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("calculate - Deve calcular e salvar a leitura de energia comercial com base na anterior")
    void calculate_WhenPreviousReadingExists_ShouldCalculateAndSave() {

        when(commercialEnergyRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(previousCommercialEnergy));
        when(commercialEnergyRepository.save(any(CommercialEnergy.class))).thenReturn(newCommercialEnergy);
        when(commercialEnergyMapper.toDtoResponse(any(CommercialEnergy.class), any())).thenReturn(commercialEnergyDtoResponse);

        CommercialEnergyDtoResponse result = commercialEnergyService.calculate(newCommercialEnergy);

        assertNotNull(result);
    }

    @Test
    @DisplayName("calculate - Não deve calcular valores se não houver leitura anterior")
    void calculate_WhenNoPreviousReading_ShouldSaveWithoutAmounts() {

        when(commercialEnergyRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(commercialEnergyRepository.save(any(CommercialEnergy.class))).thenReturn(newCommercialEnergy);
        when(commercialEnergyMapper.toDtoResponse(any(CommercialEnergy.class), any())).thenReturn(commercialEnergyDtoResponse);

        CommercialEnergyDtoResponse result = commercialEnergyService.calculate(newCommercialEnergy);

        assertNotNull(result);
        verify(commercialEnergyRepository, times(1)).save(newCommercialEnergy);
    }

    @Test
    @DisplayName("edit - Deve recalcular e salvar uma leitura existente")
    void edit_WhenReadingExists_ShouldRecalculateAndSave() {

        CommercialEnergy readingBeforePrevious = new CommercialEnergy();
        readingBeforePrevious.setId(1L);
        readingBeforePrevious.setInternalCounter(1000.0);
        readingBeforePrevious.setAccountConsumption(500.0);
        readingBeforePrevious.setAccountValue(100.0);

        CommercialEnergy existingCommercialEnergy = new CommercialEnergy();
        existingCommercialEnergy.setId(2L);
        existingCommercialEnergy.setInternalCounter(1050.0);
        existingCommercialEnergy.setAccountConsumption(520.0);
        existingCommercialEnergy.setAccountValue(105.0);

        CommercialEnergy editedData = new CommercialEnergy();
        editedData.setInternalCounter(1100.0);
        editedData.setAccountConsumption(550.0);
        editedData.setAccountValue(110.0);

        when(commercialEnergyRepository.findById(2L)).thenReturn(Optional.of(existingCommercialEnergy));
        when(commercialEnergyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(existingCommercialEnergy, readingBeforePrevious)));
        when(commercialEnergyRepository.save(any(CommercialEnergy.class))).thenReturn(existingCommercialEnergy);
        when(commercialEnergyMapper.toDtoResponse(any(CommercialEnergy.class), any())).thenReturn(commercialEnergyDtoResponse);

        CommercialEnergyDtoResponse result = commercialEnergyService.edit(editedData, 2L);

        assertNotNull(result);
        assertEquals(1100.0, result.internalCounter());

        verify(commercialEnergyRepository, times(1)).save(existingCommercialEnergy);

    }

    @Test
    @DisplayName("edit - Deve lançar NotFoundException se a leitura não existir")
    void edit_WhenReadingNotFound_ShouldThrowException() {

        when(commercialEnergyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            commercialEnergyService.edit(new CommercialEnergy(), 99L);
        });

        verify(commercialEnergyRepository, never()).save(any(CommercialEnergy.class));

    }

    @Test
    @DisplayName("findById - Deve encontrar uma leitura de energia comercial pelo ID")
    void findById_WhenCommercialEnergyExists_ShouldReturnCommercialEnergy() {

        when(commercialEnergyRepository.findById(1L)).thenReturn(Optional.of(previousCommercialEnergy));
        when(commercialEnergyMapper.toDtoResponse(any(CommercialEnergy.class), any())).thenReturn(commercialEnergyDtoResponse);

        CommercialEnergyDtoResponse found = commercialEnergyService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.id());

    }

    @Test
    @DisplayName("findById - Deve lançar NotFoundException ao buscar ID inexistente")
    void findById_WhenCommercialEnergyNotFound_ShouldThrowException() {

        when(commercialEnergyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            commercialEnergyService.findById(99L);
        });

    }
}
