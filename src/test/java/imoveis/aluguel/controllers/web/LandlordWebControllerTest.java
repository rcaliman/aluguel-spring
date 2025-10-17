package imoveis.aluguel.controllers.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.enums.ContactTypeEnum;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;
import imoveis.aluguel.repositories.LandlordRepository;

@WebMvcTest(LandlordWebController.class)
@WithMockUser
class LandlordWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {

        @Bean
        public LandlordService landlordService() {
            return mock(LandlordService.class);
        }

        @Bean
        public PropertyRepository propertyRepository() {
            return mock(imoveis.aluguel.repositories.PropertyRepository.class);
        }

        @Bean
        public TenantRepository tenantRepository() {
            return mock(imoveis.aluguel.repositories.TenantRepository.class);
        }

        @Bean
        public LandlordRepository landlordRepository() {
            return mock(imoveis.aluguel.repositories.LandlordRepository.class);
        }

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LandlordService landlordService;

    @Test
    @DisplayName("GET /landlords - Deve exibir a lista de locadores")
    void listLandlords_ShouldReturnListView() throws Exception {

        imoveis.aluguel.dtos.LandlordDtoResponse landlordDtoResponse = new imoveis.aluguel.dtos.LandlordDtoResponse(1L, "Maria Souza",
                null, null, null, null, null, null, null, null, null, null, true);

        when(landlordService.list(Sort.by("name"))).thenReturn(List.of(landlordDtoResponse));

        mockMvc.perform(get("/landlords")).andExpect(status().isOk()).andExpect(view().name("landlord/list"))
                .andExpect(model().attributeExists("landlords")).andExpect(model().attribute("landlords", hasSize(1)))
                .andExpect(model().attribute("currentPage", "landlords"));
    }

    @Test
    @DisplayName("GET /landlords/new - Deve exibir o formulário de novo locador")
    void showCreateForm_ShouldReturnFormView() throws Exception {

        mockMvc.perform(get("/landlords/new")).andExpect(status().isOk()).andExpect(view().name("landlord/form"))
                .andExpect(model().attributeExists("landlord"))
                .andExpect(model().attribute("landlord", is(instanceOf(Landlord.class))))
                .andExpect(model().attribute("maritalStatusOptions", MaritalStatusEnum.values()))
                .andExpect(model().attribute("contactTypeOptions", ContactTypeEnum.values()));

    }


    @Test
    @DisplayName("POST /landlords/save - Deve criar um novo locador e redirecionar")
    void saveLandlord_ForCreate_ShouldCallServiceAndRedirect() throws Exception {

        when(landlordService.create(any(Landlord.class))).thenReturn(new imoveis.aluguel.dtos.LandlordDtoResponse(1L, "Novo Locador", null, null, null, null, null, null, null, null, null, null, true));

        mockMvc.perform(
                post("/landlords/save").with(csrf()).param("name", "Novo Locador").param("cpfCnpj", "111.222.333-44"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/landlords"));

        verify(landlordService, times(1)).create(any(Landlord.class));
        verify(landlordService, never()).update(anyLong(), any(Landlord.class));

    }
}