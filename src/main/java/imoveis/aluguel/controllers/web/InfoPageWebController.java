package imoveis.aluguel.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.dtos.RentalCorrectionDTO;
import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.services.PropertyLogService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.services.RentalCorrectionService;
import imoveis.aluguel.services.TenantService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/infopage")
public class InfoPageWebController {

    private final PropertyService propertyService;
    private final TenantService tenantService;
    private final PropertyLogService propertyLogService;
    private final RentalCorrectionService rentalCorrectionService;

    @GetMapping("/{id}")
    public String info(@PathVariable Long id, Model model) {

        var property = propertyService.findById(id);

        TenantDtoResponse tenant = null;

        if (property.tenant() != null) {
            tenant = tenantService.findById(property.tenant().id());
        }

        var propertyLog = propertyLogService.findAllByPropertyId(property.id());

        RentalCorrectionDTO rentalCorrection = rentalCorrectionService.calculateCorrectedRentalValue(property.id());

        model.addAttribute("property", property);
        model.addAttribute("tenant", tenant);
        model.addAttribute("propertyLog", propertyLog);
        model.addAttribute("rentalCorrection", rentalCorrection);

        return "infopage/infopage";

    }

}
