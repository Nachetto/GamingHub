package dev.nacho.ghub.repository;


import dev.nacho.ghub.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombreUsuario(String username);
    Optional<Usuario> findByGoogleId(String googleId);
}