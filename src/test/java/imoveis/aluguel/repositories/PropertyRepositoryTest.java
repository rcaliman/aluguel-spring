package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.PropertyTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; // <-- IMPORTAR ESTA CLASSE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class PropertyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Test
    @DisplayName("Deve persistir um imóvel e preencher createdAt via @PrePersist")
    void save_ShouldPersistPropertyAndSetCreatedAt() {

        Property property = new Property();
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);

        assertNull(property.getCreatedAt());

        Property savedProperty = propertyRepository.save(property);

        assertNotNull(savedProperty.getId());
        assertNotNull(savedProperty.getCreatedAt());

    }

    @Test
    @DisplayName("Deve setar o tenant de um imóvel para null quando o tenant for deletado")
    void onDelete_WhenTenantIsDeleted_PropertyTenantShouldBeNull() {

        Tenant tenant = new Tenant();
        tenant.setName("Inquilino a ser deletado");
        Tenant savedTenant = tenantRepository.save(tenant);

        Property property = new Property();
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        property.setTenant(savedTenant);
        Property savedProperty = propertyRepository.save(property);

        entityManager.flush();
        entityManager.clear();

        tenantRepository.deleteById(savedTenant.getId());
        entityManager.flush();
        entityManager.clear();

        Property foundProperty = propertyRepository.findById(savedProperty.getId()).orElse(null);

        assertNotNull(foundProperty);
        assertNull(foundProperty.getTenant(), "O inquilino do imóvel deveria ser nulo após a exclusão.");

    }
}