package com.thalisson.cash_flow.repositories;

import com.thalisson.cash_flow.models.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    @Query("SELECT SUM(d.valor) FROM Despesa d")
    BigDecimal calcularTotalDespesas();
}

