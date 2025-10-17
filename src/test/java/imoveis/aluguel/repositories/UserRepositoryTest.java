package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "imoveis.aluguel.entities")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User createValidUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole(RoleEnum.OPERADOR);
        return user;
    }

    @Test
    @DisplayName("Deve persistir um usuário e preencher createdAt via @PrePersist")
    void save_ShouldPersistUserAndSetCreatedAt() {

        User user = createValidUser();
        assertNull(user.getCreatedAt());

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());

    }

    @Test
    @DisplayName("Deve preencher updatedAt via @PreUpdate ao atualizar um usuário")
    void update_ShouldSetUpdatedAt() {

        User persistedUser = entityManager.persistFlushFind(createValidUser());
        assertNull(persistedUser.getUpdatedAt());

        persistedUser.setPassword("newPassword456");
        userRepository.saveAndFlush(persistedUser);

        assertNotNull(persistedUser.getUpdatedAt());

    }

    @Test
    @DisplayName("Deve falhar ao salvar um usuário com 'username' duplicado")
    void save_WhenUsernameIsDuplicate_ShouldThrowException() {

        userRepository.saveAndFlush(createValidUser());

        User duplicateUser = new User();
        duplicateUser.setUsername("testuser");
        duplicateUser.setPassword("anotherPassword");
        duplicateUser.setRole(RoleEnum.ADMIN);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(duplicateUser);
        });

    }

    @Test
    @DisplayName("Deve falhar ao salvar um usuário com 'username' nulo")
    void save_WhenUsernameIsNull_ShouldThrowException() {

        User user = createValidUser();
        user.setUsername(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    @DisplayName("Deve falhar ao salvar um usuário com 'password' nulo")
    void save_WhenPasswordIsNull_ShouldThrowException() {

        User user = createValidUser();
        user.setPassword(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });

    }

    @Test
    @DisplayName("Deve falhar ao salvar um usuário com 'role' nula")
    void save_WhenRoleIsNull_ShouldThrowException() {

        User user = createValidUser();
        user.setRole(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });

    }
}