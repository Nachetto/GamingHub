package dev.nacho.ghub.domain.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AmistadDTO {
    private String nombreAmigo;
    private String estado;
    private String fechaSolicitud;
    private String fechaAceptacion;
}