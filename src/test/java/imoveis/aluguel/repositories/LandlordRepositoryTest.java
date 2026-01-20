package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Landlord;
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
@EntityScan(basePackages = "imoveis.aluguel.entities")
class LandlordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LandlordRepository landlordRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    @DisplayName("Deve persistir um locador e preencher createdAt via @PrePersist")
    void save_ShouldSetCreatedAtOnPersist() {

        Landlord landlord = new Landlord();
        landlord.setName("Novo Locador");
        assertNull(landlord.getCreatedAt());

        Landlord savedLandlord = landlordRepository.save(landlord);

        assertNotNull(savedLandlord.getId());
        assertNotNull(savedLandlord.getCreatedAt());

    }

    @Test
    @DisplayName("Deve persistir um locador e seus contatos em cascata")
    void save_WithContacts_ShouldPersistContactsInCascade() {

        Landlord landlord = new Landlord();
        landlord.setName("Locador com Contato");

        Contact contact = new Contact();
        contact.setType(ContactTypeEnum.EMAIL);
        contact.setContact("locador@email.com");
        contact.setLandlord(landlord);

        landlord.getContacts().add(contact);

        Landlord savedLandlord = landlordRepository.save(landlord);
        entityManager.flush();
        entityManager.clear();

        Landlord foundLandlord = landlordRepository.findById(savedLandlord.getId()).orElseThrow();
        assertEquals(1, foundLandlord.getContacts().size());
        assertEquals("locador@email.com", foundLandlord.getContacts().get(0).getContact());

    }

    @Test
    @DisplayName("Deve remover um contato do banco quando ele é removido da lista do locador (orphanRemoval)")
    void update_WhenContactIsRemovedFromList_ShouldDeleteContact() {

        Landlord landlord = new Landlord();
        landlord.setName("Locador com Contato");
        Contact contact = new Contact();
        contact.setType(ContactTypeEnum.CELULAR);
        contact.setContact("99999-9999");
        contact.setLandlord(landlord);
        landlord.getContacts().add(contact);

        Landlord savedLandlord = entityManager.persistFlushFind(landlord);
        assertEquals(1, contactRepository.count());

        savedLandlord.getContacts().clear(); // Remove o contato da lista
        landlordRepository.saveAndFlush(savedLandlord);
        entityManager.clear();

        Landlord foundLandlord = landlordRepository.findById(savedLandlord.getId()).orElseThrow();
        assertTrue(foundLandlord.getContacts().isEmpty(), "A lista de contatos deveria estar vazia.");
        assertEquals(0, contactRepository.count(), "O contato deveria ter sido deletado do banco de dados.");

    }

    @Test
    @DisplayName("Deve falhar ao salvar um locador sem o campo 'name'")
    void save_WhenNameIsNull_ShouldThrowException() {

        Landlord landlord = new Landlord();

        assertThrows(DataIntegrityViolationException.class, () -> {
            landlordRepository.saveAndFlush(landlord);
        });

    }

    @Test
    @DisplayName("Deve falhar ao salvar dois locadores com o mesmo CPF/CNPJ")
    void save_WhenCpfCnpjIsDuplicate_ShouldThrowException() {

        Landlord landlord1 = new Landlord();
        landlord1.setName("Locador Um");
        landlord1.setCpfCnpj("111.222.333-44");
        landlordRepository.saveAndFlush(landlord1);

        Landlord landlord2 = new Landlord();
        landlord2.setName("Locador Dois");
        landlord2.setCpfCnpj("111.222.333-44");

        assertThrows(DataIntegrityViolationException.class, () -> {
            landlordRepository.saveAndFlush(landlord2);
        });

    }
}
