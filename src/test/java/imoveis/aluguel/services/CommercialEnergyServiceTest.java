package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
class CommercialEnergyServiceTest {

    @Mock
    private CommercialEnergyRepository commercialEnergyRepository;

    @Mock
    private CommercialEnergyMapper commercialEnergyMapper;

    @InjectMocks
    private CommercialEnergyService commercialEnergyService;

    @Test
    @DisplayName("Deve listar as 2 últimas energy commercials ordenadas")
    void listLasts_ShouldReturnLastTwoEnergiesOrdered() {
        // Given
        CommercialEnergy energy1 = new CommercialEnergy();
        energy1.setId(1L);
        CommercialEnergy energy2 = new CommercialEnergy();
        energy2.setId(2L);

        when(commercialEnergyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(energy2, energy1)));

        CommercialEnergyDtoResponse dto1 = new CommercialEnergyDtoResponse(1L, null, null, null, null, null, null, null, null, false);
        CommercialEnergyDtoResponse dto2 = new CommercialEnergyDtoResponse(2L, null, null, null, null, null, null, null, null, true);

        when(commercialEnergyMapper.toDtoResponse(energy1, false)).thenReturn(dto1);
        when(commercialEnergyMapper.toDtoResponse(energy2, true)).thenReturn(dto2);

        // When
        List<CommercialEnergyDtoResponse> result = commercialEnergyService.listLasts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commercialEnergyRepository).findTop2ByOrderByIdDesc();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há energies")
    void listLasts_WithNoEnergies_ShouldReturnEmptyList() {
        // Given
        when(commercialEnergyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(Collections.emptyList()));

        // When
        List<CommercialEnergyDtoResponse> result = commercialEnergyService.listLasts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve calcular energy com base na última energy")
    void calculate_WithPreviousEnergy_ShouldCalculateCorrectly() {
        // Given
        CommercialEnergy lastEnergy = new CommercialEnergy();
        lastEnergy.setId(1L);
        lastEnergy.setInternalCounter(1000.0);

        CommercialEnergy newEnergy = new CommercialEnergy();
        newEnergy.setInternalCounter(1500.0);
        newEnergy.setAccountConsumption(700.0);
        newEnergy.setAccountValue(350.0);

        when(commercialEnergyRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.of(lastEnergy));
        when(commercialEnergyRepository.save(any(CommercialEnergy.class)))
                .thenReturn(newEnergy);

        // When
        CommercialEnergy result = commercialEnergyService.calculate(newEnergy);

        // Then
        assertNotNull(result);
        assertEquals(500.0, newEnergy.getCalculatedConsumption1()); // 1500 - 1000
        assertEquals(200.0, newEnergy.getCalculatedConsumption2()); // 700 - 500
        verify(commercialEnergyRepository).save(newEnergy);
    }

    @Test
    @DisplayName("Deve salvar energy sem calcular quando não há energia anterior")
    void calculate_WithoutPreviousEnergy_ShouldSaveWithoutCalculation() {
        // Given
        CommercialEnergy newEnergy = new CommercialEnergy();
        newEnergy.setInternalCounter(1000.0);
        newEnergy.setAccountConsumption(500.0);
        newEnergy.setAccountValue(250.0);

        when(commercialEnergyRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.empty());
        when(commercialEnergyRepository.save(any(CommercialEnergy.class)))
                .thenReturn(newEnergy);

        // When
        CommercialEnergy result = commercialEnergyService.calculate(newEnergy);

        // Then
        assertNotNull(result);
        verify(commercialEnergyRepository).save(newEnergy);
    }

    @Test
    @DisplayName("Deve editar energy existente com cálculos")
    void edit_WithTwoEnergies_ShouldUpdateAndCalculate() {
        // Given
        Long energyId = 2L;

        CommercialEnergy existingEnergy = new CommercialEnergy();
        existingEnergy.setId(energyId);
        existingEnergy.setInternalCounter(1000.0);

        CommercialEnergy previousEnergy = new CommercialEnergy();
        previousEnergy.setId(1L);
        previousEnergy.setInternalCounter(500.0);

        CommercialEnergy editedEnergy = new CommercialEnergy();
        editedEnergy.setDate(LocalDate.now());
        editedEnergy.setInternalCounter(1500.0);
        editedEnergy.setAccountConsumption(1200.0);
        editedEnergy.setAccountValue(600.0);

        when(commercialEnergyRepository.findById(energyId))
                .thenReturn(Optional.of(existingEnergy));
        when(commercialEnergyRepository.findTop2ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(existingEnergy, previousEnergy)));
        when(commercialEnergyRepository.save(any(CommercialEnergy.class)))
                .thenReturn(existingEnergy);

        // When
        CommercialEnergy result = commercialEnergyService.edit(editedEnergy, energyId);

        // Then
        assertNotNull(result);
        assertEquals(1500.0, existingEnergy.getInternalCounter());
        assertEquals(1200.0, existingEnergy.getAccountConsumption());
        assertEquals(600.0, existingEnergy.getAccountValue());
        verify(commercialEnergyRepository).save(existingEnergy);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao editar energy inexistente")
    void edit_WithNonExistentEnergy_ShouldThrowNotFoundException() {
        // Given
        Long energyId = 999L;
        CommercialEnergy editedEnergy = new CommercialEnergy();

        when(commercialEnergyRepository.findById(energyId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> {
            commercialEnergyService.edit(editedEnergy, energyId);
        });
    }

    @Test
    @DisplayName("Deve buscar energy por id")
    void findById_ShouldReturnEnergy() {
        // Given
        Long energyId = 1L;
        CommercialEnergy energy = new CommercialEnergy();
        energy.setId(energyId);

        when(commercialEnergyRepository.findById(energyId))
                .thenReturn(Optional.of(energy));

        // When
        CommercialEnergy result = commercialEnergyService.findById(energyId);

        // Then
        assertNotNull(result);
        assertEquals(energyId, result.getId());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar energy inexistente")
    void findById_WithNonExistentId_ShouldThrowNotFoundException() {
        // Given
        Long energyId = 999L;

        when(commercialEnergyRepository.findById(energyId))
                .thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> {
            commercialEnergyService.findById(energyId);
        });
    }
}
