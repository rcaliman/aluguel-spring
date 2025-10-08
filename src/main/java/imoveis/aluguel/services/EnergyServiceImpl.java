package imoveis.aluguel.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.repositories.EnergyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnergyServiceImpl implements EnergyService {

    private final EnergyRepository energyRepository;

    @Override
    public List<Energy> listLasts() {

        var lasts = energyRepository.findTop3ByOrderByIdDesc().orElse(Collections.emptyList());
        
        if (lasts.isEmpty()) {
            return Collections.emptyList();
        }
        
        var sortedEnergies = lasts.stream()
            .sorted(Comparator.comparing(Energy::getId))
            .toList();

        return sortedEnergies;
        
    }

    @Override
    @Transactional
    public Energy calculate(Energy newEnergy) {
        
        var energy = calculateAmounts(newEnergy, false);

        return energyRepository.save(energy);

    }

    private Energy calculateAmounts(Energy newEnergy, Boolean isPut) {

        int recordToUse = isPut ? 1 : 0;

        var previousEnergy = energyRepository.findTop2ByOrderByIdDesc().orElse(null).get(recordToUse);

        if(previousEnergy != null) {

            var consuption1 = Math.max(0, newEnergy.getCounter1() - previousEnergy.getCounter1());
            var consuption2 = Math.max(0, newEnergy.getCounter2() - previousEnergy.getCounter2());
            var consuption3 = Math.max(0, newEnergy.getCounter3() - previousEnergy.getCounter3());

            var total = consuption1 + consuption2 + consuption3;

            if(total > 0) {

                var amount1 = ((double)consuption1/total) * newEnergy.getBillAmount();
                var amount2 = ((double)consuption2/total) * newEnergy.getBillAmount();
                var amount3 = ((double)consuption3/total) * newEnergy.getBillAmount();

                newEnergy.setAmount1(amount1);
                newEnergy.setAmount2(amount2);
                newEnergy.setAmount3(amount3);

            }
        }

        return newEnergy;

    }

    @Override
    @Transactional
    public Energy edit(Energy editedEnergy, Long id) {

        Energy existingEnergy = energyRepository.findById(id).orElseThrow(
            () ->  new EntityNotFoundException(String.format("Conta de id %d não encontrada.", id))
        );

        var calculatedEnergy = calculateAmounts(editedEnergy, true);

        existingEnergy.setCounter1(calculatedEnergy.getCounter1());
        existingEnergy.setCounter2(calculatedEnergy.getCounter2());
        existingEnergy.setCounter3(calculatedEnergy.getCounter3());
        existingEnergy.setBillAmount(calculatedEnergy.getBillAmount());
        existingEnergy.setKwhValue(calculatedEnergy.getKwhValue());
        existingEnergy.setDate(calculatedEnergy.getDate());

        calculateAmounts(existingEnergy, true);

        return energyRepository.save(existingEnergy);
        
    }

    @Override
    public Energy findById(Long id) {
        return energyRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Conta de id %d não encontrada.", id))
        );
    }

}
