package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.ContactDtoRequest;
import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.enums.ContactTypeEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class ContactMapperTest {

    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);

    @Test
    void shouldMapDtoToContact() {
        ContactDtoRequest dto = new ContactDtoRequest(ContactTypeEnum.EMAIL, "teste@email.com");

        Contact contact = contactMapper.toContact(dto);

        assertNotNull(contact);
        assertEquals(ContactTypeEnum.EMAIL, contact.getType());
        assertEquals("teste@email.com", contact.getContact());
        assertNull(contact.getId());
        assertNull(contact.getTenant());

    }
}