package imoveis.aluguel.services;

import imoveis.aluguel.entities.EnergyTitle;

public interface EnergyTitleService {

    public void save(EnergyTitle energyTitle);

    public EnergyTitle findLast();

}
