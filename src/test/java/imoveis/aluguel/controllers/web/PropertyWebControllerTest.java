
package imoveis.aluguel.controllers.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;

@WebMvcTest(PropertyWebController.class)
@WithMockUser
public class PropertyWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private LandlordService landlordService;

    @MockBean
    private PropertyRepository propertyRepository;

    @Test
    public void testListProperties() throws Exception {
        when(propertyService.list(any(String.class))).thenReturn(Collections.emptyList());
        when(landlordService.list(any(Sort.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(view().name("property/list"))
                .andExpect(model().attributeExists("properties", "landlords", "years", "currentYear", "currentMonth", "currentPage"));
    }

    @Test
    public void testShowCreateForm() throws Exception {
        when(tenantService.list(any(Sort.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("property/form"))
                .andExpect(model().attributeExists("property", "tenants", "propertyTypes", "propertyUseTypes", "currentPage"));
    }

    @Test
    public void testSaveProperty_New() throws Exception {
        Property newProperty = new Property();
        PropertyDtoResponse dtoResponse = new PropertyDtoResponse(1L, null, null, null, null, null, null, null, null, null, null, null, null);
        when(propertyService.create(any(Property.class))).thenReturn(dtoResponse);

        mockMvc.perform(post("/properties/save").with(csrf()).flashAttr("property", newProperty))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    public void testSaveProperty_Update() throws Exception {
        Property existingProperty = new Property();
        existingProperty.setId(1L);
        PropertyDtoResponse dtoResponse = new PropertyDtoResponse(1L, null, null, null, null, null, null, null, null, null, null, null, null);
        when(propertyService.update(anyLong(), any(Property.class))).thenReturn(dtoResponse);

        mockMvc.perform(post("/properties/save").with(csrf()).flashAttr("property", existingProperty))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    public void testShowEditForm() throws Exception {
        Property property = new Property();
        property.setId(1L);
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(tenantService.list(any(Sort.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/properties/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("property/form"))
                .andExpect(model().attribute("property", property))
                .andExpect(model().attributeExists("tenants", "propertyTypes", "propertyUseTypes", "currentPage"));
    }

    @Test
    public void testDeleteProperty() throws Exception {
        doNothing().when(propertyService).deleteById(1L);

        mockMvc.perform(get("/properties/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }
}
