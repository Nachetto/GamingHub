package dev.nacho.ghub.repository;


import dev.nacho.ghub.domain.model.security.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RolesRepository  extends JpaRepository<Roles, String> { }
