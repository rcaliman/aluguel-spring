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

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals(RoleEnum.OPERADOR, result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("saveUser - Deve salvar um usuário com a role especificada")
    void saveUser_ShouldSaveUserWithSpecifiedRole() {
        user.setRole(RoleEnum.ADMIN);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals(RoleEnum.ADMIN, result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
