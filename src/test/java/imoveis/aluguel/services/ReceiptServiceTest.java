package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import imoveis.aluguel.dtos.ReceiptDtoRequest;
import imoveis.aluguel.entities.Contact;
import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.entities.Property;
import imoveis.aluguel.entities.Receipt;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.repositories.PropertyRepository;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock private LandlordService landlordService;
    @Mock private PropertyRepository propertyRepository;
    @InjectMocks private ReceiptService receiptService;

    @Test
    @DisplayName("Deve gerar recibos para propriedades")
    void receipts_ShouldGenerateReceipts() {
        Landlord landlord = new Landlord();
        landlord.setName("João Silva");
        landlord.setCity("São Paulo");
        landlord.setState("SP");

        Contact contact = new Contact();
        contact.setContact("11999999999");

        Tenant tenant = new Tenant();
        tenant.setName("Maria Santos");
        tenant.setContacts(List.of(contact));

        Property property = new Property();
        property.setTenant(tenant);
        property.setValue(1500.0);
        property.setNumber("123");
        property.setPaymentDay("10");

        ReceiptDtoRequest request = new ReceiptDtoRequest(List.of(1L), 1L, "janeiro", "2024");

        when(landlordService.findById(1L)).thenReturn(landlord);
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        List<Receipt> result = receiptService.receipts(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Maria Santos", result.get(0).getTenant());
        assertEquals(1500.0, result.get(0).getValue());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há propriedades")
    void receipts_WithNoProperties_ShouldReturnEmpty() {
        Landlord landlord = new Landlord();
        landlord.setCity("São Paulo");
        landlord.setState("SP");

        ReceiptDtoRequest request = new ReceiptDtoRequest(null, 1L, "janeiro", "2024");

        when(landlordService.findById(1L)).thenReturn(landlord);

        List<Receipt> result = receiptService.receipts(request);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
