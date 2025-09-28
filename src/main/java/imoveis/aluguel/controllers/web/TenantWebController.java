package imoveis.aluguel.controllers.web;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tenants") // Rota base para todas as ações de inquilino
@RequiredArgsConstructor
public class TenantWebController {

    private final TenantService tenantService;

    /**
     * Lista todos os inquilinos.
     */
    @GetMapping
    public String listTenants(Model model) {
        model.addAttribute("tenants", tenantService.list(Sort.by("name")));
        return "tenant/list"; // Aponta para a nova view /tenant/list.html
    }
    
    /**
     * Exibe o formulário para criar um novo inquilino.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tenant", new Tenant());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        return "tenant/form"; // Aponta para a nova view /tenant/form.html
    }

    /**
     * Exibe o formulário para editar um inquilino existente.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tenant tenant = tenantService.findById(id);
        model.addAttribute("tenant", tenant);
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        return "tenant/form"; // Reutiliza o mesmo formulário
    }

    /**
     * Salva um inquilino (novo ou existente).
     */
    @PostMapping("/save")
    public String saveTenant(@ModelAttribute("tenant") Tenant tenant) {
        if (tenant.getId() == null) {
            tenantService.create(tenant);
        } else {
            tenantService.update(tenant.getId(), tenant);
        }
        return "redirect:/tenants"; // Redireciona para a lista de inquilinos
    }

    /**
     * Deleta um inquilino.
     */
    @GetMapping("/delete/{id}")
    public String deleteTenant(@PathVariable Long id) {
        tenantService.deleteById(id);
        return "redirect:/tenants";
    }
}