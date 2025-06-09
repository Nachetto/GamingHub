package dev.nacho.ghub.web;

import dev.nacho.ghub.common.Constants;
import dev.nacho.ghub.domain.model.dto.GoogleUserInfo;
import dev.nacho.ghub.domain.model.dto.LoginRequestDTO;
import dev.nacho.ghub.domain.model.enumeration.RolUsuario;
import dev.nacho.ghub.domain.model.security.Roles;
import dev.nacho.ghub.domain.model.security.Token;
import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.domain.service.impl.LoginService;
import dev.nacho.ghub.domain.service.impl.SmsLoginServiceImpl;
import dev.nacho.ghub.domain.service.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(Constants.BASE_URL+ Constants.AUTH_URL)
public class AuthController {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LoginService servicioLogin;
    private final SmsLoginServiceImpl smsLoginServiceImpl;

    public AuthController(JwtService jwtService, UserDetailsService userDetailsService, LoginService servicioLogin, SmsLoginServiceImpl smsLoginServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.servicioLogin = servicioLogin;
        this.smsLoginServiceImpl = smsLoginServiceImpl;
    }

    @PostMapping( Constants.PATH_LOGIN)
    public ResponseEntity<Token> login(@RequestBody LoginRequestDTO loginRequest) {
        Token tokens = servicioLogin.login(loginRequest.getUsername(), loginRequest.getPassword());
        return tokens == null
                ? ResponseEntity.status(401).body(null)
                : ResponseEntity.ok(tokens);
    }

    @PostMapping("/google-login")
    public ResponseEntity<Token> googleLogin(@RequestParam String idToken) {
        // 1. Validar el idToken con Google y extraer datos
        GoogleUserInfo userInfo = servicioLogin.verifyAndExtract(idToken);
        if (userInfo == null) {
            return ResponseEntity.status(401).build();
        }

        // 2. Buscar o crear usuario en tu base de datos
        Usuario usuario = servicioLogin.findByGoogleId(userInfo.getSub());
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setGoogleId(userInfo.getSub());
            usuario.setEmail(userInfo.getEmail());
            usuario.setNombreUsuario(userInfo.getName());
            usuario.setEnabled(true);
            usuario.setRoles(Set.of(new Roles(null,usuario, RolUsuario.USUARIO)));
            servicioLogin.save(usuario);
        }

        // 3. Generar tu propio JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getNombreUsuario());
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new Token(jwt, refreshToken));
    }

    @PostMapping(Constants.PATH_REGISTER)
    public ResponseEntity<Void> signup(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String phone) {
        // Crear y persistir el usuario
        Usuario userEntity = Usuario.builder()
                .id(new UUID(0, 0))
                .nombreUsuario(username)
                .password(password)
                .telefono(phone)
                .build();
        userEntity.setEnabled(true);
        Roles rolUsuario = new Roles(null, userEntity, RolUsuario.USUARIO);
        userEntity.setRoles(Set.of(rolUsuario));

        Usuario usuarioPersistido = servicioLogin.save(userEntity);
        if (usuarioPersistido == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.status(201).build();
    }

    @PostMapping(Constants.PATH_REFRESH_TOKEN)
    public ResponseEntity<Token> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            String newToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            Token token = new Token(newToken, newRefreshToken);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User oauthUser) {
        // user.getName() suele ser el "sub" de Google
        String googleId = oauthUser.getName();

        String email    = oauthUser.getAttribute("email")== null ? "" : oauthUser.getAttribute("email");
        String nombre   = oauthUser.getAttribute("name")== null ? "" : oauthUser.getAttribute("name");

        return Map.of(
                "googleId", googleId,
                "email",    email,
                "name",     nombre
        );
    }


    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String phoneNumber) {
        smsLoginServiceImpl.sendCode(phoneNumber);
        return ResponseEntity.ok("Code sent");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<String> validateCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean valid = smsLoginServiceImpl.validateCode(phoneNumber, code);
        return valid ? ResponseEntity.ok("Logged in successfully")
                : ResponseEntity.badRequest().body("Invalid code");
    }

    @GetMapping("/")
    public String home() {
        return "Estas dentro!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Estas dentro y seguro!";
    }

}
