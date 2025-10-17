package imoveis.aluguel.controllers.web;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import imoveis.aluguel.dtos.ContractDtoRequest;
import imoveis.aluguel.dtos.LandlordDtoResponse;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.mappers.PropertyMapper;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.services.LandlordService;
import imoveis.aluguel.services.PropertyService;
import imoveis.aluguel.utils.NumberToWordsConverter;
import lombok.RequiredArgsConstructor;

@RequestMapping("/contracts")
@Controller
@RequiredArgsConstructor
public class ContractWebController {

    private final LandlordService landlordService;
    private final PropertyService propertyService;

    @GetMapping("/form/{id}")
    public String contractForm(@PathVariable Long id, Model model) {

        var property = propertyService.findById(id);

        if (Objects.isNull(property.tenant())) {
            return "redirect:/properties?error=ImovelVago";
        }

        var tenant = property.tenant();

        List<LandlordDtoResponse> landlords = landlordService.list(Sort.by("name"));

        model.addAttribute("property", property);
        model.addAttribute("tenant", tenant);
        model.addAttribute("landlords", landlords);
        model.addAttribute("propertyTypeEnum", PropertyTypeEnum.values());

        return "contract/form";

    }

    @PostMapping("/editor")
    public String contractEditor(@ModelAttribute ContractDtoRequest dtoRequest, Model model) {

        var property = propertyService.findById(dtoRequest.propertyId());
        var landlord = landlordService.findById(dtoRequest.landlordId());
        var tenant = property.tenant();

        if (tenant == null) {
            throw new NotFoundException("Imóvel não possui inquilino associado para gerar o contrato.");
        }

        var period = period(dtoRequest.startYear(), dtoRequest.startMonth(), dtoRequest.endYear(),
                dtoRequest.endMonth(), property.paymentDay());

        String dataContratoPorExtenso = getDataContratoPorExtenso(landlord.city(), landlord.state(),
                property.paymentDay(), dtoRequest.startMonth(), dtoRequest.startYear());

        String valorPorExtenso = NumberToWordsConverter.convert(property.value());

        model.addAttribute("landlord", landlord);
        model.addAttribute("tenant", tenant);
        model.addAttribute("property", property);
        model.addAttribute("period", period);
        model.addAttribute("dataAtualPorExtenso", dataContratoPorExtenso);
        model.addAttribute("valorPorExtenso", valorPorExtenso);

        return "contract/editor";

    }

    private Map<String, String> period(String startYear, String startMonth, String endYear, String endMonth,
            String day) {

        HashMap<String, String> period = new HashMap<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        var start = LocalDate.of(Integer.parseInt(startYear), Integer.parseInt(startMonth), Integer.parseInt(day));

        var stop = LocalDate.of(Integer.parseInt(endYear), Integer.parseInt(endMonth), Integer.parseInt(day));

        var months = ChronoUnit.MONTHS.between(start, stop);

        period.put("start", start.format(dateFormat).toString());
        period.put("stop", stop.format(dateFormat).toString());
        period.put("months", Long.toString(months));

        return period;

    }

    private String getDataContratoPorExtenso(String cidade, String estado, String dia, String mesNumero, String ano) {

        Month monthEnum = Month.of(Integer.parseInt(mesNumero));

        String mesNome = monthEnum.getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

        String local = (cidade != null && !cidade.isEmpty() && estado != null && !estado.isEmpty())
                ? cidade + "-" + estado
                : "Sua Cidade-UF";

        return local + ", " + dia + " de " + mesNome + " de " + ano;

    }
}