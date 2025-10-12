package imoveis.aluguel.services;

import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.repositories.EnergyTitleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnergyTitleServiceImpl implements EnergyTitleService {

    private final EnergyTitleRepository energyTitleRepository;

    @Override
    public void save(EnergyTitle energyTitle) {

        energyTitleRepository.save(energyTitle);

    }

    @Override
    public EnergyTitle findLast() {

        return energyTitleRepository.findTopByOrderByIdDesc();
    }

}
