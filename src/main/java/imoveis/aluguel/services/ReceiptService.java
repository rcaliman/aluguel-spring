package imoveis.aluguel.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.repositories.PropertyRepository;
import imoveis.aluguel.utils.DateUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final LandlordService landlordService;
    private final PropertyRepository propertyRepository;

    public List<Receipt> receipts(ReceiptDtoRequest receiptRequest) {

        List<Receipt> receipts = new ArrayList<>();

        var landlord = landlordService.findById(receiptRequest.landlordId());

        List<Property> properties = new ArrayList<>();

        if (receiptRequest.propertyIds() != null) {

            receiptRequest.propertyIds().forEach(id -> {
                Property property = propertyRepository.findById(id).orElse(null);
                properties.add(property);
            });
        }

        properties.forEach(

                property -> {

                    var tenant = property.getTenant();
                    var contact = tenant.getContacts().isEmpty() ? "" : tenant.getContacts().get(0).getContact();
                    var nextMonth = DateUtil.getNextMonth(receiptRequest.month());
                    var nextYear = String.valueOf(Integer.valueOf(receiptRequest.year()) + 1);
                    var locale = landlord.getCity() + " " + landlord.getState();

                    var receipt = Receipt.builder()
                                    .tenant(tenant.getName())
                                    .value(property.getValue())
                                    .propertyType(property.getPropertyType())
                                    .propertyNumber(property.getNumber())
                                    .locale(locale)
                                    .day(property.getPaymentDay())
                                    .month(receiptRequest.month())
                                    .nextMonth(nextMonth)
                                    .year(receiptRequest.year())
                                    .nextYear(nextYear)
                                    .observation(property.getObservation())
                                    .landlord(landlord.getName())
                                    .tenantContact(contact)
                                .build();

                    receipts.add(receipt);
                });

        Collections.sort(receipts, (r1, r2) -> r1.getTenant().compareTo(r2.getTenant()));

        return receipts;

    }

}
