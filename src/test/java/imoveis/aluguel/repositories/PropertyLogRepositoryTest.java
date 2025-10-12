package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.PropertyLog;
import imoveis.aluguel.enums.PropertyTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class PropertyLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PropertyLogRepository propertyLogRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    @DisplayName("Deve persistir um log e preencher createdAt via @PrePersist")
    void save_ShouldPersistLogAndSetCreatedAt() {

        PropertyLog log = new PropertyLog();
        log.setTenantName("Inquilino Teste");
        log.setValue(1200.0);

        assertNull(log.getCreatedAt());

        PropertyLog savedLog = propertyLogRepository.save(log);

        assertNotNull(savedLog.getId(), "O ID não deveria ser nulo após salvar.");
        assertNotNull(savedLog.getCreatedAt(), "O campo createdAt deveria ter sido preenchido pelo @PrePersist.");

    }

    @Test
    @DisplayName("Deve persistir um log com a referência correta a um Property")
    void save_WithPropertyRelationship_ShouldPersistCorrectly() {

        Property property = new Property();
        property.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        Property savedProperty = propertyRepository.save(property);

        PropertyLog log = new PropertyLog();
        log.setTenantName("Inquilino do Imóvel " + savedProperty.getId());
        log.setProperty(savedProperty);

        PropertyLog savedLog = propertyLogRepository.save(log);
        entityManager.flush();
        entityManager.clear();

        PropertyLog foundLog = propertyLogRepository.findById(savedLog.getId()).orElse(null);

        assertNotNull(foundLog, "O log deveria ter sido encontrado no banco.");
        assertNotNull(foundLog.getProperty(), "A propriedade associada ao log não deveria ser nula.");
        assertEquals(savedProperty.getId(), foundLog.getProperty().getId(),
                "O ID da propriedade associada está incorreto.");

    }
}