package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.ContactDtoRequest;
import imoveis.aluguel.dtos.ContactDtoResponse;
import imoveis.aluguel.entities.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    
    ContactDtoResponse toDtoResponse(Contact contact);

    @Mapping(target = "id", ignore = true)         
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "landlord", ignore = true)
    @Mapping(target = "createdAt", ignore = true)  
    @Mapping(target = "updatedAt", ignore = true)  
    Contact toContact(ContactDtoRequest contactDto);
    
}
