package dev.nacho.ghub.domain.model.security;

import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.domain.model.enumeration.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20)")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, columnDefinition = "CHAR(36)")
    private Usuario usuario;

    @Column(name = "rol", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;
}