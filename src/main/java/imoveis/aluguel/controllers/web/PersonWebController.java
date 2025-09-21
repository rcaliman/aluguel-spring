package imoveis.aluguel.controllers.web;

import imoveis.aluguel.entities.Person;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.enums.PersonTypeEnum;
import imoveis.aluguel.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonWebController {

    private final PersonService personService;

    @GetMapping
    public String listPersons(Model model) {
        model.addAttribute("persons", personService.list(Sort.by("name")));
        model.addAttribute("pageTitle", "Lista de Pessoas"); // Título padrão
        return "person/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        
        model.addAttribute("person", new Person());
        model.addAttribute("personTypes", PersonTypeEnum.values());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("isTypeSpecific", false);

        return "person/form";

    }
    
    @GetMapping("/tenants")
    public String listTenants(Model model) {

        model.addAttribute("persons", personService.findByPersonType(PersonTypeEnum.TENANT));
        model.addAttribute("pageTitle", "Lista de Locatários");
        model.addAttribute("context", "tenants");

        return "person/list";

    }

    @GetMapping("/tenants/new")
    public String showCreateTenantForm(Model model) {

        Person newTenant = new Person();
        newTenant.setType(PersonTypeEnum.TENANT);

        model.addAttribute("person", newTenant);
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("isTypeSpecific", true);

        return "person/form"; 

    }

    @PostMapping("/save")
    public String savePerson(@ModelAttribute("person") Person person) {

        if (person.getId() == null) {
            personService.create(person);
        } else {
            personService.update(person.getId(), person);
        }

        if (person.getType() == PersonTypeEnum.TENANT) {
            return "redirect:/persons/tenants";
        }

        return "redirect:/persons";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Person person = personService.findById(id);
        model.addAttribute("person", person);
        model.addAttribute("personTypes", PersonTypeEnum.values());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("isTypeSpecific", person.getType() == PersonTypeEnum.TENANT);

        return "person/form";

    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id) {

        personService.deleteById(id);
        return "redirect:/persons";
        
    }

}