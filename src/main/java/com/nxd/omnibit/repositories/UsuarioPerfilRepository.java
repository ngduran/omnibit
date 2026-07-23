package com.nxd.omnibit.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxd.omnibit.models.UsuarioPerfil;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    // Método essencial para buscar o perfil operacional através do UUID do Auctoritas
    Optional<UsuarioPerfil> findByAuthUuid(UUID authUuid);

    @Query("SELECT up FROM UsuarioPerfil up JOIN FETCH up.cargo")
    List<UsuarioPerfil> findAllWithCargo();

}
