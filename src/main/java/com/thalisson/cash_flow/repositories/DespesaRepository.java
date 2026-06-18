package com.thalisson.cash_flow.repositories;

import com.thalisson.cash_flow.models.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllByUsuarioId(Long usuarioId);

    @Query("SELECT SUM(d.valor) FROM Despesa d WHERE d.usuario.id = :usuarioId")
    BigDecimal calcularTotalDespesasByUsuarioId(Long usuarioId);
}
