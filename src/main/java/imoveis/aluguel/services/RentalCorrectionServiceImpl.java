package imoveis.aluguel.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.RentalCorrectionDTO;
import imoveis.aluguel.entities.PropertyLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RentalCorrectionServiceImpl implements RentalCorrectionService {

    private final PropertyLogService propertyLogService;
    private final BacenService bacenService;

    @Override
    public RentalCorrectionDTO calculateCorrectedRentalValue(Long propertyId) {
        
        Optional<PropertyLog> lastChangeOpt = propertyLogService.findLastRelevantChangeByPropertyId(propertyId);

        if (lastChangeOpt.isEmpty()) {
            log.warn("Nenhuma alteração relevante encontrada para o imóvel ID {}", propertyId);
            return null;
        }

        PropertyLog lastChange = lastChangeOpt.get();

        if (lastChange.getValue() == null) {
            log.warn("O último registro não possui valor de aluguel definido para o imóvel ID {}", propertyId);
            return null;
        }

        LocalDate lastChangeDate = lastChange.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        if (!lastChangeDate.isBefore(today)) {
            log.info("A última alteração é da data de hoje ou futura. Não há correção a ser aplicada.");
            return RentalCorrectionDTO.builder()
                    .originalValue(lastChange.getValue())
                    .originalDate(lastChange.getCreatedAt())
                    .correctedValue(lastChange.getValue())
                    .correctionDate(Instant.now())
                    .accumulatedIpcaPercentage(0.0)
                    .detailedCalculation("Não há correção necessária. Data de referência é atual.")
                    .build();
        }

        Double accumulatedIpca = bacenService.calculateAccumulatedIpca(lastChangeDate, today);

        Double originalValue = lastChange.getValue();
        Double correctedValue = originalValue * (1 + accumulatedIpca / 100);

        String detailedCalculation = String.format(
                "Valor original: R$ %.2f | Data: %s | IPCA acumulado: %.2f%% | Valor corrigido: R$ %.2f",
                originalValue,
                lastChangeDate,
                accumulatedIpca,
                correctedValue);

        log.info("Correção calculada para imóvel ID {}: {}", propertyId, detailedCalculation);

        return RentalCorrectionDTO.builder()
                .originalValue(originalValue)
                .originalDate(lastChange.getCreatedAt())
                .correctedValue(correctedValue)
                .correctionDate(Instant.now())
                .accumulatedIpcaPercentage(accumulatedIpca)
                .detailedCalculation(detailedCalculation)
                .build();
    }

}
