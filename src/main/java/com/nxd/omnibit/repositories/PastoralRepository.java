package com.nxd.omnibit.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxd.omnibit.models.Pastoral;

import org.springframework.transaction.annotation.Transactional;

public interface PastoralRepository extends JpaRepository<Pastoral, Long> {
    
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM tb_pastoral c WHERE c.nome = ?1", nativeQuery = true)
    Optional<Pastoral> findByNome(String nome);

    @Transactional(readOnly = true)
    boolean existsByNome(String nome);

    Optional<Pastoral> findByUuid(UUID uuid);
}
