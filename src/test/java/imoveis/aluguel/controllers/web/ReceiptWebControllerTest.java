package imoveis.aluguel.controllers.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.exceptions.ValidationException;
import imoveis.aluguel.mappers.ReceiptMapper;
import imoveis.aluguel.services.ReceiptService;

@WebMvcTest(ReceiptWebController.class)
@WithMockUser
class ReceiptWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {
        @Bean
        public ReceiptService receiptService() {
            return mock(ReceiptService.class);
        }

        @Bean
        public ReceiptMapper receiptMapper() {
            return mock(ReceiptMapper.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private ReceiptMapper receiptMapper;

    @Test
    @DisplayName("POST /generate - Deve gerar recibos e exibir a página de lista")
    void generateReceipts_ShouldReturnListView_WhenDataIsValid() throws Exception {

        Receipt receipt = Receipt.builder().tenant("Inquilino Teste").build();
        ReceiptDtoResponse dtoResponse = new ReceiptDtoResponse("Inquilino Teste", null, null, PropertyTypeEnum.APARTAMENTO, null, null, null, null,
                null, null,null, null, null, null, null);

        when(receiptService.receipts(any(ReceiptDtoRequest.class))).thenReturn(List.of(receipt));
        when(receiptMapper.dtoResponse(any(Receipt.class))).thenReturn(dtoResponse);

        mockMvc.perform(post("/receipts/generate").with(csrf()).param("propertyIds", "1", "2")
                .param("landlordId", "100").param("month", "Janeiro").param("year", "2025")).andExpect(status().isOk())
                .andExpect(view().name("receipt/list")).andExpect(model().attributeExists("receipts", "currentPage"));
    }

    @Test
    @DisplayName("POST /generate - Deve lançar ValidationException quando propertyIds está ausente")
    void generateReceipts_ShouldThrowValidationException_WhenPropertyIdsAreMissing() throws Exception {
        mockMvc.perform(post("/receipts/generate").with(csrf())
                .param("landlordId", "100")
                .param("month", "Janeiro")
                .param("year", "2025"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("Nenhum imóvel foi selecionado", result.getResolvedException().getMessage()));
    }


}
