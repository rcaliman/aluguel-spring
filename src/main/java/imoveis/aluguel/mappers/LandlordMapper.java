package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import imoveis.aluguel.entities.Landlord;

@Mapper(componentModel = "spring")
public interface LandlordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    void updateEntity(Landlord source, @MappingTarget Landlord target);

}
