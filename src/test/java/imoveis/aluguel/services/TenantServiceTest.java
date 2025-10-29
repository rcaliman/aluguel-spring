package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.repositories.TenantRepository;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock private TenantRepository tenantRepository;
    @Mock private TenantMapper tenantMapper;
    @InjectMocks private TenantService tenantService;

    @Test
    @DisplayName("Deve criar tenant")
    void create_ShouldSaveTenant() {
        Tenant tenant = new Tenant();
        tenant.setContacts(List.of());
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        Tenant result = tenantService.create(tenant);

        assertNotNull(result);
        verify(tenantRepository).save(tenant);
    }

    @Test
    @DisplayName("Deve buscar tenant por CPF/CNPJ")
    void findByCpfCnpj_ShouldReturnTenant() {
        Tenant tenant = new Tenant();
        when(tenantRepository.findByCpfCnpj("12345678901")).thenReturn(Optional.of(tenant));

        Tenant result = tenantService.findByCpfCnpj("12345678901");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar tenant inexistente")
    void findByCpfCnpj_WithNonExistent_ShouldThrowNotFoundException() {
        when(tenantRepository.findByCpfCnpj("99999999999")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> tenantService.findByCpfCnpj("99999999999"));
    }

    @Test
    @DisplayName("Deve buscar tenant por id")
    void findById_ShouldReturnTenant() {
        Tenant tenant = new Tenant();
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));

        Tenant result = tenantService.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve listar todos os tenants")
    void list_ShouldReturnAll() {
        Tenant tenant = new Tenant();
        tenant.setContacts(new ArrayList<>());
        tenant.setProperties(new ArrayList<>());
        when(tenantRepository.findAllWithProperties()).thenReturn(List.of(tenant));

        List<Tenant> result = tenantService.list(Sort.by("name"));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve deletar tenant")
    void deleteById_ShouldDelete() {
        Tenant tenant = new Tenant();
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));

        tenantService.deleteById(1L);

        verify(tenantRepository).delete(tenant);
    }
}
