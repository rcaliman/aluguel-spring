package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.PropertyDtoRequest;
import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.entities.Property;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyMapper INSTANCE = Mappers.getMapper(PropertyMapper.class);
    
    PropertyDtoResponse toDtoResponse(Property property);
    
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore =true)
    @Mapping(target = "updatedAt", ignore = true)
    Property toProperty(PropertyDtoRequest propertyDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Property source, @MappingTarget Property target);

}
