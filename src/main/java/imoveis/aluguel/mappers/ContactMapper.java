package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.ContactDtoRequest;
import imoveis.aluguel.dtos.ContactDtoResponse;
import imoveis.aluguel.entities.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);
    
    ContactDtoResponse toDtoResponse(Contact contact);

    Contact toContact(ContactDtoRequest contactDto);
    
}
