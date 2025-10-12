package imoveis.aluguel.mappers;

import imoveis.aluguel.dtos.UserDtoRequest;
import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapDtoToUser() {
       
        UserDtoRequest dto = new UserDtoRequest("usuario", "senha123", RoleEnum.OPERADOR);

        User user = userMapper.toUser(dto);

        assertNotNull(user);
        assertEquals("usuario", user.getUsername());
        assertEquals("senha123", user.getPassword());
        assertNull(user.getId());
        assertNull(user.getRole());
        
    }
}