package imoveis.aluguel.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import imoveis.aluguel.dtos.BacenIpcaResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BacenServiceImpl implements BacenService {

    private static final String BACEN_IPCA_URL = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/dados";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RestTemplate restTemplate;

    @Override
    public List<BacenIpcaResponseDTO> getIpcaData(LocalDate startDate, LocalDate endDate) {
        try {
            String url = String.format("%s?formato=json&dataInicial=%s&dataFinal=%s",
                    BACEN_IPCA_URL,
                    startDate.format(DATE_FORMATTER),
                    endDate.format(DATE_FORMATTER));

            log.info("Consultando IPCA no Bacen: {}", url);

            BacenIpcaResponseDTO[] response = restTemplate.getForObject(url, BacenIpcaResponseDTO[].class);

            if (response == null || response.length == 0) {
                log.warn("Nenhum dado de IPCA encontrado para o período {} a {}", startDate, endDate);
                return List.of();
            }

            return Arrays.asList(response);

        } catch (Exception e) {
            log.error("Erro ao consultar API do Bacen", e);
            throw new RuntimeException("Erro ao consultar dados do IPCA no Bacen", e);
        }
    }

    @Override
    public Double calculateAccumulatedIpca(LocalDate startDate, LocalDate endDate) {
        List<BacenIpcaResponseDTO> ipcaData = getIpcaData(startDate, endDate);

        if (ipcaData.isEmpty()) {
            log.warn("Não há dados de IPCA para calcular a correção");
            return 0.0;
        }

        // Cálculo do índice acumulado: (1 + i1/100) * (1 + i2/100) * ... - 1
        double accumulatedIndex = 1.0;

        for (BacenIpcaResponseDTO ipca : ipcaData) {
            Double valor = ipca.getValorAsDouble();
            if (valor != null) {
                accumulatedIndex *= (1 + valor / 100);
            }
        }

        // Retorna a variação percentual acumulada
        double accumulatedPercentage = (accumulatedIndex - 1) * 100;

        log.info("IPCA acumulado de {} a {}: {:.2f}%", startDate, endDate, accumulatedPercentage);

        return accumulatedPercentage;
    }

}
