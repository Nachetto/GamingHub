package dev.nacho.ghub.web;


import com.google.api.Authentication;
import dev.nacho.ghub.domain.model.dto.AmistadDTO;
import dev.nacho.ghub.domain.model.dto.FriendRequestDTO;
import dev.nacho.ghub.domain.model.dto.FriendshipDTO;
import dev.nacho.ghub.domain.model.dto.PartidaDTO;
import dev.nacho.ghub.domain.service.AmistadService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final AmistadService friendService;

    public FriendController(AmistadService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/request")
    public ResponseEntity<FriendshipDTO> requestFriend(
            @RequestBody FriendRequestDTO requestDto,
            Authentication authentication) {

        // Suponemos que authentication.getName() es el UUID de Usuario
        String requesterId = authentication.getDescriptorForType().getName();

        FriendshipDTO dto = friendService.requestFriend(requesterId, requestDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getAmigosFromUsername")
    public ResponseEntity<List<AmistadDTO>> getAllAcceptedFriendsFromUsername(@RequestParam("username") @NotNull String username) {
//        List<AmistadDTO> amigos = friendService.getFriendsFromUsername(username);
        List<AmistadDTO> amigos = List.of(
                new AmistadDTO("Juan Pérez", "ACEPTADO", LocalDateTime.of(2023, 5, 10, 14, 30).toString(), LocalDateTime.of(2023, 5, 12, 16, 45).toString()),
                new AmistadDTO("María López", "PENDIENTE", LocalDateTime.of(2023, 6, 1, 10, 0).toString(), null),
                new AmistadDTO("Carlos Gómez", "RECHAZADO", LocalDateTime.of(2023, 4, 20, 9, 15).toString(), LocalDateTime.of(2023, 4, 21, 11, 0).toString()),
                new AmistadDTO("Ana Torres", "ACEPTADO", LocalDateTime.of(2023, 3, 5, 18, 20).toString(), LocalDateTime.of(2023, 3, 6, 20, 0).toString())
        );

        if (amigos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(amigos);
        }
    }

}