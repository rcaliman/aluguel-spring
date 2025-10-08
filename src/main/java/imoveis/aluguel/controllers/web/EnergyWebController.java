package imoveis.aluguel.controllers.web;

import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoResponseList;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.services.EnergyService;
import imoveis.aluguel.services.EnergyTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/energies")
@RequiredArgsConstructor
public class EnergyWebController {

    private final EnergyService energyService;
    private final EnergyTitleService energyTitleService;
    private final EnergyMapper energyMapper;

    @GetMapping
    public String listEnergyReadings(Model model) {

        List<Energy> energies = energyService.listLasts();
        EnergyTitle energyTitles = energyTitleService.findLast();

        energyTitles = setDefaultTitles(energyTitles);

        List<EnergyDtoResponseList> dtoList = IntStream.range(0, energies.size())
                .mapToObj(i -> {
                    Energy energy = energies.get(i);
                    boolean isLast = (i == energies.size() - 1);
                    return energyMapper.toDtoResponseList(energy, isLast);
                })
                .toList();

        model.addAttribute("energyReadings", dtoList);
        model.addAttribute("energyTitles", energyTitles);
        model.addAttribute("currentPage", "energies");


        return "energy/list";
    }

    private EnergyTitle setDefaultTitles(EnergyTitle energyTitles) {
        if(energyTitles == null) {
            energyTitles = new EnergyTitle();
            energyTitles.setTitleAmount1("conta 1");
            energyTitles.setTitleAmount2("conta 2");
            energyTitles.setTitleAmount3("conta 3");
        } else {
            if(energyTitles.getTitleAmount1() == null || energyTitles.getTitleAmount2().isBlank()) {
                energyTitles.setTitleAmount1("conta 1");
            }
            if(energyTitles.getTitleAmount2() == null || energyTitles.getTitleAmount2().isBlank()) {
                energyTitles.setTitleAmount2("conta 2");
            }
            if(energyTitles.getTitleAmount3() == null || energyTitles.getTitleAmount3().isBlank()) {
                energyTitles.setTitleAmount3("conta 3");
            }
        }
        return energyTitles;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("energy", new Energy());
        model.addAttribute("currentPage", "energies");

        return "energy/form";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Energy energy = energyService.findById(id);

        model.addAttribute("energy", energy);
        model.addAttribute("currentPage", "energies");

        return "energy/form";

    }

    @PostMapping("/save")
    public String saveEnergy(@ModelAttribute EnergyDtoRequest dtoRequest) {

        var energy = energyMapper.toEnergy(dtoRequest);
        if (energy.getId() == null) {
            energyService.calculate(energy);
        } else {
            energyService.edit(energy, energy.getId());
        }

        return "redirect:/energies";
        
    }

    @GetMapping("/titleform")
    public String titleForm(Model model) {

        model.addAttribute("energyTitle", new EnergyTitle());
        model.addAttribute("currentPage", "energies");

        return "energy/titleform";

    }

    @PostMapping("/savetitle")
    public String saveEnergyTitle(@ModelAttribute EnergyTitle energyTitle) {
        
        energyTitleService.save(energyTitle);
        
        return "redirect:/energies";

    }

}