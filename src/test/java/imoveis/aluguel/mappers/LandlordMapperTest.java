package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.LandlordDtoRequest;
import imoveis.aluguel.entities.Landlord;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class LandlordMapperTest {

    private final LandlordMapper landlordMapper = Mappers.getMapper(LandlordMapper.class);

    @Test
    void shouldMapDtoToLandlord() {

        LandlordDtoRequest dto = new LandlordDtoRequest(null, "Novo Locador", null, null, "222.222", null, null, null,
                null, null, null, "Brasileiro", true);

        Landlord landlord = landlordMapper.toLandlord(dto);

        assertNotNull(landlord);
        assertEquals("Novo Locador", landlord.getName());
        assertEquals("222.222", landlord.getCpfCnpj());
        assertTrue(landlord.getMain());
        assertNull(landlord.getCreatedAt());

    }
}