package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.enums.ContactTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities") // Garante que a entidade Contact seja encontrada
class ContactRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    @DisplayName("Deve persistir um contato e preencher createdAt via @PrePersist")
    void save_ShouldPersistContactAndSetCreatedAt() {

        Contact contact = new Contact();
        contact.setType(ContactTypeEnum.EMAIL);
        contact.setContact("teste@email.com");

        assertNull(contact.getCreatedAt());

        Contact savedContact = contactRepository.save(contact);

        assertNotNull(savedContact.getId());
        assertNotNull(savedContact.getCreatedAt());

    }

    @Test
    @DisplayName("Deve preencher updatedAt via @PreUpdate ao atualizar um contato")
    void update_ShouldSetUpdatedAt() {

        Contact contact = new Contact();
        contact.setType(ContactTypeEnum.CELULAR);
        contact.setContact("11111-1111");
        Contact persistedContact = entityManager.persistFlushFind(contact);

        assertNull(persistedContact.getUpdatedAt());

        persistedContact.setContact("22222-2222");
        Contact updatedContact = contactRepository.save(persistedContact);
        entityManager.flush();

        assertNotNull(updatedContact.getUpdatedAt(), "O campo updatedAt deveria ter sido preenchido.");

    }

    @Test
    @DisplayName("Deve falhar ao salvar um contato sem o campo 'type'")
    void save_WithoutType_ShouldThrowException() {

        Contact contact = new Contact();
        contact.setContact("contato sem tipo");

        assertThrows(DataIntegrityViolationException.class, () -> {
            contactRepository.saveAndFlush(contact);
        }, "Deveria falhar ao salvar um contato com o campo 'type' nulo.");
    }

    @Test
    @DisplayName("Deve falhar ao salvar um contato sem o campo 'contact'")
    void save_WithoutContactString_ShouldThrowException() {

        Contact contact = new Contact();
        contact.setType(ContactTypeEnum.TELEFONE);

        assertThrows(DataIntegrityViolationException.class, () -> {
            contactRepository.saveAndFlush(contact);
        }, "Deveria falhar ao salvar um contato com o campo 'contact' nulo.");
    }

}