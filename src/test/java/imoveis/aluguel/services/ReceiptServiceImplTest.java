package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import imoveis.aluguel.enums.PropertyTypeEnum;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {

    @Mock
    private LandlordService landlordService;
    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    private Landlord landlord;
    private Property propertyA;
    private Property propertyB;
    private Tenant tenantA;
    private Tenant tenantB;

    @BeforeEach
    void setUp() {
        landlord = new Landlord();
        landlord.setId(100L);
        landlord.setName("Sr. Locador");

        Contact contactA = new Contact();
        contactA.setContact("99999-AAAA");
        tenantA = new Tenant();
        tenantA.setName("Ana Inquilino");
        tenantA.setContacts(List.of(contactA));

        Contact contactB = new Contact();
        contactB.setContact("99999-BBBB");
        tenantB = new Tenant();
        tenantB.setName("Bruno Inquilino");
        tenantB.setContacts(List.of(contactB));

        propertyA = new Property();
        propertyA.setId(1L);
        propertyA.setValue(1000.0);
        propertyA.setPropertyType(PropertyTypeEnum.APARTAMENTO);
        propertyA.setNumber("101");
        propertyA.setPaymentDay("10");
        propertyA.setTenant(tenantA);

        propertyB = new Property();
        propertyB.setId(2L);
        propertyB.setValue(1200.0);
        propertyB.setPropertyType(PropertyTypeEnum.QUITINETE);
        propertyB.setNumber("A");
        propertyB.setPaymentDay("15");
        propertyB.setTenant(tenantB);
    }

    @Test
    @DisplayName("Deve criar e ordenar recibos por nome do inquilino")
    void receipts_ShouldCreateAndSortReceipts_WhenDataIsValid() {

        ReceiptDtoRequest request = new ReceiptDtoRequest(List.of(2L, 1L), 100L, "Janeiro", "2025");

        when(landlordService.findById(100L)).thenReturn(landlord);
        when(propertyService.findById(1L)).thenReturn(propertyA);
        when(propertyService.findById(2L)).thenReturn(propertyB);

        List<Receipt> receipts = receiptService.receipts(request);

        assertNotNull(receipts);
        assertEquals(2, receipts.size());

        assertEquals("Ana Inquilino", receipts.get(0).getTenant());
        assertEquals("Bruno Inquilino", receipts.get(1).getTenant());

        Receipt firstReceipt = receipts.get(0);
        assertEquals(1000.0, firstReceipt.getValue());
        assertEquals("Sr. Locador", firstReceipt.getLandlord());
        assertEquals("99999-AAAA", firstReceipt.getTenantContact());

        verify(landlordService, times(1)).findById(100L);
        verify(propertyService, times(1)).findById(1L); // Espera 1 chamada
        verify(propertyService, times(1)).findById(2L); // Espera 1 chamada

    }

    @Test
    @DisplayName("Deve lançar exceção se o inquilino não tiver contatos")
    void receipts_ShouldThrowException_WhenTenantHasNoContacts() {

        tenantA.setContacts(new ArrayList<>());
        ReceiptDtoRequest request = new ReceiptDtoRequest(List.of(1L), 100L, "Janeiro", "2025");

        when(landlordService.findById(100L)).thenReturn(landlord);
        when(propertyService.findById(1L)).thenReturn(propertyA);
        List<Receipt> receipts = receiptService.receipts(request);

        assertNotNull(receipts);
        assertEquals(1, receipts.size());
        assertEquals("", receipts.get(0).getTenantContact());

    }
}