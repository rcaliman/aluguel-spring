package imoveis.aluguel.services;

import imoveis.aluguel.dtos.EnergyTitleDtoResponse;
import imoveis.aluguel.entities.EnergyTitle;

public interface EnergyTitleService {

    public void save(EnergyTitle energyTitle);

    public EnergyTitleDtoResponse findLast();

}
