package dev.nacho.ghub.domain.service;

import dev.nacho.ghub.domain.model.dto.PartidaDTO;

import java.util.List;
import java.util.stream.LongStream;

public interface PartidaService {
    List<PartidaDTO> findAnfitrionedPartidasFromUsername(String username);
}
