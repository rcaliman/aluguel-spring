package imoveis.aluguel.controllers.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.dtos.TenantDtoSumary;
import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;

@WebMvcTest(ContractWebController.class)
@WithMockUser
class ContractWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {
        @Bean
        public LandlordService landlordService() {
            return mock(LandlordService.class);
        }

        @Bean
        public PropertyService propertyService() {
            return mock(PropertyService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private LandlordService landlordService;

    private Tenant tenant;
    private Landlord landlord;
    private PropertyDtoResponse propertyDto;

    @BeforeEach
    void setUp() {
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("Inquilino Teste");
        tenant.setNationality("Brasileiro(a)");
        tenant.setCpfCnpj("111.111.111-11");
        tenant.setDocument("22.222.222-2");
        tenant.setCity("Vila Velha");
        tenant.setState("ES");

        landlord = new Landlord();
        landlord.setId(10L);
        landlord.setName("Locador Teste");
        landlord.setNationality("Brasileiro(a)");
        landlord.setMaritalStatus(MaritalStatusEnum.SOLTEIRO);
        landlord.setCity("Colatina");
        landlord.setState("ES");
        landlord.setCpfCnpj("333.333.333-33");

        TenantDtoSumary tenantDtoSumary = new TenantDtoSumary(1L, "Inquilino Teste", null, "22.222.222-2",
                "111.111.111-11", null, "Rua do Inquilino", "Bairro", "Vila Velha", "ES", null, "Brasileiro(a)");

        propertyDto = new PropertyDtoResponse(1L, PropertyTypeEnum.APARTAMENTO,
                PropertyUseTypeEnum.RESIDENCIAL, "Rua dos Testes", "Centro", "Colatina",
                "ES", "123", tenantDtoSumary, 1500.00, null,
                null, "10");
    }

    @Test
    @DisplayName("GET /form/{id} - Deve exibir o formulário de contrato quando o imóvel tem inquilino")
    void contractForm_ShouldDisplayForm_WhenPropertyHasTenant() throws Exception {

        LandlordDtoResponse landlordDtoResponse = new LandlordDtoResponse(10L, "Locador Teste",
                null, null, null, null, null, null, null, null, null, null, true);

        when(propertyService.findById(1L)).thenReturn(propertyDto);
        when(landlordService.list(Sort.by("name"))).thenReturn(List.of(landlordDtoResponse));

        mockMvc.perform(get("/contracts/form/1")).andExpect(status().isOk()).andExpect(view().name("contract/form"))
                .andExpect(model().attributeExists("property", "tenant", "landlords", "propertyTypeEnum"));

    }

    @Test
    @DisplayName("GET /form/{id} - Deve redirecionar se o imóvel não tiver inquilino")
    void contractForm_ShouldRedirect_WhenPropertyHasNoTenant() throws Exception {

        PropertyDtoResponse propertyWithoutTenant = new PropertyDtoResponse(1L, PropertyTypeEnum.APARTAMENTO,
                PropertyUseTypeEnum.RESIDENCIAL, "Rua dos Testes", "Centro", "Colatina",
                "ES", "123", null, 1500.00, null,
                null, "10");

        when(propertyService.findById(1L)).thenReturn(propertyWithoutTenant);

        mockMvc.perform(get("/contracts/form/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties?error=ImovelVago"));

    }

    @Test
    @DisplayName("POST /editor - Deve gerar e exibir o editor de contrato com sucesso")
    void contractEditor_ShouldDisplayEditor_OnSuccess() throws Exception {

        LandlordDtoResponse landlordDtoResponse = new LandlordDtoResponse(
                10L,
                "Locador Teste",
                List.of(),
                "MG-12.345.678",
                "333.333.333-33",
                null,
                "Rua dos Locadores",
                "Centro",
                "Colatina",
                "ES",
                MaritalStatusEnum.SOLTEIRO,
                "Brasileiro(a)",
                true);

        when(propertyService.findById(1L)).thenReturn(propertyDto);

        when(landlordService.findById(10L)).thenReturn(landlordDtoResponse);
        mockMvc.perform(post("/contracts/editor").with(csrf()).param("propertyId", "1").param("landlordId", "10")
                .param("startMonth", "10").param("startYear", "2025").param("endMonth", "10").param("endYear", "2026"))
                .andExpect(status().isOk()).andExpect(view().name("contract/editor"))
                .andExpect(model().attributeExists("landlord", "tenant", "property", "period", "valorPorExtenso",
                        "dataAtualPorExtenso"))
                .andExpect(model().attribute("landlord", landlordDtoResponse));

    }

    @Test
    @DisplayName("POST /editor - Deve retornar erro 400 se o imóvel não tiver inquilino")
    void contractEditor_ShouldReturn400_WhenPropertyHasNoTenant() throws Exception {

        LandlordDtoResponse landlordDtoResponse = new LandlordDtoResponse(10L, "Locador Teste",
                null, null, null, null, null, null, null, null, null, null, true);
        
        PropertyDtoResponse propertyWithoutTenant = new PropertyDtoResponse(1L, PropertyTypeEnum.APARTAMENTO,
                PropertyUseTypeEnum.RESIDENCIAL, "Rua dos Testes", "Centro", "Colatina",
                "ES", "123", null, 1500.00, null,
                null, "10");

        when(propertyService.findById(1L)).thenReturn(propertyWithoutTenant);
        when(landlordService.findById(10L)).thenReturn(landlordDtoResponse);

        mockMvc.perform(post("/contracts/editor")
                .with(csrf())
                .param("propertyId", "1")
                .param("landlordId", "10")
                .param("startMonth", "10")
                .param("startYear", "2025")
                .param("endMonth", "10")
                .param("endYear", "2026"))
                .andExpect(status().isBadRequest());

    }
}