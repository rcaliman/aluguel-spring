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

    @Mock
    private imoveis.aluguel.mappers.EnergyTitleMapper energyTitleMapper;

    @InjectMocks
    private EnergyTitleServiceImpl energyTitleService;

    private EnergyTitle energyTitle;
    private imoveis.aluguel.dtos.EnergyTitleDtoResponse energyTitleDtoResponse;

    @BeforeEach
    void setUp() {
        energyTitle = new EnergyTitle();
        energyTitle.setId(1L);
        energyTitle.setTitleAmount1("12345");

        energyTitleDtoResponse = new imoveis.aluguel.dtos.EnergyTitleDtoResponse(1L, "12345", "title2", "title3", "title4", "title5");
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
        when(energyTitleMapper.toDtoResponse(energyTitle)).thenReturn(energyTitleDtoResponse);

        imoveis.aluguel.dtos.EnergyTitleDtoResponse result = energyTitleService.findLast();

        assertNotNull(result);
        assertEquals(energyTitleDtoResponse, result);
        verify(energyTitleRepository, times(1)).findTopByOrderByIdDesc();
    }

    @Test
    @DisplayName("findLast - Deve retornar um novo título de energia se não houver título de energia")
    void findLast_ShouldReturnNewEnergyTitleWhenNoEnergyTitle() {
        when(energyTitleRepository.findTopByOrderByIdDesc()).thenReturn(null);
        when(energyTitleMapper.toDtoResponse(any(EnergyTitle.class))).thenReturn(energyTitleDtoResponse);

        imoveis.aluguel.dtos.EnergyTitleDtoResponse result = energyTitleService.findLast();

        assertNotNull(result);
        verify(energyTitleRepository, times(1)).findTopByOrderByIdDesc();
    }
}
