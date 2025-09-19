package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

@Mapper(componentModel = "spring")
public interface EnergyMapper {
    
    EnergyMapper INSTANCE = Mappers.getMapper(EnergyMapper.class);

    Energy toEnergy(EnergyDtoRequest energyDto);


    @Mapping(target = "last", constant = "false")
    EnergyDtoResponse toDtoResponse(Energy energy);
    
    default EnergyDtoResponse toDtoResponse(Energy energy, boolean isLast) {
        return new EnergyDtoResponse(
            energy.getId(),
            energy.getCounter1(),
            energy.getCounter2(),
            energy.getCounter3(),
            energy.getAmount1(),
            energy.getAmount2(),
            energy.getAmount3(),
            energy.getKwhValue(),
            energy.getBillAmount(),
            energy.getDate(),
            isLast
        );
    }

}
