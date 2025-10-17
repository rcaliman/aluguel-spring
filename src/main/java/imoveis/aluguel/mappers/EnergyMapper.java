package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

@Mapper(componentModel = "spring", uses = {})
public interface EnergyMapper {

    @Mapping(target = "amount1", ignore = true)
    @Mapping(target = "amount2", ignore = true)
    @Mapping(target = "amount3", ignore = true)
    Energy toEnergy(EnergyDtoRequest energyDto);

    default EnergyDtoResponse toDtoResponse(Energy energy, boolean isLast) {

        return new EnergyDtoResponse(energy.getId(), energy.getCounter1(), energy.getCounter2(),
                energy.getCounter3(), energy.getAmount1(), energy.getAmount2(), energy.getAmount3(),
                energy.getKwhValue(), energy.getBillAmount(), energy.getDate(), isLast);
                
    }

}
