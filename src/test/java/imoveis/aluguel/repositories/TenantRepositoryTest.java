package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.ContactTypeEnum;
import imoveis.aluguel.enums.PropertyTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class TenantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    @DisplayName("Deve persistir um inquilino e preencher createdAt via @PrePersist")
    void save_ShouldSetCreatedAtOnPersist() {

        Tenant tenant = new Tenant();
        tenant.setName("Novo Inquilino");
        assertNull(tenant.getCreatedAt());

        Tenant savedTenant = tenantRepository.save(tenant);

        assertNotNull(savedTenant.getId());
        assertNotNull(savedTenant.getCreatedAt());

    }

    @Test
    @DisplayName("Deve persistir um inquilino com seus contatos em cascata")
    void saveWithContacts_ShouldPersistContactsInCascade() {

        Tenant tenant = new Tenant();
        tenant.setName("Inquilino com Contato");

        Contact contact = new Contact();

        contact.setType(ContactTypeEnum.CELULAR);
        contact.setContact("99999-8888");
        contact.setTenant(tenant);

        tenant.getContacts().add(contact);

        Tenant savedTenant = tenantRepository.save(tenant);

        entityManager.flush();
        entityManager.clear();

        Tenant foundTenant = tenantRepository.findById(savedTenant.getId()).orElseThrow();

        assertEquals(1, foundTenant.getContacts().size());
        assertEquals("99999-8888", foundTenant.getContacts().get(0).getContact());
    }

    @Test
    @DisplayName("Deve anular a referência ao inquilino nos imóveis ao deletá-lo (@PreRemove)")
    void preRemove_WhenTenantIsDeleted_ShouldNullifyTenantInProperties() {

        Tenant tenant = new Tenant();
        tenant.setName("Inquilino com Imóvel");
        Tenant savedTenant = tenantRepository.save(tenant);

        Property property = new Property();
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setTenant(savedTenant);
        Property savedProperty = propertyRepository.save(property);

        tenantRepository.deleteById(savedTenant.getId());
        entityManager.flush();
        entityManager.clear();

        Property foundProperty = propertyRepository.findById(savedProperty.getId()).orElseThrow();
        assertNull(foundProperty.getTenant(), "O imóvel deveria estar sem inquilino após a exclusão.");

    }

    @Test
    @DisplayName("Deve falhar ao tentar salvar dois inquilinos com o mesmo CPF/CNPJ")
    void saveDuplicateCpfCnpj_ShouldThrowException() {

        Tenant tenant1 = new Tenant();

        tenant1.setName("Inquilino Um");
        tenant1.setCpfCnpj("111.111.111-11");
        tenantRepository.saveAndFlush(tenant1);

        Tenant tenant2 = new Tenant();

        tenant2.setName("Inquilino Dois");
        tenant2.setCpfCnpj("111.111.111-11");

        assertThrows(DataIntegrityViolationException.class, () -> {
            tenantRepository.saveAndFlush(tenant2);
        });

    }
}