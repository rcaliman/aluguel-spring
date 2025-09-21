package imoveis.aluguel.controllers.web;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.mappers.ReceiptMapper;
import imoveis.aluguel.services.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptWebController {

    private final ReceiptService receiptService;
    private final ReceiptMapper receiptMapper;

    @PostMapping("/generate")
    public String generateReceipts(@RequestParam("propertyIds") List<Long> propertyIds,
                                    @RequestParam("landlordId") Long landlordId,
                                    @RequestParam("month") String month,
                                    @RequestParam("year") String year,
                                    Model model) {

        ReceiptDtoRequest request = new ReceiptDtoRequest(
            propertyIds,
            landlordId,
            "Colatina",
            null,
            month,
            year
        );

        List<ReceiptDtoResponse> receipts = receiptService.receipts(request).stream()
                                                .map(receiptMapper::dtoResponse)
                                                .toList();

        model.addAttribute("receipts", receipts);

        return "receipt/list";
    }
    
}