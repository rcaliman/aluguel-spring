package imoveis.aluguel.controllers.web;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.enums.ContactTypeEnum;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.LandlordRepository;
import imoveis.aluguel.services.LandlordService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/landlords")
@RequiredArgsConstructor
public class LandlordWebController {

    private final LandlordService landlordService;
    private final LandlordRepository landlordRepository;

    @GetMapping
    public String listLandlords(Model model) {

        model.addAttribute("landlords", landlordService.list(Sort.by("name")));
        model.addAttribute("currentPage", "landlords");

        return "landlord/list";

    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showCreateForm(Model model) {

        model.addAttribute("landlord", new Landlord());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("contactTypeOptions", ContactTypeEnum.values());
        model.addAttribute("currentPage", "landlords");

        return "landlord/form";

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showEditForm(@PathVariable Long id, Model model) {

        Landlord landlord = landlordRepository.findById(id).orElseThrow(
            () -> new NotFoundException(String.format("Locador de id %d não encontrado", id))
        );

        model.addAttribute("landlord", landlord);
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("contactTypeOptions", ContactTypeEnum.values());
        model.addAttribute("currentPage", "landlords");

        return "landlord/form";

    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String saveLandlord(@ModelAttribute("landlord") Landlord landlord) {

        if (landlord.getId() == null) {
            landlordService.create(landlord);
        } else {
            landlordService.update(landlord.getId(), landlord);
        }

        return "redirect:/landlords";

    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String deleteLandlord(@PathVariable Long id) {

        landlordService.deleteById(id);

        return "redirect:/landlords";

    }

}