package imoveis.aluguel.controllers.web;

import imoveis.aluguel.entities.Property;
import imoveis.aluguel.enums.PersonTypeEnum; // Importar o Enum
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.services.PersonService;
import imoveis.aluguel.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyWebController {

    private final PropertyService propertyService;
    private final PersonService personService;

    @GetMapping
    public String listProperties(Model model) {

        model.addAttribute("properties", propertyService.list(Sort.by("address")));
        model.addAttribute("landlords", personService.findByPersonType(PersonTypeEnum.TENANT));

        int currentYear = LocalDate.now().getYear();
        List<Integer> years = IntStream.rangeClosed(currentYear - 5, currentYear + 2)
                                            .boxed().collect(Collectors.toList());
        model.addAttribute("years", years);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", LocalDate.now().getMonthValue());


        return "property/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("property", new Property());
        model.addAttribute("persons", personService.list(Sort.by("name")));
        model.addAttribute("propertyTypes", PropertyTypeEnum.values());

        return "property/form";
        
    }

    @PostMapping("/save")
    public String saveProperty(@ModelAttribute("property") Property property) {

        if (property.getId() == null) {
            propertyService.create(property);
        } else {
            propertyService.update(property.getId(), property);
        }

        return "redirect:/properties";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Property property = propertyService.findById(id);
        model.addAttribute("property", property);
        model.addAttribute("persons", personService.list(Sort.by("name")));
        model.addAttribute("propertyTypes", PropertyTypeEnum.values());

        return "property/form";

    }

    @GetMapping("/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {

        propertyService.deleteById(id);
        return "redirect:/properties";

    }
}