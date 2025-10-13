package imoveis.aluguel.controllers.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;
import jakarta.servlet.ServletException; // Adicione esta importação no topo do arquivo

@WebMvcTest(ContractWebController.class)
@WithMockUser
class ContractWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {
        @Bean
        public TenantMapper tenantMapper() {
            return mock(TenantMapper.class);
        }

        @Bean
        public LandlordService landlordService() {
            return mock(LandlordService.class);
        }

        @Bean
        public LandlordMapper landlordMapper() {
            return mock(LandlordMapper.class);
        }

        @Bean
        public PropertyService propertyService() {
            return mock(PropertyService.class);
        }

        @Bean
        public PropertyMapper propertyMapper() {
            return mock(PropertyMapper.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private LandlordMapper landlordMapper;
    @Autowired
    private PropertyMapper propertyMapper;

    private Property property;
    private Tenant tenant;
    private Landlord landlord;

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

        property = new Property();
        property.setId(1L);
        property.setAddress("Rua dos Testes");
        property.setLocation("Centro");
        property.setNumber("123");
        property.setCity("Colatina");
        property.setState("ES");
        property.setValue(1500.00);
        property.setPaymentDay("10");
        property.setUseType(PropertyUseTypeEnum.RESIDENCIAL);
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setTenant(tenant);
    }

    @Test
    @DisplayName("GET /form/{id} - Deve exibir o formulário de contrato quando o imóvel tem inquilino")
    void contractForm_ShouldDisplayForm_WhenPropertyHasTenant() throws Exception {

        when(propertyService.findById(1L)).thenReturn(property);
        when(landlordService.list(Sort.by("name"))).thenReturn(List.of(landlord));

        TenantDtoResponse tenantDto = new TenantDtoResponse(1L, "Inquilino Teste", null, "22.222.222-2",
                "111.111.111-11", null, "Rua do Inquilino", "Bairro", "Vila Velha", "ES", null, "Brasileiro(a)");

        PropertyDtoResponse propertyDto = new PropertyDtoResponse(property.getId(), property.getPropertyType(),
                property.getUseType(), property.getAddress(), property.getLocation(), property.getCity(),
                property.getState(), property.getNumber(), tenantDto, property.getValue(), property.getComplement(),
                property.getObservation(), property.getPaymentDay());

        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDto);
        when(tenantMapper.toDtoResponse(any(Tenant.class))).thenReturn(tenantDto);
        when(landlordMapper.toDtoResponse(any(Landlord.class))).thenReturn(new LandlordDtoResponse(10L, "Locador Teste",
                null, null, null, null, null, null, null, null, null, null, true));

        mockMvc.perform(get("/contracts/form/1")).andExpect(status().isOk()).andExpect(view().name("contract/form"))
                .andExpect(model().attributeExists("property", "tenant", "landlords", "propertyTypeEnum"));

    }

    @Test
    @DisplayName("GET /form/{id} - Deve redirecionar se o imóvel não tiver inquilino")
    void contractForm_ShouldRedirect_WhenPropertyHasNoTenant() throws Exception {

        property.setTenant(null);
        when(propertyService.findById(1L)).thenReturn(property);

        mockMvc.perform(get("/contracts/form/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties?error=ImovelVago"));

    }

    @Test
    @DisplayName("POST /editor - Deve gerar e exibir o editor de contrato com sucesso")
    void contractEditor_ShouldDisplayEditor_OnSuccess() throws Exception {

        when(propertyService.findById(1L)).thenReturn(property);
        when(landlordService.findById(10L)).thenReturn(landlord);

        mockMvc.perform(post("/contracts/editor").with(csrf()).param("propertyId", "1").param("landlordId", "10")
                .param("startMonth", "10").param("startYear", "2025").param("endMonth", "10").param("endYear", "2026"))
                .andExpect(status().isOk()).andExpect(view().name("contract/editor"))
                .andExpect(model().attributeExists("landlord", "tenant", "property", "period", "valorPorExtenso",
                        "dataAtualPorExtenso"))
                .andExpect(model().attribute("landlord", landlord)).andExpect(model().attribute("tenant", tenant));

    }

    @Test
    @DisplayName("POST /editor - Deve retornar erro 400 se o imóvel não tiver inquilino")
    void contractEditor_ShouldReturn400_WhenPropertyHasNoTenant() throws Exception {

        property.setTenant(null);
        when(propertyService.findById(1L)).thenReturn(property);
        when(landlordService.findById(10L)).thenReturn(landlord);

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