package imoveis.aluguel.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    @Test
    @DisplayName("Deve criar user com senha codificada")
    void create_ShouldEncodePassword() {
        User user = new User();
        user.setPassword("plain");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Deve definir role padrão OPERADOR ao criar user")
    void create_WithoutRole_ShouldSetOperadorRole() {
        User user = new User();
        user.setPassword("plain");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.create(user);

        assertEquals(RoleEnum.OPERADOR, user.getRole());
    }

    @Test
    @DisplayName("Deve buscar user por id")
    void findById_ShouldReturnUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve buscar user por username")
    void findByUsername_ShouldReturnUser() {
        User user = new User();
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("admin");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar user inexistente")
    void findById_WithNonExistent_ShouldThrowNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(999L));
    }

    @Test
    @DisplayName("Deve listar users ordenados por username")
    void list_ShouldReturnOrderedUsers() {
        when(userRepository.findAll(any(Sort.class))).thenReturn(List.of(new User()));

        List<User> result = userService.list();

        assertEquals(1, result.size());
    }
}
