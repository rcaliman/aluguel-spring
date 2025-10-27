package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user) {

        if (user.getRole() == null) {
            user.setRole(RoleEnum.OPERADOR);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    @Transactional
    public void update(User user, Long id) {

        User foundedUser = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Usuário de id %d não encontrado.", id)));

        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            foundedUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            foundedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRole() != null) {
            foundedUser.setRole(user.getRole());
        }

        userRepository.save(foundedUser);

    }

    @Transactional
    public void updatePassword(Long id, String password) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Usuário de id %d não encontrado", id)));

        User loggedUser = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new NotFoundException(String.format("Username %s não encontrado", auth.getName())));

        if (password != null && !password.isBlank() && user.getUsername().equals(loggedUser.getUsername())) {

            var newPassword = passwordEncoder.encode(password);
            user.setPassword(newPassword);
            userRepository.save(user);
            return;

        }

        throw new RuntimeException(
                "Não foi possível alterar a senha. Verifique se você tem permissão para esta operação.");

    }

    public User findById(Long id) {

        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Usuário de id %d não encontrado.", id)));

    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format("O usuario %s não foi localizado.", username)));

    }

    @Transactional
    public void delete(Long id) {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        String usernameLogged = securityContext.getAuthentication().getName();

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("O usuário de id %d não foi encontrado", id)));

        if (user.getUsername().equals(usernameLogged)) {
            throw new RuntimeException(
                    String.format("O usuário %s está logado e não pode ser deletado", usernameLogged));
        }

        userRepository.deleteById(id);

    }

    public List<User> list() {

        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));

    }

}