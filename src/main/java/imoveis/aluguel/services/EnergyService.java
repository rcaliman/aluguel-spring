package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;

public interface EnergyService {

    List<EnergyDtoResponse> listLasts();

    EnergyDtoResponse calculate(Energy energy);

    EnergyDtoResponse edit(Energy editedEnergy, Long id);

    EnergyDtoResponse findById(Long id);

}
