package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock private PropertyRepository propertyRepository;
    @Mock private TenantRepository tenantRepository;
    @Mock private PropertyMapper propertyMapper;
    @Mock private PropertyLogMapper propertyLogMapper;
    @InjectMocks private PropertyService propertyService;

    @Test
    @DisplayName("Deve criar property")
    void create_ShouldSaveProperty() {
        Property property = new Property();
        PropertyLog log = new PropertyLog();
        when(propertyLogMapper.toPropertyLog(any())).thenReturn(log);
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property result = propertyService.create(property);

        assertNotNull(result);
        verify(propertyRepository).save(property);
    }

    @Test
    @DisplayName("Deve buscar property por id")
    void findById_ShouldReturnProperty() {
        Property property = new Property();
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        Property result = propertyService.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar property inexistente")
    void findById_WithNonExistent_ShouldThrowNotFoundException() {
        when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> propertyService.findById(999L));
    }

    @Test
    @DisplayName("Deve listar properties ordenadas por tenant name")
    void list_ByTenantName_ShouldReturnOrdered() {
        when(propertyRepository.findAllOrderByTenantNameAsc()).thenReturn(List.of(new Property()));

        List<Property> result = propertyService.list("tenant.name");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve deletar property")
    void deleteById_ShouldDelete() {
        Property property = new Property();
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.deleteById(1L);

        verify(propertyRepository).delete(property);
    }
}
