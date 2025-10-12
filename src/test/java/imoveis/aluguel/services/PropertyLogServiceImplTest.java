package imoveis.aluguel.services;

import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.repositories.PropertyLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyLogServiceImplTest {

    @Mock
    private PropertyLogRepository propertyLogRepository;

    @InjectMocks
    private PropertyLogServiceImpl propertyLogService;

    @Test
    @DisplayName("Deve chamar o método do repositório e retornar a lista de logs correta")
    void findAllByPropertyId_ShouldCallRepositoryAndReturnList() {
        
        Long propertyId = 1L;
        
        PropertyLog log1 = new PropertyLog();
        log1.setId(100L);
        log1.setObservation("Log 1");

        PropertyLog log2 = new PropertyLog();
        log2.setId(101L);
        log2.setObservation("Log 2");
        
        List<PropertyLog> expectedLogs = List.of(log1, log2);

        when(propertyLogRepository.findAllByPropertyIdOrderByIdDesc(propertyId)).thenReturn(expectedLogs);

        List<PropertyLog> actualLogs = propertyLogService.findAllByPropertyId(propertyId);

        assertNotNull(actualLogs, "A lista de logs não deveria ser nula.");
        assertEquals(2, actualLogs.size(), "A lista deveria conter 2 logs.");
        assertEquals(expectedLogs, actualLogs, "A lista retornada deveria ser a mesma que a do repositório.");

        verify(propertyLogRepository, times(1)).findAllByPropertyIdOrderByIdDesc(propertyId);
    
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se o repositório não encontrar logs")
    void findAllByPropertyId_WhenNoLogsFound_ShouldReturnEmptyList() {
        
        Long propertyId = 2L;
        
        when(propertyLogRepository.findAllByPropertyIdOrderByIdDesc(propertyId)).thenReturn(Collections.emptyList());

        List<PropertyLog> actualLogs = propertyLogService.findAllByPropertyId(propertyId);

        assertNotNull(actualLogs, "A lista não deveria ser nula, mesmo que vazia.");
        assertTrue(actualLogs.isEmpty(), "A lista deveria estar vazia.");

        verify(propertyLogRepository, times(1)).findAllByPropertyIdOrderByIdDesc(propertyId);
    
    }
}