package dev.nacho.ghub.domain.service.security;

import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByNombreUsuario(username).orElseThrow(() -> new UsernameNotFoundException("Usuario not found"));

        return User.builder()
                .username(user.getNombreUsuario())
                .password(user.getPassword())
                .roles(
                        user.getRoles().stream()
                                .map(roles -> roles.getRol().name())
                                .collect(Collectors.joining(",")))
                .authorities(user.getRoles().stream()
                        .map(roles -> roles.getRol().name())
                        .collect(Collectors.joining(",")))
                .build();
    }
}
