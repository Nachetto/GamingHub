package dev.nacho.ghub.web;

import dev.nacho.ghub.common.Constants;
import dev.nacho.ghub.domain.model.dto.PartidaDTO;
import dev.nacho.ghub.domain.service.PartidaService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL+ "/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping("/getAnfitrionedPartidasFromUsername")
    public ResponseEntity<List<PartidaDTO>> getAnfitrionedPartidasFromUsername(@RequestParam("username") @NotNull String username) {
        List<PartidaDTO> partidaDTOList = partidaService.findAnfitrionedPartidasFromUsername(username);
        if (partidaDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(partidaDTOList);
        }
    }
}
