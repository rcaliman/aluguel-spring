package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import imoveis.aluguel.dtos.PropertyDtoResponse;
import imoveis.aluguel.dtos.PropertyDtoSumary;
import imoveis.aluguel.entities.Property;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyDtoResponse toDtoResponse(Property property);
    
    PropertyDtoSumary tDtoSumary(Property property);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propertyLogs", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(Property source, @MappingTarget Property target);

}
