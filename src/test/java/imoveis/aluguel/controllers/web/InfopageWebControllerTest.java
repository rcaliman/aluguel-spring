package imoveis.aluguel.controllers.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.dtos.PropertyLogDtoResponse;
import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.services.PropertyLogService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;
import jakarta.servlet.ServletException;

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
        public PropertyMapper propertyMapper() {
            return mock(PropertyMapper.class);
        }

        @Bean
        public TenantService tenantService() {
            return mock(TenantService.class);
        }

        @Bean
        public TenantMapper tenantMapper() {
            return mock(TenantMapper.class);
        }

        @Bean
        public PropertyLogService propertyLogService() {
            return mock(PropertyLogService.class);
        }

        @Bean
        public PropertyLogMapper propertyLogMapper() {
            return mock(PropertyLogMapper.class);
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyMapper propertyMapper;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private PropertyLogService propertyLogService;
    @Autowired
    private PropertyLogMapper propertyLogMapper;

    private Property property;
    private Tenant tenant;
    private PropertyLog propertyLog;

    @BeforeEach
    void setUp() {

        tenant = new Tenant();
        tenant.setId(10L);
        tenant.setName("Inquilino Exemplo");
        tenant.setMaritalStatus(MaritalStatusEnum.SOLTEIRO);
        tenant.setContacts(new ArrayList<>());

        property = new Property();
        property.setId(1L);
        property.setAddress("Rua dos Testes, 123");
        property.setNumber("123 A");
        property.setTenant(tenant);
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setUseType(PropertyUseTypeEnum.RESIDENCIAL);

        propertyLog = new PropertyLog();
        propertyLog.setId(100L);
        propertyLog.setObservation("Contrato gerado");

    }

    @Test
    @DisplayName("GET /infopage/{id} - Deve retornar 200 OK se o imóvel não tiver inquilino")
    void info_ShouldReturnOk_WhenPropertyHasNoTenant() throws Exception {

        property.setTenant(null);
        when(propertyService.findById(1L)).thenReturn(property);
        when(propertyLogService.findAllByPropertyId(1L)).thenReturn(new ArrayList<>());

        PropertyDtoResponse propertyDto = new PropertyDtoResponse(
                property.getId(), property.getPropertyType(), property.getUseType(), property.getAddress(),
                property.getLocation(), property.getCity(), property.getState(), property.getNumber(), null,
                property.getValue(), property.getComplement(), property.getObservation(), property.getPaymentDay()
        );
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDto);

        mockMvc.perform(get("/infopage/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("infopage/infopage"))
            .andExpect(model().attribute("property", propertyDto))
            .andExpect(model().attribute("tenant", org.hamcrest.Matchers.nullValue()));
    }
}