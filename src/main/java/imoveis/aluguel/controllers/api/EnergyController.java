package imoveis.aluguel.controllers.api;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoResponse;
import imoveis.aluguel.dtos.EnergyDtoResponseList;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.services.EnergyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/energy")
public class EnergyController {

    private final EnergyService energyService;
    private final EnergyMapper energyMapper;

    @GetMapping("/lasts")
    public ResponseEntity<List<EnergyDtoResponseList>> listLasts() {

        var energies = energyService.listLasts();

        var dtoResponse = IntStream.range(0, energies.size())
                .mapToObj(i -> {
                    Energy energy = energies.get(i);
                    boolean isLast = (i == energies.size() - 1);
                    return energyMapper.toDtoResponseList(energy, isLast);
                })
                .toList();

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<EnergyDtoResponse> calculate(@RequestBody EnergyDtoRequest dtoRequest) {

        var energy = energyMapper.toEnergy(dtoRequest);
        var newEnergy = energyService.calculate(energy);
        var dtoResponse = energyMapper.toDtoResponse(newEnergy);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergyDtoResponse> edit(@RequestBody EnergyDtoRequest dtoRequest, @PathVariable Long id) {

        var energy = energyMapper.toEnergy(dtoRequest);
        var editedEnergy = energyService.edit(energy, id);
        var dtoResponse = energyMapper.toDtoResponse(editedEnergy);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }
}
