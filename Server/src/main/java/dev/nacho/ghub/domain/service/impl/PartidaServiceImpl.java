package dev.nacho.ghub.domain.service.impl;

import dev.nacho.ghub.domain.model.Usuario;
import dev.nacho.ghub.domain.model.dto.PartidaDTO;
import dev.nacho.ghub.domain.service.PartidaService;
import dev.nacho.ghub.repository.PartidaRepository;
import dev.nacho.ghub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartidaServiceImpl implements PartidaService {
    private final UserRepository userRepository;
    private final PartidaRepository partidaRepository;

    public PartidaServiceImpl(UserRepository userRepository, PartidaRepository partidaRepository) {
        this.userRepository = userRepository;
        this.partidaRepository = partidaRepository;
    }

    @Override
    public List<PartidaDTO> findAnfitrionedPartidasFromUsername(String username) {
        Usuario user  = userRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return partidaRepository.findAnfitrionedPartidasFromUserId(user)
                .stream()
                .map(partida -> new PartidaDTO()
                        .toPartidaCardDto(partida))
                .toList();
    }
}
