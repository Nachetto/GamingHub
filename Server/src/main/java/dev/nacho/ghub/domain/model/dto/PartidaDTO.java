package dev.nacho.ghub.domain.model.dto;

import dev.nacho.ghub.domain.model.Partida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartidaDTO {
    private String id;
    private String anfitrionNombre;
    private String juegoNombre;
    private String estado;
    private String fechaCreacion;

    public PartidaDTO toPartidaCardDto(Partida partida) {
        return new PartidaDTO(
                partida.getId(),
                partida.getAnfitrion().getNombreUsuario(),
                partida.getJuego().getNombre(),
                partida.getEstado().name(),
                partida.getFechaCreacion().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // Formatea la fecha
        );
    }
}