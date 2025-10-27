package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.repositories.EnergyRepository;

@ExtendWith(MockitoExtension.class)
class EnergyServiceTest {

    @Mock private EnergyRepository energyRepository;
    @Mock private EnergyMapper energyMapper;
    @InjectMocks private EnergyService energyService;

    @Test
    @DisplayName("Deve listar as 3 últimas energies")
    void listLasts_ShouldReturnLastThree() {
        Energy e1 = new Energy();
        e1.setId(1L);
        Energy e2 = new Energy();
        e2.setId(2L);

        when(energyRepository.findTop3ByOrderByIdDesc())
                .thenReturn(Optional.of(List.of(e2, e1)));
        when(energyMapper.toDtoResponse(any(), anyBoolean()))
                .thenReturn(new EnergyDtoResponse(1L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, null, false));

        List<EnergyDtoResponse> result = energyService.listLasts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve calcular e salvar energy")
    void calculate_ShouldSaveWithCalculation() {
        Energy newEnergy = new Energy();
        newEnergy.setCounter1(100.0);
        newEnergy.setCounter2(200.0);
        newEnergy.setCounter3(300.0);
        newEnergy.setBillAmount(600.0);

        when(energyRepository.findTop2ByOrderByIdDesc()).thenReturn(Optional.empty());
        when(energyRepository.save(any(Energy.class))).thenReturn(newEnergy);

        Energy result = energyService.calculate(newEnergy);

        assertNotNull(result);
        verify(energyRepository).save(newEnergy);
    }

    @Test
    @DisplayName("Deve buscar energy por id")
    void findById_ShouldReturnEnergy() {
        Energy energy = new Energy();
        when(energyRepository.findById(1L)).thenReturn(Optional.of(energy));

        Energy result = energyService.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar energy inexistente")
    void findById_WithNonExistent_ShouldThrowNotFoundException() {
        when(energyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> energyService.findById(999L));
    }
}
