package imoveis.aluguel.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.CommercialEnergyDtoResponse;
import imoveis.aluguel.entities.CommercialEnergy;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.CommercialEnergyMapper;
import imoveis.aluguel.repositories.CommercialEnergyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommercialEnergyServiceImpl implements CommercialEnergyService {

    private final CommercialEnergyRepository commercialEnergyRepository;
    private final CommercialEnergyMapper commercialEnergyMapper;

    @Override
    @Cacheable("last-commercial-energies")
    public List<CommercialEnergyDtoResponse> listLasts() {

        var lasts = commercialEnergyRepository.findTop2ByOrderByIdDesc().orElse(List.of());

        if (lasts.isEmpty()) {
            return Collections.emptyList();
        }

        var orderedLasts = lasts.stream()
                            .sorted(Comparator.comparing(CommercialEnergy::getId))
                            .toList();
                            
        List<CommercialEnergyDtoResponse> dtoListResponse = IntStream.range(0, orderedLasts.size()).mapToObj( i -> {
                CommercialEnergy energy = orderedLasts.get(i);
                boolean isLast = (i == orderedLasts.size() - 1);

                return commercialEnergyMapper.toDtoResponse(energy, isLast);
            }
        ).toList();

        return dtoListResponse;
    }

    @Transactional
    @CacheEvict(value = "last-commercial-energies", allEntries = true)
    public CommercialEnergyDtoResponse calculate(CommercialEnergy energy) {

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

        var newEnergy = commercialEnergyRepository.save(energy);
        return commercialEnergyMapper.toDtoResponse(newEnergy, null);

    }

    @Override
    @Transactional
    @CacheEvict(value = "last-commercial-energies", allEntries = true)
    public CommercialEnergyDtoResponse edit(CommercialEnergy editedEnergy, Long id) {

        CommercialEnergy energy = commercialEnergyRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("conta de id %d não encontrada", id)));

        var energyList = commercialEnergyRepository.findTop2ByOrderByIdDesc().orElseThrow(
                () -> new NotFoundException("registros não encontrados"));

        energy.setDate(editedEnergy.getDate());
        energy.setInternalCounter(editedEnergy.getInternalCounter());
        energy.setAccountConsumption(editedEnergy.getAccountConsumption());
        energy.setAccountValue(editedEnergy.getAccountValue());

        if (energyList.size() < 2) {
            energy.setCalculatedConsumption1(null);
            energy.setCalculatedConsumption2(null);
            energy.setAmount1(null);
            energy.setAmount2(null);

        } else {

            var energyPrev = energyList.get(1);

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
        }

        var newEnergy =  commercialEnergyRepository.save(energy);

        return commercialEnergyMapper.toDtoResponse(newEnergy, null);

    }

    @Override
    @Cacheable("commercial-energy-by-id")
    public CommercialEnergyDtoResponse findById(Long id) {

        var energy = commercialEnergyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Conta de id %d não encontrada.", id)));

        return commercialEnergyMapper.toDtoResponse(energy, null);

    }

}