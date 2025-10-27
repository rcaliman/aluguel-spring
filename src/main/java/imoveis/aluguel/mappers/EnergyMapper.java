package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

@Mapper(componentModel = "spring", uses = {})
public interface EnergyMapper {


    default EnergyDtoResponse toDtoResponse(Energy energy, boolean isLast) {

        return new EnergyDtoResponse(energy.getId(), energy.getCounter1(), energy.getCounter2(),
                energy.getCounter3(), energy.getAmount1(), energy.getAmount2(), energy.getAmount3(),
                energy.getKwhValue(), energy.getBillAmount(), energy.getDate(), isLast);
                
    }

}
