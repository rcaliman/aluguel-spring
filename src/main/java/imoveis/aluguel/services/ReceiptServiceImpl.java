package imoveis.aluguel.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Receipt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final TenantService tenantService;
    private final PropertyService propertyService;

    @Override
    public List<Receipt> receipts(ReceiptDtoRequest receiptRequest) {

        List<Receipt> receipts = new ArrayList<>();

        var landlord = tenantService.findById(receiptRequest.landlordId());

        List<Property> properties = new ArrayList<>();

        if(receiptRequest.propertyIds() != null) {
            receiptRequest.propertyIds().forEach(
                id -> properties.add(propertyService.findById(id))
            );
        }

        properties.forEach(
            
            property -> {
                var contact = propertyService.findById(property.getId()).getTenant().getContacts().getFirst();
                
                var receipt = Receipt.builder()
                                .tenant(property.getTenant().getName())
                                .value(property.getValue())
                                .propertyType(property.getType())
                                .propertyNumber(property.getNumber())
                                .locale(receiptRequest.locale())
                                .day(receiptRequest.day())
                                .month(receiptRequest.month())
                                .year(receiptRequest.year())
                                .landlord(landlord.getName())
                                .tenantContact(contact.toString())
                                .build();
            
                receipts.add(receipt);
            }
        );

        Collections.sort(receipts, (r1, r2) -> r1.getTenant().compareTo(r2.getTenant()));

        return receipts;

    }
    
}
