package imoveis.aluguel.controllers.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;

@WebMvcTest(PropertyWebController.class)
@WithMockUser
class PropertyWebControllerTest {

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
        public LandlordService landlordService() {
            return mock(LandlordService.class);
        }
    }

    @BeforeEach
    void setUp() {

        reset(propertyService, tenantService, landlordService);

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private LandlordService landlordService;

    @Test
    @DisplayName("GET /properties - Deve exibir a página de listagem de imóveis")
    void listProperties_ShouldReturnListViewWithData() throws Exception {

        when(propertyService.list(anyString())).thenReturn(Collections.emptyList());
        when(landlordService.list(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties")).andExpect(status().isOk()).andExpect(view().name("property/list"))
                .andExpect(model().attributeExists("properties", "landlords", "years", "currentPage"));

    }

    @Test
    @DisplayName("GET /properties/new - Deve exibir o formulário de criação de imóvel")
    void showCreateForm_ShouldReturnFormView() throws Exception {

        when(tenantService.list(any())).thenReturn(Collections.singletonList(new Tenant()));

        mockMvc.perform(get("/properties/new")).andExpect(status().isOk()).andExpect(view().name("property/form"))
                .andExpect(model().attributeExists("property", "tenants", "propertyTypes", "propertyUseTypes",
                        "currentPage"));

    }

    @Test
    @DisplayName("GET /properties/edit/{id} - Deve exibir o formulário de edição com os dados do imóvel")
    void showEditForm_WhenPropertyExists_ShouldReturnFormView() throws Exception {

        Property property = new Property();
        property.setId(1L);
        property.setAddress("Rua Existente");

        when(propertyService.findById(1L)).thenReturn(property);
        when(tenantService.list(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties/edit/1")).andExpect(status().isOk()).andExpect(view().name("property/form"))
                .andExpect(model().attribute("property", property));

    }

    @Test
    @DisplayName("POST /properties/save (Create) - Deve chamar o serviço de criação e redirecionar")
    void saveProperty_ForCreate_ShouldCallCreateAndRedirect() throws Exception {

        mockMvc.perform(post("/properties/save").with(csrf()).param("address", "Nova Rua, 123").param("propertyType",
                PropertyTypeEnum.APARTAMENTO.name())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));

        verify(propertyService).create(any(Property.class));
        verify(propertyService, never()).update(anyLong(), any(Property.class));

    }

    @Test
    @DisplayName("POST /properties/save (Update) - Deve chamar o serviço de atualização e redirecionar")
    void saveProperty_ForUpdate_ShouldCallUpdateAndRedirect() throws Exception {

        Property propertyToUpdate = new Property();
        propertyToUpdate.setId(1L);

        mockMvc.perform(post("/properties/save").with(csrf()).flashAttr("property", propertyToUpdate)
                .param("address", "Rua Atualizada, 456").param("propertyType", PropertyTypeEnum.APARTAMENTO.name()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/properties"));

        verify(propertyService).update(eq(1L), any(Property.class));
        verify(propertyService, never()).create(any(Property.class));

    }

    @Test
    @DisplayName("GET /properties/delete/{id} - Deve chamar o serviço de exclusão e redirecionar")
    void deleteProperty_ShouldCallDeleteAndRedirect() throws Exception {

        mockMvc.perform(get("/properties/delete/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));

        verify(propertyService).deleteById(1L);

    }

    @Test
    @DisplayName("GET /properties - Deve usar o sort padrão quando um sort inválido for provido")
    void listProperties_ShouldUseDefaultSort_WhenSortFieldIsInvalid() throws Exception {
        when(propertyService.list(anyString())).thenReturn(Collections.emptyList());
        when(landlordService.list(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties").param("sortField", "invalidSortField"))
                .andExpect(status().isOk())
                .andExpect(view().name("property/list"));

        verify(propertyService).list("invalidSortField");
    }

    @Test
    @DisplayName("POST /properties/save (Create) - Deve chamar o serviço de criação e redirecionar com os dados")
    void saveProperty_ForCreate_ShouldCallCreateAndRedirectWithData() throws Exception {
        mockMvc.perform(post("/properties/save").with(csrf())
                .param("address", "Nova Rua, 123")
                .param("propertyType", PropertyTypeEnum.APARTAMENTO.name())
                .param("value", "1500.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));

        verify(propertyService).create(any(Property.class));
    }

    @Test
    @DisplayName("POST /properties/save (Update) - Deve chamar o serviço de atualização e redirecionar com os dados")
    void saveProperty_ForUpdate_ShouldCallUpdateAndRedirectWithData() throws Exception {
        Property propertyToUpdate = new Property();
        propertyToUpdate.setId(1L);

        mockMvc.perform(post("/properties/save").with(csrf())
                .flashAttr("property", propertyToUpdate)
                .param("address", "Rua Atualizada, 456")
                .param("propertyType", PropertyTypeEnum.APARTAMENTO.name())
                .param("value", "2000.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));

        verify(propertyService).update(eq(1L), any(Property.class));
    }
}