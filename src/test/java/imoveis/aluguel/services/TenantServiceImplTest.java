package imoveis.aluguel.services;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {

    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private TenantMapper tenantMapper;

    @InjectMocks
    private TenantServiceImpl tenantService;

    private Tenant tenant;

    @BeforeEach
    void setUp() {
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("Inquilino Teste");

        Contact contact1 = new Contact();
        contact1.setContact("99999-1111");
        tenant.setContacts(new ArrayList<>(List.of(contact1)));
    }

    @Test
    @DisplayName("create - Deve associar os contatos ao inquilino antes de salvar")
    void create_ShouldSetBackReferenceOnContactsAndSave() {
        
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        tenantService.create(tenant);

        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepository).save(tenantCaptor.capture());

        Tenant capturedTenant = tenantCaptor.getValue();
        
        assertEquals(capturedTenant, capturedTenant.getContacts().get(0).getTenant());

    }

    @Test
    @DisplayName("update - Deve limpar contatos antigos e adicionar os novos")
    void update_ShouldReplaceContactsAndSaveChanges() {
        
        Tenant updatedTenantData = new Tenant();
        updatedTenantData.setName("Nome Atualizado");
        Contact newContact = new Contact();
        newContact.setContact("88888-2222");
        updatedTenantData.setContacts(List.of(newContact));

        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        tenantService.update(1L, updatedTenantData);

        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepository).save(tenantCaptor.capture());

        Tenant savedTenant = tenantCaptor.getValue();

        assertEquals(1, savedTenant.getContacts().size());
        assertEquals("88888-2222", savedTenant.getContacts().get(0).getContact());
        verify(tenantMapper).updateEntity(updatedTenantData, tenant);

    }

    @Test
    @DisplayName("findById - Deve lançar EntityNotFoundException se o inquilino não for encontrado")
    void findById_WhenNotFound_ShouldThrowException() {
        
        when(tenantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            tenantService.findById(99L);
        });

    }

    @Test
    @DisplayName("deleteById - Deve chamar o método delete do repositório")
    void deleteById_WhenFound_ShouldCallDelete() {
        
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));
        doNothing().when(tenantRepository).delete(tenant);

        tenantService.deleteById(1L);

        verify(tenantRepository, times(1)).delete(tenant);
        
    }
}