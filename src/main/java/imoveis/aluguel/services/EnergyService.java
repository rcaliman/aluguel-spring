package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.entities.Energy;

public interface EnergyService {

    List<Energy> listLasts();

    Energy calculate(Energy energy);

    Energy edit(Energy editedEnergy, Long id);

    Energy findById(Long id);
    
}
