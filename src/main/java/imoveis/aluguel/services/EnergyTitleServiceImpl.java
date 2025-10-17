package imoveis.aluguel.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.EnergyTitleDtoResponse;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.mappers.EnergyTitleMapper;
import imoveis.aluguel.repositories.EnergyTitleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnergyTitleServiceImpl implements EnergyTitleService {

    private final EnergyTitleRepository energyTitleRepository;
    private final EnergyTitleMapper energyTitleMapper;

    @Override
    @CacheEvict(value = "energie-tiles", allEntries = true)
    public void save(EnergyTitle energyTitle) {

        energyTitleRepository.save(energyTitle);

    }

    @Override
    @Cacheable(value = "energie-tiles")
    public EnergyTitleDtoResponse findLast() {

        var titles = energyTitleRepository.findTopByOrderByIdDesc();

        titles = setDefaultTitles(titles);

        return energyTitleMapper.toDtoResponse(titles);
    }

    private EnergyTitle setDefaultTitles(EnergyTitle energyTitles) {

        if (energyTitles == null) {

            energyTitles = new EnergyTitle();
            energyTitles.setTitleAmount1("conta 1");
            energyTitles.setTitleAmount2("conta 2");
            energyTitles.setTitleAmount3("conta 3");
            energyTitles.setTitleAmount4("conta 1");
            energyTitles.setTitleAmount5("conta 2");

        } else {

            if (energyTitles.getTitleAmount1() == null || energyTitles.getTitleAmount1().isBlank()) {
                energyTitles.setTitleAmount1("conta 1");
            }
            if (energyTitles.getTitleAmount2() == null || energyTitles.getTitleAmount2().isBlank()) {
                energyTitles.setTitleAmount2("conta 2");
            }
            if (energyTitles.getTitleAmount3() == null || energyTitles.getTitleAmount3().isBlank()) {
                energyTitles.setTitleAmount3("conta 3");
            }
            if (energyTitles.getTitleAmount4() == null || energyTitles.getTitleAmount4().isBlank()) {
                energyTitles.setTitleAmount4("conta 1");
            }
            if (energyTitles.getTitleAmount5() == null || energyTitles.getTitleAmount5().isBlank()) {
                energyTitles.setTitleAmount5("conta 2");
            }
            
        }

        return energyTitles;

    }
}
