package imoveis.aluguel.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.dtos.CommercialEnergyDtoRequest;
import imoveis.aluguel.dtos.EnergyDtoRequest;
import imoveis.aluguel.entities.CommercialEnergy;
import imoveis.aluguel.entities.Energy;
import imoveis.aluguel.entities.EnergyTitle;
import imoveis.aluguel.mappers.CommercialEnergyMapper;
import imoveis.aluguel.mappers.EnergyMapper;
import imoveis.aluguel.services.CommercialEnergyService;
import imoveis.aluguel.services.EnergyService;
import imoveis.aluguel.services.EnergyTitleService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/energies")
@RequiredArgsConstructor
public class EnergyWebController {

    private final EnergyService energyService;
    private final EnergyTitleService energyTitleService;
    private final EnergyMapper energyMapper;

    private final CommercialEnergyService commercialEnergyService;
    private final CommercialEnergyMapper commercialEnergyMapper;

    @GetMapping
    public String listEnergyReadings(Model model) {

        var energiesReadings = energyService.listLasts();
        
        var energyTitles = energyTitleService.findLast();

        model.addAttribute("energyReadings", energiesReadings);
        model.addAttribute("energyTitles", energyTitles);
        model.addAttribute("currentPage", "energies");

        var commercialEnergiesReadings = commercialEnergyService.listLasts();
                                                        
        model.addAttribute("commercialEnergyReadings", commercialEnergiesReadings);

        return "energy/list";

    }



    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("energy", new Energy());
        model.addAttribute("currentPage", "energies");

        return "energy/form";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        var energy = energyService.findById(id);

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

    @GetMapping("/new-commercial")
    public String showCreateFormCommercial(Model model) {

        model.addAttribute("commercialEnergy", new CommercialEnergy());
        model.addAttribute("currentPage", "energies");

        return "energy/commercial-form";

    }

    @PostMapping("/save-commercial")
    public String saveCommercialEnergy(@ModelAttribute CommercialEnergyDtoRequest dtoRequest) {

        var commercialEnergy = commercialEnergyMapper.toEntity(dtoRequest);

        if (commercialEnergy.getId() == null) {
            commercialEnergyService.calculate(commercialEnergy);
        } else {
            commercialEnergyService.edit(commercialEnergy, commercialEnergy.getId());
        }

        return "redirect:/energies";

    }

    @GetMapping("/edit-commercial/{id}")
    public String showEditFormCommercial(@PathVariable Long id, Model model) {

        var commercialEnergy = commercialEnergyService.findById(id);

        model.addAttribute("commercialEnergy", commercialEnergy);
        model.addAttribute("currentPage", "energies");

        return "energy/commercial-form";

    }


}