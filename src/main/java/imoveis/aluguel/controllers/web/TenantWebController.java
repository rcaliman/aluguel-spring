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

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.ContactTypeEnum;
import imoveis.aluguel.enums.MaritalStatusEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.TenantRepository;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantWebController {

    private final TenantService tenantService;
    private final TenantRepository tenantRepository;

    @GetMapping
    public String listTenants(Model model) {

        model.addAttribute("tenants", tenantService.list(Sort.by("name")));
        model.addAttribute("currentPage", "tenants");

        return "tenant/list";

    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showCreateForm(Model model) {

        model.addAttribute("tenant", new Tenant());
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("contactTypeOptions", ContactTypeEnum.values());
        model.addAttribute("currentPage", "tenants");

        return "tenant/form";

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String showEditForm(@PathVariable Long id, Model model) {

        Tenant tenant = tenantRepository.findById(id).orElseThrow(
            () -> new NotFoundException(String.format("Inquilino de id %d não encontrado", id))
        );

        model.addAttribute("tenant", tenant);
        model.addAttribute("maritalStatusOptions", MaritalStatusEnum.values());
        model.addAttribute("contactTypeOptions", ContactTypeEnum.values());
        model.addAttribute("currentPage", "tenants");

        return "tenant/form";

    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String saveTenant(@ModelAttribute Tenant tenant) {

        // Converter strings vazias em null para evitar violação de constraint única
        if (tenant.getCpfCnpj() != null && tenant.getCpfCnpj().trim().isEmpty()) {
            tenant.setCpfCnpj(null);
        }

        if (tenant.getDocument() != null && tenant.getDocument().trim().isEmpty()) {
            tenant.setDocument(null);
        }

        if (tenant.getAddress() != null && tenant.getAddress().trim().isEmpty()) {
            tenant.setAddress(null);
        }

        if (tenant.getLocation() != null && tenant.getLocation().trim().isEmpty()) {
            tenant.setLocation(null);
        }

        if (tenant.getState() != null && tenant.getState().trim().isEmpty()) {
            tenant.setState(null);
        }

        if (tenant.getCity() != null && tenant.getCity().trim().isEmpty()) {
            tenant.setCity(null);
        }

        if (tenant.getNationality() != null && tenant.getNationality().trim().isEmpty()) {
            tenant.setNationality(null);
        }

        if (tenant.getId() == null) {
            tenantService.create(tenant);
        } else {
            tenantService.update(tenant.getId(), tenant);
        }

        return "redirect:/tenants";

    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERADOR')")
    public String deleteTenant(@PathVariable Long id) {

        tenantService.deleteById(id);

        return "redirect:/tenants";

    }
}