package dev.nacho.ghub.domain.model;

import dev.nacho.ghub.domain.model.enumeration.TipoChat;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "ENUM('PRIVADO', 'PARTIDA')") // Ajustado para ENUM
    private TipoChat tipo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", unique = true, columnDefinition = "CHAR(36)") // Coincide con la definición en la base de datos
    private Partida partida;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes;
}
