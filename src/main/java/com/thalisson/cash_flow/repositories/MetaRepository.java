package com.thalisson.cash_flow.repositories;

import com.thalisson.cash_flow.models.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

    List<Meta> findAllByUsuarioId(Long usuarioId);

    Optional<Meta> findByIdAndUsuarioId(Long id, Long usuarioId);
}
