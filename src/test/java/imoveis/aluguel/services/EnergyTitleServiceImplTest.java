package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.repositories.EnergyTitleRepository;

@ExtendWith(MockitoExtension.class)
class EnergyTitleServiceImplTest {

    @Mock
    private EnergyTitleRepository energyTitleRepository;

    @InjectMocks
    private EnergyTitleServiceImpl energyTitleService;

    private EnergyTitle energyTitle;

    @BeforeEach
    void setUp() {
        energyTitle = new EnergyTitle();
        energyTitle.setId(1L);
        energyTitle.setTitleAmount1("12345");
    }

    @Test
    @DisplayName("save - Deve salvar um título de energia")
    void save_ShouldSaveEnergyTitle() {
        energyTitleService.save(energyTitle);

        verify(energyTitleRepository, times(1)).save(energyTitle);
    }

    @Test
    @DisplayName("findLast - Deve retornar o último título de energia")
    void findLast_ShouldReturnLastEnergyTitle() {
        when(energyTitleRepository.findTopByOrderByIdDesc()).thenReturn(energyTitle);

        EnergyTitle result = energyTitleService.findLast();

        assertNotNull(result);
        assertEquals(energyTitle, result);
        verify(energyTitleRepository, times(1)).findTopByOrderByIdDesc();
    }

    @Test
    @DisplayName("findLast - Deve retornar nulo se não houver título de energia")
    void findLast_ShouldReturnNullWhenNoEnergyTitle() {
        when(energyTitleRepository.findTopByOrderByIdDesc()).thenReturn(null);

        EnergyTitle result = energyTitleService.findLast();

        assertNull(result);
        verify(energyTitleRepository, times(1)).findTopByOrderByIdDesc();
    }
}
