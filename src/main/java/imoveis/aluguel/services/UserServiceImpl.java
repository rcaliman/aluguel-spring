package imoveis.aluguel.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.HoleEnum;
import imoveis.aluguel.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User saveUser(User user) {
        if(user.getHole() == null) {
            user.setHole(HoleEnum.OPERATOR);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
}
