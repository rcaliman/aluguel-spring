package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;

@Mapper(componentModel = "spring")
public interface CommercialEnergyMapper {

    @Mapping(target = "isLast", ignore = true)
    CommercialEnergyDtoResponse toResponse(CommercialEnergy commercialEnergy);

    @Mapping(target = "calculatedConsumption1", ignore = true)
    @Mapping(target = "calculatedConsumption2", ignore = true)
    CommercialEnergy toEntity(imoveis.aluguel.dtos.CommercialEnergyDtoRequest commercialEnergyDtoRequest);

    default CommercialEnergyDtoResponse toCommercialEnergyDtoResponseList(CommercialEnergy commercialEnergy, Boolean isLast) {
        
        return new CommercialEnergyDtoResponse(
            commercialEnergy.getId(),
            commercialEnergy.getDate(), 
            commercialEnergy.getAmount1(), 
            commercialEnergy.getAmount2(), 
            commercialEnergy.getInternalCounter(), 
            commercialEnergy.getAccountValue(), 
            commercialEnergy.getAccountConsumption(), 
            commercialEnergy.getCalculatedConsumption1(), 
            commercialEnergy.getCalculatedConsumption2(), 
            isLast
            );
    }
}