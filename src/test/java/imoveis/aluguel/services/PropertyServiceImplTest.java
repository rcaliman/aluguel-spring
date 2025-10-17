package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.repositories.TenantRepository;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private PropertyLogMapper propertyLogMapper;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property property;
    private Tenant tenant;
    private PropertyDtoResponse propertyDtoResponse;

    @BeforeEach
    void setUp() {
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("inquilino");

        property = new Property();
        property.setId(1L);
        property.setNumber("123");
        property.setTenant(tenant);
        property.setPropertyLogs(new ArrayList<>());

        propertyDtoResponse = new PropertyDtoResponse(1L, null, null, null, null, null, null, "123", null, null, null, null, null);
    }

    @Test
    @DisplayName("create - Deve criar um imóvel e adicionar um log")
    void create_ShouldCreatePropertyAndAddLog() {
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDtoResponse);

        PropertyDtoResponse result = propertyService.create(property);

        assertNotNull(result);
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    @DisplayName("findById - Deve encontrar um imóvel pelo ID")
    void findById_WhenPropertyExists_ShouldReturnProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDtoResponse);

        PropertyDtoResponse result = propertyService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    @DisplayName("findById - Deve lançar NotFoundException se o imóvel não for encontrado")
    void findById_WhenPropertyDoesNotExist_ShouldThrowException() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            propertyService.findById(99L);
        });
    }

    @Test
    @DisplayName("update - Deve atualizar um imóvel e adicionar um log")
    void update_ShouldUpdatePropertyAndAddLog() {
        Property updatedProperty = new Property();
        updatedProperty.setNumber("456");
        updatedProperty.setTenant(tenant);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDtoResponse);

        PropertyDtoResponse result = propertyService.update(1L, updatedProperty);

        assertNotNull(result);
        verify(propertyMapper, times(1)).updateEntity(updatedProperty, property);
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    @DisplayName("update - Deve desvincular o inquilino se o inquilino for nulo")
    void update_WhenTenantIsNull_ShouldUnsetTenant() {
        Property updatedProperty = new Property();
        updatedProperty.setTenant(null);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyLogMapper.toPropertyLog(any(Property.class))).thenReturn(new PropertyLog());
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(new PropertyDtoResponse(1L, null, null, null, null, null, null, "123", null, null, null, null, null));

        PropertyDtoResponse result = propertyService.update(1L, updatedProperty);

        assertNull(result.tenant());
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    @DisplayName("list - Deve retornar uma lista de imóveis ordenada")
    void list_ShouldReturnSortedListOfProperties() {
        List<Property> properties = List.of(property, new Property());
        when(propertyRepository.findAllOrderByTenantNameAsc()).thenReturn(properties);
        when(propertyMapper.toDtoResponse(any(Property.class))).thenReturn(propertyDtoResponse);

        List<PropertyDtoResponse> result = propertyService.list("tenant.name");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(propertyRepository, times(1)).findAllOrderByTenantNameAsc();
    }

    @Test
    @DisplayName("deleteById - Deve deletar um imóvel pelo ID")
    void deleteById_WhenPropertyExists_ShouldDeleteProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        doNothing().when(propertyRepository).delete(property);

        propertyService.deleteById(1L);

        verify(propertyRepository, times(1)).delete(property);
    }

    @Test
    @DisplayName("deleteById - Deve lançar NotFoundException se o imóvel não for encontrado")
    void deleteById_WhenPropertyDoesNotExist_ShouldThrowException() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            propertyService.deleteById(99L);
        });
    }
}