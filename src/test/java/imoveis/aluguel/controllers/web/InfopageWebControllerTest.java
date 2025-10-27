package imoveis.aluguel.controllers.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import imoveis.aluguel.services.PropertyLogService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.RentalCorrectionService;
import imoveis.aluguel.services.TenantService;

@WebMvcTest(InfoPageWebController.class)
@WithMockUser
class InfoPageWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {

        @Bean
        public PropertyService propertyService() {
            return mock(PropertyService.class);
        }

        @Bean
        public TenantService tenantService() {
            return mock(TenantService.class);
        }

        @Bean
        public PropertyLogService propertyLogService() {
            return mock(PropertyLogService.class);
        }

        @Bean
        public RentalCorrectionService rentalCorrectionService() {
            return mock(RentalCorrectionService.class);
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private PropertyLogService propertyLogService;
    @Autowired
    private RentalCorrectionService rentalCorrectionService;

    private Tenant tenant;
    private PropertyLog propertyLog;
    private imoveis.aluguel.entities.Property property;

    @BeforeEach
    void setUp() {

        tenant = new Tenant();
        tenant.setId(10L);
        tenant.setName("Inquilino Exemplo");
        tenant.setMaritalStatus(MaritalStatusEnum.SOLTEIRO);
        tenant.setContacts(new ArrayList<>());

        property = new imoveis.aluguel.entities.Property();
        property.setId(1L);
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setUseType(PropertyUseTypeEnum.RESIDENCIAL);
        property.setAddress("Rua dos Testes, 123");
        property.setNumber("123 A");

        propertyLog = new PropertyLog();
        propertyLog.setId(100L);
        propertyLog.setObservation("Contrato gerado");

    }

    @Test
    @DisplayName("GET /infopage/{id} - Deve retornar 200 OK se o imóvel não tiver inquilino")
    void info_ShouldReturnOk_WhenPropertyHasNoTenant() throws Exception {

        when(propertyService.findById(1L)).thenReturn(property);
        when(propertyLogService.findAllByPropertyId(1L)).thenReturn(new ArrayList<>());
        when(rentalCorrectionService.calculateCorrectedRentalValue(1L)).thenReturn(null);

        mockMvc.perform(get("/infopage/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("infopage/infopage"))
            .andExpect(model().attribute("property", property))
            .andExpect(model().attribute("tenant", org.hamcrest.Matchers.nullValue()));
    }
}