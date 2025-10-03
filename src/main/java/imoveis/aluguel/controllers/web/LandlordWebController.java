package imoveis.aluguel.controllers.web;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.services.LandlordService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/landlords") // Rota base para todas as ações de locador
@RequiredArgsConstructor
public class LandlordWebController {

    private final LandlordService landlordService;

    /**
     * Lista todos os locadores.
     */
    @GetMapping
    public String listLandlords(Model model) {
        model.addAttribute("landlords", landlordService.list(Sort.by("name")));
        return "landlord/list"; // Aponta para a view /landlord/list.html
    }
    
    /**
     * Exibe o formulário para criar um novo locador.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("landlord", new Landlord());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        return "landlord/form"; // Aponta para a view /landlord/form.html
    }

    /**
     * Exibe o formulário para editar um locador existente.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Landlord landlord = landlordService.findById(id);
        model.addAttribute("landlord", landlord);
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        return "landlord/form"; // Reutiliza o mesmo formulário
    }

    /**
     * Salva um locador (novo ou existente).
     */
    @PostMapping("/save")
    public String saveLandlord(@ModelAttribute("landlord") Landlord landlord) {
        if (landlord.getId() == null) {
            landlordService.create(landlord);
        } else {
            landlordService.update(landlord.getId(), landlord);
        }
        return "redirect:/landlords"; // Redireciona para a lista de locadores
    }

    /**
     * Deleta um locador.
     */
    @GetMapping("/delete/{id}")
    public String deleteLandlord(@PathVariable Long id) {
        landlordService.deleteById(id);
        return "redirect:/landlords";
    }
}