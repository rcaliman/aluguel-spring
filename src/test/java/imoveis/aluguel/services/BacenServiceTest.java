package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import imoveis.aluguel.dtos.BacenIpcaDtoResponse;

@ExtendWith(MockitoExtension.class)
class BacenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BacenService bacenService;

    @Test
    @DisplayName("Deve buscar dados de IPCA do Bacen com sucesso")
    void getIpcaData_ShouldReturnIpcaDataSuccessfully() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 31);

        BacenIpcaDtoResponse[] mockResponse = new BacenIpcaDtoResponse[] {
                new BacenIpcaDtoResponse("01/01/2023", "0.5"),
                new BacenIpcaDtoResponse("01/02/2023", "0.6"),
                new BacenIpcaDtoResponse("01/03/2023", "0.7")
        };

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(mockResponse);

        // When
        List<BacenIpcaDtoResponse> result = bacenService.getIpcaData(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("0.5", result.get(0).getValor());
        assertEquals("0.6", result.get(1).getValor());
        assertEquals("0.7", result.get(2).getValor());
        verify(restTemplate).getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando resposta do Bacen é null")
    void getIpcaData_WithNullResponse_ShouldReturnEmptyList() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(null);

        // When
        List<BacenIpcaDtoResponse> result = bacenService.getIpcaData(startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando resposta do Bacen é array vazio")
    void getIpcaData_WithEmptyResponse_ShouldReturnEmptyList() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        BacenIpcaDtoResponse[] mockResponse = new BacenIpcaDtoResponse[] {};

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(mockResponse);

        // When
        List<BacenIpcaDtoResponse> result = bacenService.getIpcaData(startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando Bacen retorna 404")
    void getIpcaData_WithNotFound_ShouldReturnEmptyList() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        // When
        List<BacenIpcaDtoResponse> result = bacenService.getIpcaData(startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando ocorre erro genérico")
    void getIpcaData_WithGenericError_ShouldReturnEmptyList() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenThrow(new RuntimeException("Erro de conexão"));

        // When
        List<BacenIpcaDtoResponse> result = bacenService.getIpcaData(startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve calcular IPCA acumulado corretamente")
    void calculateAccumulatedIpca_ShouldCalculateCorrectly() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 31);

        BacenIpcaDtoResponse[] mockResponse = new BacenIpcaDtoResponse[] {
                new BacenIpcaDtoResponse("01/01/2023", "1.0"), // 1%
                new BacenIpcaDtoResponse("01/02/2023", "2.0"), // 2%
                new BacenIpcaDtoResponse("01/03/2023", "1.5")  // 1.5%
        };

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(mockResponse);

        // When
        Double result = bacenService.calculateAccumulatedIpca(startDate, endDate);

        // Then
        assertNotNull(result);
        // Cálculo: (1 + 0.01) * (1 + 0.02) * (1 + 0.015) - 1 = 0.04565... * 100 = 4.565...%
        assertEquals(4.565, result, 0.01);
    }

    @Test
    @DisplayName("Deve retornar 0.0 quando não há dados de IPCA")
    void calculateAccumulatedIpca_WithNoData_ShouldReturnZero() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(new BacenIpcaDtoResponse[] {});

        // When
        Double result = bacenService.calculateAccumulatedIpca(startDate, endDate);

        // Then
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Deve calcular IPCA acumulado ignorando valores nulos")
    void calculateAccumulatedIpca_WithNullValues_ShouldIgnoreNulls() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 3, 31);

        BacenIpcaDtoResponse[] mockResponse = new BacenIpcaDtoResponse[] {
                new BacenIpcaDtoResponse("01/01/2023", "1.0"),
                new BacenIpcaDtoResponse("01/02/2023", null),
                new BacenIpcaDtoResponse("01/03/2023", "2.0")
        };

        when(restTemplate.getForObject(any(String.class), eq(BacenIpcaDtoResponse[].class)))
                .thenReturn(mockResponse);

        // When
        Double result = bacenService.calculateAccumulatedIpca(startDate, endDate);

        // Then
        assertNotNull(result);
        // Cálculo: (1 + 0.01) * (1 + 0.02) - 1 = 0.0302 * 100 = 3.02%
        assertEquals(3.02, result, 0.01);
    }
}
