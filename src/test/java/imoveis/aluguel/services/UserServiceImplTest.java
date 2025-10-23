package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.mappers.UserMapper;
import imoveis.aluguel.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    @DisplayName("saveUser - Deve salvar um usuário com a role padrão OPERADOR")
    void saveUser_ShouldSaveUserWithDefaultRole() {
        UserDtoResponse expectedResponse = new UserDtoResponse(1L, "testuser", "encodedPassword", RoleEnum.OPERADOR, null, null);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.dtoResponse(any(User.class))).thenReturn(expectedResponse);

        UserDtoResponse result = userService.create(user);

        assertNotNull(result);
        assertEquals(RoleEnum.OPERADOR, result.role());
        assertEquals("encodedPassword", result.password());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("saveUser - Deve salvar um usuário com a role especificada")
    void saveUser_ShouldSaveUserWithSpecifiedRole() {
        user.setRole(RoleEnum.ADMIN);
        UserDtoResponse expectedResponse = new UserDtoResponse(1L, "testuser", "encodedPassword", RoleEnum.ADMIN, null, null);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.dtoResponse(any(User.class))).thenReturn(expectedResponse);

        UserDtoResponse result = userService.create(user);

        assertNotNull(result);
        assertEquals(RoleEnum.ADMIN, result.role());
        assertEquals("encodedPassword", result.password());
        verify(userRepository, times(1)).save(user);
    }
}
