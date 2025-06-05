package dev.nacho.ghub.domain.model;

import dev.nacho.ghub.domain.model.enumeration.EstadoParticipante;
import dev.nacho.ghub.domain.model.security.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "Participante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participante {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, columnDefinition = "CHAR(36)")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false, columnDefinition = "CHAR(36)")
    private Partida partida;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('ACTIVO', 'INACTIVO', 'EXPULSADO')")
    private EstadoParticipante estado;

    @Column(name = "rol_en_juego", length = 50)
    private String rolEnJuego;

    @Column(name = "puntuacion", columnDefinition = "INT(11)")
    private Integer puntuacion;
}
