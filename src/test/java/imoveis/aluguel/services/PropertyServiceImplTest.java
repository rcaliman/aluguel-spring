package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @Mock private PropertyRepository propertyRepository;
    @Mock private TenantRepository tenantRepository;
    @Mock private PropertyMapper propertyMapper;
    @Mock private PropertyLogMapper propertyLogMapper;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property property;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        tenant = new Tenant();
        tenant.setId(10L);
        tenant.setName("Inquilino Teste");

        property = new Property();
        property.setId(1L);
        property.setAddress("Rua Teste, 123");
    }

    @Test
    @DisplayName("create - Deve adicionar um log e salvar o imóvel")
    void create_ShouldAddLogAndSaveProperty() {
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property savedProperty = propertyService.create(property);

        assertNotNull(savedProperty);
        assertFalse(savedProperty.getPropertyLogs().isEmpty());
        verify(propertyLogMapper).toPropertyLog(property);
        verify(propertyRepository).save(property);
    }

    @Test
    @DisplayName("update - Deve atualizar campos, associar novo inquilino e adicionar log")
    void update_ShouldUpdateFieldsAndTenant_AndAddLog() {
        Property updatedData = new Property();
        Tenant newTenant = new Tenant();
        newTenant.setId(20L);
        updatedData.setTenant(newTenant);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(tenantRepository.findById(20L)).thenReturn(Optional.of(newTenant));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());

        Property result = propertyService.update(1L, updatedData);

        assertNotNull(result);
        assertEquals(20L, result.getTenant().getId());
        assertFalse(result.getPropertyLogs().isEmpty());
        verify(propertyMapper).updateEntity(updatedData, property);
        verify(tenantRepository).findById(20L);
        verify(propertyRepository).save(property);
    }

    @Test
    @DisplayName("update - Deve desassociar inquilino quando o inquilino no request for nulo")
    void update_ToUnassignTenant_ShouldSetTenantToNull() {
        
        property.setTenant(tenant);
        Property updatedData = new Property();
        updatedData.setTenant(null);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());

        Property result = propertyService.update(1L, updatedData);

        assertNull(result.getTenant());
        verify(tenantRepository, never()).findById(anyLong());
        verify(propertyRepository).save(property);

    }
    
    @Test
    @DisplayName("deleteById - Deve deletar o imóvel se ele existir")
    void deleteById_WhenPropertyExists_ShouldDelete() {

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        doNothing().when(propertyRepository).delete(property);
        
        propertyService.deleteById(1L);
        
        verify(propertyRepository).delete(property);

    }
    
    @Test
    @DisplayName("deleteById - Deve lançar exceção se o imóvel não existir")
    void deleteById_WhenPropertyNotFound_ShouldThrowException() {

        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> {
            propertyService.deleteById(99L);
        });
        
        verify(propertyRepository, never()).delete(any());
        
    }
}