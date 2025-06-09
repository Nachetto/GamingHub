package dev.nacho.ghub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Mensaje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Usuario autor;

    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false) // Ajustado para TEXT
    private String contenido;

    @Column(name = "fecha_envio", nullable = false, columnDefinition = "DATETIME(6)") // Ajustado para DATETIME
    private LocalDateTime fechaEnvio;

    @Column(name = "es_sistema", nullable = false, columnDefinition = "TINYINT(1)") // Ajustado para TINYINT
    private boolean esSistema;
}
