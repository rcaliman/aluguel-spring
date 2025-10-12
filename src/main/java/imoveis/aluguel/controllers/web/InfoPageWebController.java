package imoveis.aluguel.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.mappers.PropertyLogMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.services.PropertyLogService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/infopage")
public class InfoPageWebController {

    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;
    private final TenantService tenantService;
    private final TenantMapper tenantMapper;
    private final PropertyLogService propertyLogService;
    private final PropertyLogMapper propertyLogMapper;

    @GetMapping("/{id}")
    public String info(@PathVariable Long id, Model model) {

        var property = propertyService.findById(id);
        var propertyDtoResponse = propertyMapper.toDtoResponse(property);

        var tenant = tenantService.findById(property.getTenant().getId());
        var tenantDtoResponse = tenantMapper.toDtoResponse(tenant);

        var propertyLog = propertyLogService.findAllByPropertyId(property.getId());
        var propertyLogDtoResponse = propertyLog.stream().map(propertyLogMapper::toDtoResponse).toList();

        model.addAttribute("property", propertyDtoResponse);
        model.addAttribute("tenant", tenantDtoResponse);
        model.addAttribute("propertyLog", propertyLogDtoResponse);

        return "/infopage/infopage";

    }

}
