package imoveis.aluguel.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;

@SpringBootTest
class TenantMapperTest {

    @Autowired
    private TenantMapper mapper;

    @Test
    @DisplayName("Deve atualizar entity com dados do source, mantendo id e contacts")
    void updateEntity_ShouldUpdateFieldsButKeepIdAndContacts() {
        // Given
        Tenant source = new Tenant();
        source.setId(999L); // Este ID não deve ser copiado
        source.setName("João Silva");
        source.setDocument("123456789");
        source.setCpfCnpj("12345678901");
        source.setDateOfBirth(LocalDate.of(1985, 3, 15));
        source.setAddress("Rua A, 123");
        source.setLocation("Centro");
        source.setState("SP");
        source.setCity("São Paulo");
        source.setMaritalStatus(MaritalStatusEnum.CASADO);
        source.setNationality("Brasileira");

        Tenant target = new Tenant();
        target.setId(1L); // Este ID deve ser mantido
        target.setName("Maria Santos");
        target.setCreatedAt(Instant.now());

        Contact contact = new Contact();
        target.setContacts(new ArrayList<>());
        target.getContacts().add(contact); // Estes contatos devem ser mantidos

        // When
        mapper.updateEntity(source, target);

        // Then
        assertNotNull(target);
        assertEquals(1L, target.getId()); // ID original mantido
        assertNotEquals(999L, target.getId()); // ID do source não copiado
        assertEquals("João Silva", target.getName());
        assertEquals("123456789", target.getDocument());
        assertEquals("12345678901", target.getCpfCnpj());
        assertEquals(LocalDate.of(1985, 3, 15), target.getDateOfBirth());
        assertEquals("Rua A, 123", target.getAddress());
        assertEquals("Centro", target.getLocation());
        assertEquals("SP", target.getState());
        assertEquals("São Paulo", target.getCity());
        assertEquals(MaritalStatusEnum.CASADO, target.getMaritalStatus());
        assertEquals("Brasileira", target.getNationality());
        assertEquals(1, target.getContacts().size()); // Contacts mantidos
    }

    @Test
    @DisplayName("Deve atualizar entity com campos nulos")
    void updateEntity_WithNullFields_ShouldUpdateCorrectly() {
        // Given
        Tenant source = new Tenant();
        source.setName("Pedro Costa");
        source.setCpfCnpj("98765432100");

        Tenant target = new Tenant();
        target.setId(2L);
        target.setName("Antigo Nome");
        target.setDocument("DOC123");
        target.setContacts(new ArrayList<>());

        // When
        mapper.updateEntity(source, target);

        // Then
        assertEquals(2L, target.getId());
        assertEquals("Pedro Costa", target.getName());
        assertEquals("98765432100", target.getCpfCnpj());
        assertEquals(0, target.getContacts().size());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos exceto id e contacts")
    void updateEntity_ShouldUpdateAllFieldsExceptIdAndContacts() {
        // Given
        Tenant source = new Tenant();
        source.setName("Ana Paula");
        source.setDocument("987654321");
        source.setCpfCnpj("11122233344");
        source.setDateOfBirth(LocalDate.of(1990, 7, 25));
        source.setAddress("Av. B, 456");
        source.setLocation("Zona Norte");
        source.setState("RJ");
        source.setCity("Rio de Janeiro");
        source.setMaritalStatus(MaritalStatusEnum.SOLTEIRO);
        source.setNationality("Brasileira");

        Tenant target = new Tenant();
        target.setId(3L);

        // When
        mapper.updateEntity(source, target);

        // Then
        assertEquals(3L, target.getId());
        assertEquals("Ana Paula", target.getName());
        assertEquals("987654321", target.getDocument());
        assertEquals("11122233344", target.getCpfCnpj());
        assertEquals(LocalDate.of(1990, 7, 25), target.getDateOfBirth());
        assertEquals("Av. B, 456", target.getAddress());
        assertEquals("Zona Norte", target.getLocation());
        assertEquals("RJ", target.getState());
        assertEquals("Rio de Janeiro", target.getCity());
        assertEquals(MaritalStatusEnum.SOLTEIRO, target.getMaritalStatus());
        assertEquals("Brasileira", target.getNationality());
    }

    @Test
    @DisplayName("Deve preservar a lista de contacts original ao atualizar")
    void updateEntity_ShouldPreserveContactsList() {
        // Given
        Tenant source = new Tenant();
        source.setName("Carlos Souza");
        source.setCpfCnpj("55566677788");

        Contact contact1 = new Contact();
        Contact contact2 = new Contact();

        Tenant target = new Tenant();
        target.setId(4L);
        target.setName("Nome Original");
        target.setContacts(new ArrayList<>());
        target.getContacts().add(contact1);
        target.getContacts().add(contact2);

        // When
        mapper.updateEntity(source, target);

        // Then
        assertEquals(4L, target.getId());
        assertEquals("Carlos Souza", target.getName());
        assertEquals(2, target.getContacts().size());
        assertEquals(contact1, target.getContacts().get(0));
        assertEquals(contact2, target.getContacts().get(1));
    }
}
