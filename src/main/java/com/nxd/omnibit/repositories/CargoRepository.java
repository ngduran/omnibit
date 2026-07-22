package com.nxd.omnibit.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxd.omnibit.models.Cargo;

import org.springframework.transaction.annotation.Transactional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM tb_cargo c WHERE c.nome = ?1", nativeQuery = true)
    Optional<Cargo> findByNome(String nome);

    @Transactional(readOnly = true)
    boolean existsByNome(String nome);

    Optional<Cargo> findByUuid(UUID uuid);

}