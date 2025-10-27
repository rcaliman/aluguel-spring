package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.dtos.RentalDtoCorrection;
import imoveis.aluguel.entities.PropertyLog;

@ExtendWith(MockitoExtension.class)
class RentalCorrectionServiceTest {

    @Mock private PropertyLogService propertyLogService;
    @Mock private BacenService bacenService;
    @InjectMocks private RentalCorrectionService rentalCorrectionService;

    @Test
    @DisplayName("Deve calcular correção de aluguel com IPCA")
    void calculateCorrectedRentalValue_ShouldReturnCorrection() {
        PropertyLog log = new PropertyLog();
        log.setValue(1000.0);
        LocalDate pastDate = LocalDate.now().minusMonths(6);
        log.setCreatedAt(pastDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(propertyLogService.findLastRelevantChangeByPropertyId(1L))
                .thenReturn(Optional.of(log));
        when(bacenService.calculateAccumulatedIpca(any(), any()))
                .thenReturn(5.0);

        RentalDtoCorrection result = rentalCorrectionService.calculateCorrectedRentalValue(1L);

        assertNotNull(result);
        assertEquals(1000.0, result.getOriginalValue());
        assertEquals(1050.0, result.getCorrectedValue());
        assertEquals(5.0, result.getAccumulatedIpcaPercentage());
    }

    @Test
    @DisplayName("Deve retornar null quando não há log de propriedade")
    void calculateCorrectedRentalValue_WithNoLog_ShouldReturnNull() {
        when(propertyLogService.findLastRelevantChangeByPropertyId(1L))
                .thenReturn(Optional.empty());

        RentalDtoCorrection result = rentalCorrectionService.calculateCorrectedRentalValue(1L);

        assertNull(result);
    }

    @Test
    @DisplayName("Deve retornar correção zero quando data é atual")
    void calculateCorrectedRentalValue_WithCurrentDate_ShouldReturnZeroCorrection() {
        PropertyLog log = new PropertyLog();
        log.setValue(1000.0);
        log.setCreatedAt(Instant.now());

        when(propertyLogService.findLastRelevantChangeByPropertyId(1L))
                .thenReturn(Optional.of(log));

        RentalDtoCorrection result = rentalCorrectionService.calculateCorrectedRentalValue(1L);

        assertNotNull(result);
        assertEquals(1000.0, result.getOriginalValue());
        assertEquals(1000.0, result.getCorrectedValue());
        assertEquals(0.0, result.getAccumulatedIpcaPercentage());
    }
}
