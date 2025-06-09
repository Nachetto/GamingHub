package dev.nacho.ghub.domain.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import dev.nacho.ghub.common.security.PasswordHash;
import dev.nacho.ghub.domain.error.InternalServerException;
import dev.nacho.ghub.domain.model.dto.GoogleUserInfo;
import dev.nacho.ghub.domain.model.security.Token;
import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.domain.service.security.JwtService;
import dev.nacho.ghub.repository.RolesRepository;
import dev.nacho.ghub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Collections;

@Service
public class LoginService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

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

    public Usuario save(Usuario userEntity) {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);

        userEntity.setCodigo(Base64.getUrlEncoder().encodeToString(salt));
        try {
            userEntity.setPassword(encriptar.createHash(userEntity.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Usuario user = userRepository.save(userEntity);
        rolesRepository.saveAll(user.getRoles());
        return user;
    }

    public Usuario getUserByUsername(String username) {
        return userRepository.findByNombreUsuario(username).orElse(null);
    }

    public Usuario findByGoogleId(String sub) {
        return userRepository.findByGoogleId(sub).orElse(null);
    }

    public GoogleUserInfo verifyAndExtract(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();

                GoogleUserInfo userInfo = new GoogleUserInfo();
                userInfo.setSub(payload.getSubject());
                userInfo.setEmail(payload.getEmail());
                userInfo.setName((String) payload.get("name"));
                return userInfo;
            }
        } catch (Exception e) {
            throw new InternalServerException("Error al verificar el token de Google: " + e.getMessage());
        }
        return null;
    }
}
