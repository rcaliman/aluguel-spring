package imoveis.aluguel.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.dtos.ReceiptDtoResponse;
import imoveis.aluguel.mappers.ReceiptMapper;
import imoveis.aluguel.services.ReceiptService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptController {
    
    private final ReceiptService receiptService;
    private final ReceiptMapper receiptMapper;

    @PostMapping
    public List<ReceiptDtoResponse> receipts(@RequestBody ReceiptDtoRequest dtoRequest) {

        var listReceipts = receiptService.receipts(dtoRequest);
        List<ReceiptDtoResponse> listDto = new ArrayList<>();

        listReceipts.forEach(
            receipt -> {
                listDto.add(receiptMapper.dtoResponse(receipt));
            }
        );

        return listDto;

    }
}
