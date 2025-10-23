package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.UserDtoResponse;
import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.UserMapper;
import imoveis.aluguel.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private UserMapper userMapper;

    @Override
    @Transactional
    public UserDtoResponse create(User user) {

        if (user.getRole() == null) {
            user.setRole(RoleEnum.OPERADOR);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var newUser = userRepository.save(user);

        return userMapper.dtoResponse(newUser);

    }

    @Override
    @Transactional
    public UserDtoResponse update(User user, Long id) {

        User foundedUser = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Usuário de id %d não encontrado.", id)));

        // Atualiza o username se foi modificado
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            foundedUser.setUsername(user.getUsername());
        }

        // Atualiza a senha apenas se foi fornecida (não vazia)
        // Se vier vazia, mantém a senha atual
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            foundedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // Se a senha vier vazia/nula, não faz nada - mantém a senha existente

        // Atualiza o role se foi fornecido
        if (user.getRole() != null) {
            foundedUser.setRole(user.getRole());
        }

        var savedUser = userRepository.save(foundedUser);

        return userMapper.dtoResponse(savedUser);

    }

    @Override
    public UserDtoResponse findById(Long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Usuário de id %d não encontrado.", id)));

        return userMapper.dtoResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        
        SecurityContext securityContext = SecurityContextHolder.getContext();

        String usernameLogged = securityContext.getAuthentication().getName();

        User user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException(String.format("O usuário de id %d não foi encontrado", id))
        );

        if(user.getUsername().equals(usernameLogged)) {
            throw new RuntimeException(String.format("O usuário %s está logado e não pode ser deletado", usernameLogged));
        }

        userRepository.deleteById(id);
        
    }

    @Override
    public List<UserDtoResponse> list() {

        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username")).stream()
                    .map(userMapper::dtoResponse)
                    .toList();

    }

}