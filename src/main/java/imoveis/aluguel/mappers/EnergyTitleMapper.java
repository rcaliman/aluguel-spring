package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;

import imoveis.aluguel.dtos.EnergyTitleDtoResponse;
import imoveis.aluguel.entities.EnergyTitle;

@Mapper(componentModel = "spring")
public interface EnergyTitleMapper {

    EnergyTitleDtoResponse toDtoResponse (EnergyTitle energyTitle);
    
}
