package dev.nacho.ghub.domain.model;


import dev.nacho.ghub.domain.model.enumeration.EstadoPartida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Partida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partida {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anfitrion_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Usuario anfitrion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Juego juego;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('CREADA', 'EN_CURSO', 'FINALIZADA')") // Ajustado para ENUM
    private EstadoPartida estado;

    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "DATETIME(6)") // Ajustado para DATETIME
    private LocalDateTime fechaCreacion;

    @Column(name = "duracion_estimada", columnDefinition = "INT(11)") // Ajustado para INT
    private Integer duracionEstimada;

    @Column(name = "config_extra", columnDefinition = "JSON") // Ajustado para JSON
    private String configExtra;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participante> participantes;

    @OneToOne(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Chat chat;
}
