package imoveis.aluguel.controllers.web;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyWebController {

    private final PropertyService propertyService;
    private final TenantService tenantService;
    private final LandlordService landlordService;
    private final PropertyRepository propertyRepository;

    @GetMapping
    public String listProperties(Model model,
                                    @RequestParam(defaultValue = "tenant.name") String sortField
                                    ) {

        int currentYear = LocalDate.now().getYear();
        List<Integer> years = IntStream.rangeClosed(currentYear - 5, currentYear + 2)
                .boxed().collect(Collectors.toList());

        model.addAttribute("properties", propertyService.list(sortField));
        model.addAttribute("landlords", landlordService.list(Sort.by("name")));
        model.addAttribute("years", years);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", LocalDate.now().getMonthValue());
        model.addAttribute("currentPage", "properties");

        return "property/list";

    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showCreateForm(Model model) {

        model.addAttribute("property", new Property());
        model.addAttribute("tenants", tenantService.list(Sort.by("name")));
        model.addAttribute("propertyTypes", PropertyTypeEnum.values());
        model.addAttribute("propertyUseTypes", PropertyUseTypeEnum.values());
        model.addAttribute("currentPage", "properties");

        return "property/form";

    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String saveProperty(@ModelAttribute("property") Property property) {

        if (property.getId() == null) {
            propertyService.create(property);
        } else {
            propertyService.update(property.getId(), property);
        }

        return "redirect:/properties";

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showEditForm(@PathVariable Long id, Model model) {

        Property property = propertyRepository.findById(id).orElseThrow(
            () -> new NotFoundException(String.format("Imóvel de id %d não encontrado.", id))
        );
        
        model.addAttribute("property", property);
        model.addAttribute("tenants", tenantService.list(Sort.by("name")));
        model.addAttribute("propertyTypes", PropertyTypeEnum.values());
        model.addAttribute("propertyUseTypes", PropertyUseTypeEnum.values());
        model.addAttribute("currentPage", "properties");

        return "property/form";

    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String deleteProperty(@PathVariable Long id) {

        propertyService.deleteById(id);
        return "redirect:/properties";

    }
}