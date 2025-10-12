package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import imoveis.aluguel.dtos.LandlordDtoRequest;
import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.entities.Landlord;

@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface LandlordMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Landlord toLandlord(LandlordDtoRequest landlordDto);

    LandlordDtoResponse toDtoResponse(Landlord landlord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    void updateEntity(Landlord source, @MappingTarget Landlord target);

}
