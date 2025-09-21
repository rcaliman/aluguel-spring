package imoveis.aluguel.controllers.web;

import imoveis.aluguel.dtos.EnergyDtoResponseList;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.services.EnergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/energy")
@RequiredArgsConstructor
public class EnergyWebController {

    private final EnergyService energyService;
    private final EnergyMapper energyMapper;

    @GetMapping
    public String listEnergyReadings(Model model) {
        List<Energy> energies = energyService.listLasts();

        List<EnergyDtoResponseList> dtoList = IntStream.range(0, energies.size())
                .mapToObj(i -> {
                    Energy energy = energies.get(i);
                    boolean isLast = (i == energies.size() - 1);
                    return energyMapper.toDtoResponseList(energy, isLast);
                })
                .toList();

        model.addAttribute("energyReadings", dtoList);
        return "energy/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("energy", new Energy());
        return "energy/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Energy energy = energyService.findById(id);
        model.addAttribute("energy", energy);
        return "energy/form";
    }

    @PostMapping("/save")
    public String saveEnergy(@ModelAttribute("energy") Energy energy) {
        if (energy.getId() == null) {
            energyService.calculate(energy);
        } else {
            energyService.edit(energy, energy.getId());
        }
        return "redirect:/energy";
    }
}