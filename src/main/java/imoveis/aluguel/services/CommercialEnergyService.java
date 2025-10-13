package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.entities.CommercialEnergy;

public interface CommercialEnergyService {

    List<CommercialEnergy> listLasts();

    CommercialEnergy calculate(CommercialEnergy energy);

    CommercialEnergy edit(CommercialEnergy editedEnergy, Long id);

    CommercialEnergy findById(Long id);

}