package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.repositories.PropertyLogRepository;

@ExtendWith(MockitoExtension.class)
class PropertyLogServiceTest {

    @Mock
    private PropertyLogRepository propertyLogRepository;

    @InjectMocks
    private PropertyLogService propertyLogService;

    @Test
    @DisplayName("Deve buscar todos os logs de uma propriedade ordenados por id desc")
    void findAllByPropertyId_ShouldReturnLogsOrderedByIdDesc() {
        // Given
        Long propertyId = 1L;
        PropertyLog log1 = new PropertyLog();
        log1.setId(1L);
        PropertyLog log2 = new PropertyLog();
        log2.setId(2L);

        when(propertyLogRepository.findAllByPropertyIdOrderByIdDesc(propertyId))
                .thenReturn(List.of(log2, log1));

        // When
        List<PropertyLog> result = propertyLogService.findAllByPropertyId(propertyId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
        verify(propertyLogRepository).findAllByPropertyIdOrderByIdDesc(propertyId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando propriedade não tem logs")
    void findAllByPropertyId_WithNoLogs_ShouldReturnEmptyList() {
        // Given
        Long propertyId = 999L;

        when(propertyLogRepository.findAllByPropertyIdOrderByIdDesc(propertyId))
                .thenReturn(List.of());

        // When
        List<PropertyLog> result = propertyLogService.findAllByPropertyId(propertyId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve buscar a última mudança relevante de uma propriedade")
    void findLastRelevantChangeByPropertyId_ShouldReturnLog() {
        // Given
        Long propertyId = 1L;
        PropertyLog log = new PropertyLog();
        log.setId(5L);

        when(propertyLogRepository.findLastRelevantChangeByPropertyId(propertyId))
                .thenReturn(Optional.of(log));

        // When
        Optional<PropertyLog> result = propertyLogService.findLastRelevantChangeByPropertyId(propertyId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
        verify(propertyLogRepository).findLastRelevantChangeByPropertyId(propertyId);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando não há mudança relevante")
    void findLastRelevantChangeByPropertyId_WithNoRelevantChange_ShouldReturnEmpty() {
        // Given
        Long propertyId = 999L;

        when(propertyLogRepository.findLastRelevantChangeByPropertyId(propertyId))
                .thenReturn(Optional.empty());

        // When
        Optional<PropertyLog> result = propertyLogService.findLastRelevantChangeByPropertyId(propertyId);

        // Then
        assertTrue(result.isEmpty());
    }
}
