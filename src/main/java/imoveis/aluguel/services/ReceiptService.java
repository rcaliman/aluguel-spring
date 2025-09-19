package imoveis.aluguel.services;

import java.util.List;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.entities.Receipt;

public interface ReceiptService {

    List<Receipt> receipts(ReceiptDtoRequest receiptRequest);
    
}
