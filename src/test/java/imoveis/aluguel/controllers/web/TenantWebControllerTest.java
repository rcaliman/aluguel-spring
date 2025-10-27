package imoveis.aluguel.controllers.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.ContactTypeEnum;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.repositories.TenantRepository;
import imoveis.aluguel.services.TenantService;

@WebMvcTest(TenantWebController.class)
@WithMockUser(authorities = "ADMIN")
class TenantWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private TenantRepository tenantRepository;

    @Test
    @DisplayName("GET /tenants - Deve exibir a lista de inquilinos")
    void listTenants_ShouldReturnListView() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("João Silva");

        when(tenantService.list(Sort.by("name"))).thenReturn(List.of(tenant));

        mockMvc.perform(get("/tenants"))
                .andExpect(status().isOk())
                .andExpect(view().name("tenant/list"))
                .andExpect(model().attributeExists("tenants", "currentPage"))
                .andExpect(model().attribute("tenants", hasSize(1)))
                .andExpect(model().attribute("currentPage", "tenants"));
    }

    @Test
    @DisplayName("GET /tenants/new - Deve exibir o formulário de novo inquilino")
    void showCreateForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/tenants/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tenant/form"))
                .andExpect(model().attributeExists("tenant", "maritalStatusOptions", "contactTypeOptions", "currentPage"))
                .andExpect(model().attribute("tenant", is(instanceOf(Tenant.class))))
                .andExpect(model().attribute("maritalStatusOptions", MaritalStatusEnum.values()))
                .andExpect(model().attribute("contactTypeOptions", ContactTypeEnum.values()));
    }

    @Test
    @DisplayName("GET /tenants/edit/{id} - Deve exibir o formulário de edição")
    void showEditForm_ShouldReturnFormViewWithTenant() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("João Silva");

        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));

        mockMvc.perform(get("/tenants/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tenant/form"))
                .andExpect(model().attributeExists("tenant", "maritalStatusOptions", "contactTypeOptions", "currentPage"))
                .andExpect(model().attribute("tenant", is(tenant)));

        verify(tenantRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /tenants/save - Deve criar um novo inquilino")
    void saveTenant_ForCreate_ShouldCallServiceAndRedirect() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setName("Novo Inquilino");

        when(tenantService.create(any(Tenant.class))).thenReturn(tenant);

        mockMvc.perform(post("/tenants/save").with(csrf())
                .param("name", "Novo Inquilino")
                .param("cpfCnpj", "111.222.333-44"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).create(any(Tenant.class));
        verify(tenantService, never()).update(anyLong(), any(Tenant.class));
    }

    @Test
    @DisplayName("POST /tenants/save - Deve atualizar inquilino existente")
    void saveTenant_ForUpdate_ShouldCallUpdateAndRedirect() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("Inquilino Atualizado");

        when(tenantService.update(anyLong(), any(Tenant.class))).thenReturn(tenant);

        mockMvc.perform(post("/tenants/save").with(csrf())
                .param("id", "1")
                .param("name", "Inquilino Atualizado"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenants"));

        verify(tenantService, never()).create(any(Tenant.class));
        verify(tenantService, times(1)).update(anyLong(), any(Tenant.class));
    }

    @Test
    @DisplayName("POST /tenants/save - Deve converter strings vazias em null")
    void saveTenant_ShouldConvertEmptyStringsToNull() throws Exception {
        when(tenantService.create(any(Tenant.class))).thenReturn(new Tenant());

        mockMvc.perform(post("/tenants/save").with(csrf())
                .param("name", "Test")
                .param("cpfCnpj", "")
                .param("document", "")
                .param("address", "")
                .param("location", "")
                .param("state", "")
                .param("city", "")
                .param("nationality", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).create(any(Tenant.class));
    }

    @Test
    @DisplayName("GET /tenants/delete/{id} - Deve deletar inquilino e redirecionar")
    void deleteTenant_ShouldCallDeleteAndRedirect() throws Exception {
        doNothing().when(tenantService).deleteById(1L);

        mockMvc.perform(get("/tenants/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenants"));

        verify(tenantService, times(1)).deleteById(1L);
    }
}
