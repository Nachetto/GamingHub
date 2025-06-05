package dev.nacho.ghub.domain.service;

import dev.nacho.ghub.domain.model.dto.FriendRequestDTO;
import dev.nacho.ghub.domain.model.dto.FriendshipDTO;

public interface AmistadService {

    FriendshipDTO requestFriend(String requesterId, FriendRequestDTO request);

}