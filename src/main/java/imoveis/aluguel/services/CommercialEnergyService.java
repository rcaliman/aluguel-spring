package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;

public interface CommercialEnergyService {

    List<CommercialEnergyDtoResponse> listLasts();

    CommercialEnergyDtoResponse calculate(CommercialEnergy energy);

    CommercialEnergyDtoResponse edit(CommercialEnergy editedEnergy, Long id);

    CommercialEnergyDtoResponse findById(Long id);

}