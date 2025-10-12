package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.TenantDtoRequest;
import imoveis.aluguel.entities.Tenant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class TenantMapperTest {

    private final TenantMapper tenantMapper = Mappers.getMapper(TenantMapper.class);

    @Test
    void shouldMapDtoToTenant() {

        TenantDtoRequest dto = new TenantDtoRequest(null, "Novo Inquilino", null, "123", "111.111", null, null, null,
                null, null, null, "Brasileiro");

        Tenant tenant = tenantMapper.toTenant(dto);

        assertNotNull(tenant);
        assertEquals("Novo Inquilino", tenant.getName());
        assertEquals("Brasileiro", tenant.getNationality());
        assertNull(tenant.getId());
        assertTrue(tenant.getProperties().isEmpty());

    }
}