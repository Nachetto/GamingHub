package dev.nacho.ghub.domain.service.impl;

import dev.nacho.ghub.common.security.PasswordHash;
import dev.nacho.ghub.domain.model.security.Token;
import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.domain.service.security.JwtService;
import dev.nacho.ghub.repository.RolesRepository;
import dev.nacho.ghub.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class LoginService {
    private final PasswordHash encriptar;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RolesRepository rolesRepository;

    public LoginService(
            JwtService jwtService,
            PasswordHash encriptarSimetrico,
            UserDetailsService userDetailsService,
            UserRepository userRepository,
            RolesRepository rolesRepository) {
        this.jwtService = jwtService;
        this.encriptar = encriptarSimetrico;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    public Token login(String user, String pass) {
        boolean isAuthenticated = userRepository.findByNombreUsuario(user)
                .map(c -> {
                            try {
                                return encriptar.validatePassword(pass, c.getPassword())
                                        && c.isEnabled();
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).orElse(false);

        if (isAuthenticated) {
            String token = jwtService.generateToken(userDetailsService.loadUserByUsername(user));
            String refreshToken = jwtService.generateRefreshToken(userDetailsService.loadUserByUsername(user));
            return new Token(
                    token,
                    refreshToken
            );
        } else {
            return null;
        }
    }

    public boolean save(Usuario userEntity) {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);

        userEntity.setCodigo(Base64.getUrlEncoder().encodeToString(salt));
        try {
            userEntity.setPassword(encriptar.createHash(userEntity.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        rolesRepository.saveAll(userEntity.getRoles());

        return userRepository.save(userEntity).equals(userEntity);
    }


    public Usuario getUserByUsername(String username) {
        return userRepository.findByNombreUsuario(username).orElse(null);
    }

}
