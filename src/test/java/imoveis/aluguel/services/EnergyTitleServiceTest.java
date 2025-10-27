package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.repositories.EnergyTitleRepository;

@ExtendWith(MockitoExtension.class)
class EnergyTitleServiceTest {

    @Mock
    private EnergyTitleRepository energyTitleRepository;

    @InjectMocks
    private EnergyTitleService energyTitleService;

    @Test
    @DisplayName("Deve salvar energy title")
    void save_ShouldSaveEnergyTitle() {
        // Given
        EnergyTitle energyTitle = new EnergyTitle();
        energyTitle.setTitleAmount1("Título 1");

        when(energyTitleRepository.save(any(EnergyTitle.class)))
                .thenReturn(energyTitle);

        // When
        energyTitleService.save(energyTitle);

        // Then
        verify(energyTitleRepository).save(energyTitle);
    }

    @Test
    @DisplayName("Deve buscar último title e retornar com valores padrão quando não existe")
    void findLast_WithNoTitles_ShouldReturnDefaultTitles() {
        // Given
        when(energyTitleRepository.findTopByOrderByIdDesc())
                .thenReturn(null);

        // When
        EnergyTitle result = energyTitleService.findLast();

        // Then
        assertNotNull(result);
        assertEquals("conta 1", result.getTitleAmount1());
        assertEquals("conta 2", result.getTitleAmount2());
        assertEquals("conta 3", result.getTitleAmount3());
        assertEquals("conta 1", result.getTitleAmount4());
        assertEquals("conta 2", result.getTitleAmount5());
    }

    @Test
    @DisplayName("Deve buscar último title e completar campos em branco com valores padrão")
    void findLast_WithPartialTitles_ShouldCompleteWithDefaults() {
        // Given
        EnergyTitle energyTitle = new EnergyTitle();
        energyTitle.setTitleAmount1("Customizado 1");
        energyTitle.setTitleAmount2("");
        energyTitle.setTitleAmount3(null);

        when(energyTitleRepository.findTopByOrderByIdDesc())
                .thenReturn(energyTitle);

        // When
        EnergyTitle result = energyTitleService.findLast();

        // Then
        assertNotNull(result);
        assertEquals("Customizado 1", result.getTitleAmount1());
        assertEquals("conta 2", result.getTitleAmount2());
        assertEquals("conta 3", result.getTitleAmount3());
        assertEquals("conta 1", result.getTitleAmount4());
        assertEquals("conta 2", result.getTitleAmount5());
    }

    @Test
    @DisplayName("Deve retornar title existente sem modificações quando todos campos estão preenchidos")
    void findLast_WithCompleteTitles_ShouldReturnAsIs() {
        // Given
        EnergyTitle energyTitle = new EnergyTitle();
        energyTitle.setTitleAmount1("Título 1");
        energyTitle.setTitleAmount2("Título 2");
        energyTitle.setTitleAmount3("Título 3");
        energyTitle.setTitleAmount4("Título 4");
        energyTitle.setTitleAmount5("Título 5");

        when(energyTitleRepository.findTopByOrderByIdDesc())
                .thenReturn(energyTitle);

        // When
        EnergyTitle result = energyTitleService.findLast();

        // Then
        assertNotNull(result);
        assertEquals("Título 1", result.getTitleAmount1());
        assertEquals("Título 2", result.getTitleAmount2());
        assertEquals("Título 3", result.getTitleAmount3());
        assertEquals("Título 4", result.getTitleAmount4());
        assertEquals("Título 5", result.getTitleAmount5());
    }
}
