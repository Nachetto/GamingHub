package dev.nacho.ghub.domain.model;

import dev.nacho.ghub.domain.model.enumeration.EstadoAmistad;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Amistad",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "amigo_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amistad {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amigo_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Usuario amigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA')") // Ajustado para ENUM
    private EstadoAmistad estado;

    @Column(name = "fecha_solicitud", nullable = false, columnDefinition = "DATETIME(6)") // Coincide con la definición en la base de datos
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aceptacion", columnDefinition = "DATETIME(6)") // Coincide con la definición en la base de datos
    private LocalDateTime fechaAceptacion;
}
