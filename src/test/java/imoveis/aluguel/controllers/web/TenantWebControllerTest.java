package imoveis.aluguel.controllers.web;

import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.services.TenantService;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;
import imoveis.aluguel.repositories.LandlordRepository;

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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TenantWebController.class)
@WithMockUser
class TenantWebControllerTest {

    @TestConfiguration
    static class ControllerTestConfiguration {
        @Bean
        public TenantService tenantService() {
            return mock(TenantService.class);
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
    private TenantService tenantService;
    @Autowired
    private TenantRepository tenantRepository;

    @BeforeEach
    void setUp() {

        reset(tenantService, tenantRepository);

    }

    @Test
    @DisplayName("GET /tenants - Deve exibir a página de listagem de inquilinos")
    void listTenants_ShouldReturnListView() throws Exception {

        when(tenantService.list(Sort.by("name"))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tenants")).andExpect(status().isOk()).andExpect(view().name("tenant/list"))
                .andExpect(model().attributeExists("tenants", "currentPage"));

    }

    @Test
    @DisplayName("GET /tenants/new - Deve exibir o formulário de criação")
    void showCreateForm_ShouldReturnFormView() throws Exception {

        mockMvc.perform(get("/tenants/new")).andExpect(status().isOk()).andExpect(view().name("tenant/form"))
                .andExpect(model().attributeExists("tenant", "maritalStatusOptions", "contactTypeOptions"));

    }

    @Test
    @DisplayName("POST /tenants/save (Create) - Deve chamar o serviço de criação e redirecionar")
    void saveTenant_ForCreate_ShouldCallCreateAndRedirect() throws Exception {

        mockMvc.perform(post("/tenants/save").with(csrf()).param("name", "Novo Inquilino"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).create(any(Tenant.class));
        verify(tenantService, never()).update(anyLong(), any(Tenant.class));

    }

    @Test
    @DisplayName("POST /tenants/save (Update) - Deve chamar o serviço de atualização e redirecionar")
    void saveTenant_ForUpdate_ShouldCallUpdateAndRedirect() throws Exception {

        Tenant tenantToUpdate = new Tenant();
        tenantToUpdate.setId(1L);

        mockMvc.perform(post("/tenants/save").with(csrf()).flashAttr("tenant", tenantToUpdate).param("name",
                "Inquilino Atualizado")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).update(eq(1L), any(Tenant.class));
        verify(tenantService, never()).create(any(Tenant.class));

    }

    @Test
    @DisplayName("GET /tenants/delete/{id} - Deve chamar o serviço de exclusão e redirecionar")
    void deleteTenant_ShouldCallDeleteAndRedirect() throws Exception {

        mockMvc.perform(get("/tenants/delete/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("GET /tenants/edit/{id} - Deve exibir o formulário de edição com inquilino")
    void showEditForm_ShouldReturnFormViewWithTenant() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("João Silva");

        when(tenantRepository.findById(1L)).thenReturn(java.util.Optional.of(tenant));

        mockMvc.perform(get("/tenants/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tenant/form"))
                .andExpect(model().attributeExists("tenant", "maritalStatusOptions", "contactTypeOptions"));

        verify(tenantRepository, times(1)).findById(1L);
    }

}