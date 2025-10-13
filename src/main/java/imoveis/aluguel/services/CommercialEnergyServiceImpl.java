package imoveis.aluguel.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.CommercialEnergy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.CommercialEnergyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommercialEnergyServiceImpl implements CommercialEnergyService {

    private final CommercialEnergyRepository commercialEnergyRepository;

    @Override
    public List<CommercialEnergy> listLasts() {
        var energyList = commercialEnergyRepository.findTop2ByOrderByIdDesc().orElse(List.of());

        var orderedList = energyList.stream().sorted(Comparator.comparing(CommercialEnergy::getId)).toList();

        return orderedList;
    }

    @Transactional
    public CommercialEnergy calculate(CommercialEnergy energy) {

        CommercialEnergy lastEnergy = commercialEnergyRepository.findTopByOrderByIdDesc().orElse(null);

        if (lastEnergy != null) {

            var kwh1 = energy.getInternalCounter() - lastEnergy.getInternalCounter();
            var kwh2 = energy.getAccountConsumption() - kwh1;
            var percentKwh1 = kwh1 / energy.getAccountConsumption();
            var percentKwh2 = kwh2 / energy.getAccountConsumption();
            var amount1 = percentKwh1 * energy.getAccountValue();
            var amount2 = percentKwh2 * energy.getAccountValue();

            energy.setCalculatedConsumption1(kwh1);
            energy.setCalculatedConsumption2(kwh2);
            energy.setAmount1(amount1);
            energy.setAmount2(amount2);

        }

        return commercialEnergyRepository.save(energy);
    }

    @Override
    @Transactional
    public CommercialEnergy edit(CommercialEnergy editedEnergy, Long id) {

        CommercialEnergy energy = commercialEnergyRepository.findById(id).orElseThrow(
                    () -> new NotFoundException(String.format("conta de id %d não encontrada", id))
            );
        var energyPrev = commercialEnergyRepository.findTop2ByOrderByIdDesc().orElseThrow(
                () -> new NotFoundException("registros não encontrados")
            ).get(1);

        energy.setDate(editedEnergy.getDate());
        energy.setInternalCounter(editedEnergy.getInternalCounter());
        energy.setAccountConsumption(editedEnergy.getAccountConsumption());
        energy.setAccountValue(editedEnergy.getAccountValue());

        var kwh1 = energy.getInternalCounter() - energyPrev.getInternalCounter();
        var kwh2 = energy.getAccountConsumption() - kwh1;
        var percentKwh1 = kwh1 / energy.getAccountConsumption();
        var percentKwh2 = kwh2 / energy.getAccountConsumption();
        var amount1 = percentKwh1 * energy.getAccountValue();
        var amount2 = percentKwh2 * energy.getAccountValue();

        energy.setCalculatedConsumption1(kwh1);
        energy.setCalculatedConsumption2(kwh2);
        energy.setAmount1(amount1);
        energy.setAmount2(amount2);

        return commercialEnergyRepository.save(energy);
    
    }

    @Override
    public CommercialEnergy findById(Long id) {

        return commercialEnergyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Conta de id %d não encontrada.", id)));

    }

}