package dev.nacho.ghub.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Juego")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Juego {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "reglas", columnDefinition = "TEXT")
    private String reglas;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partida> partidas;
}
