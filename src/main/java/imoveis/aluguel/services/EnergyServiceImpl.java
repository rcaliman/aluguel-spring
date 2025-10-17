package imoveis.aluguel.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.repositories.EnergyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnergyServiceImpl implements EnergyService {

    private final EnergyRepository energyRepository;
    private final EnergyMapper energyMapper;

    @Override
    @Cacheable(value = "energies", key = "'list-lasts'")
    public List<EnergyDtoResponse> listLasts() {

        var lasts = energyRepository.findTop3ByOrderByIdDesc().orElse(List.of());

        if (lasts.isEmpty()) {
            return Collections.emptyList();
        }

        var sortedEnergies = lasts.stream().sorted(Comparator.comparing(Energy::getId)).toList();

        var dtoResponse = IntStream.range(0, sortedEnergies.size()).mapToObj( i -> {
            Energy energy = sortedEnergies.get(i);
            boolean isLast = (i == sortedEnergies.size() - 1);

            return energyMapper.toDtoResponse(energy, isLast);
        }).toList();

        return dtoResponse;

    }

    @Override
    @Transactional
    @CacheEvict(value = "energies", allEntries = true)
    public EnergyDtoResponse calculate(Energy newEnergy) {

        var energy = calculateAmounts(newEnergy, false);

        var savedEnergy = energyRepository.save(energy);

        return energyMapper.toDtoResponse(savedEnergy, false);

    }

    private Energy calculateAmounts(Energy newEnergy, Boolean isPut) {

        int recordToUse = isPut ? 1 : 0;

        var previousEnergiesOpt = energyRepository.findTop2ByOrderByIdDesc();

        if (previousEnergiesOpt.isEmpty() || previousEnergiesOpt.get().isEmpty()) {
            return newEnergy;
        }

        var previousEnergies = previousEnergiesOpt.get();

        if (previousEnergies.size() <= recordToUse) {
            return newEnergy;
        }

        var previousEnergy = previousEnergies.get(recordToUse);

        if (previousEnergy != null) {
            
            var consuption1 = Math.max(0, newEnergy.getCounter1() - previousEnergy.getCounter1());
            var consuption2 = Math.max(0, newEnergy.getCounter2() - previousEnergy.getCounter2());
            var consuption3 = Math.max(0, newEnergy.getCounter3() - previousEnergy.getCounter3());

            var total = consuption1 + consuption2 + consuption3;

            if (total > 0) {

                var amount1 = ((double) consuption1 / total) * newEnergy.getBillAmount();
                var amount2 = ((double) consuption2 / total) * newEnergy.getBillAmount();
                var amount3 = ((double) consuption3 / total) * newEnergy.getBillAmount();

                newEnergy.setAmount1(amount1);
                newEnergy.setAmount2(amount2);
                newEnergy.setAmount3(amount3);

            }

        }

        return newEnergy;

    }

    @Override
    @Transactional
    @CacheEvict(value = "energies", allEntries = true)
    public EnergyDtoResponse edit(Energy editedEnergy, Long id) {

        Energy existingEnergy = energyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Conta de id %d não encontrada.", id)));

        var calculatedEnergy = calculateAmounts(editedEnergy, true);

        existingEnergy.setCounter1(calculatedEnergy.getCounter1());
        existingEnergy.setCounter2(calculatedEnergy.getCounter2());
        existingEnergy.setCounter3(calculatedEnergy.getCounter3());
        existingEnergy.setBillAmount(calculatedEnergy.getBillAmount());
        existingEnergy.setKwhValue(calculatedEnergy.getKwhValue());
        existingEnergy.setDate(calculatedEnergy.getDate());

        calculateAmounts(existingEnergy, true);

        var savedEnergy =  energyRepository.save(existingEnergy);

        return energyMapper.toDtoResponse(savedEnergy, false);

    }

    @Override
    @Cacheable(value = "energies", key = "#id")
    public EnergyDtoResponse findById(Long id) {

        var energy = energyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Conta de id %d não encontrada.", id)));

        return energyMapper.toDtoResponse(energy, false);

    }

}
