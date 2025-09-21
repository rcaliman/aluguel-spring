package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.PersonDtoRequest;
import imoveis.aluguel.dtos.PersonDtoResponse;
import imoveis.aluguel.entities.Person;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface PersonMapper {
    
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDtoResponse toDtoResponse(Person person);
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "properties", ignore = true)
    Person toPerson(PersonDtoRequest personDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Person source, @MappingTarget Person target);

}
