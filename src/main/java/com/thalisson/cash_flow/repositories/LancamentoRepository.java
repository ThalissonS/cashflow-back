package com.thalisson.cash_flow.repositories;

import com.thalisson.cash_flow.models.Lancamento;
import com.thalisson.cash_flow.models.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findAllByUsuarioId(Long usuarioId);

    List<Lancamento> findAllByUsuarioIdAndAnoAndMes(Long usuarioId, int ano, int mes);

    Optional<Lancamento> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Query("SELECT COALESCE(SUM(l.valor), 0) FROM Lancamento l WHERE l.usuario.id = :usuarioId AND l.tipo = :tipo AND l.ano = :ano AND l.mes = :mes")
    BigDecimal somarPorTipoNoPeriodo(Long usuarioId, TipoLancamento tipo, int ano, int mes);

    @Query("SELECT COALESCE(SUM(l.valor), 0) FROM Lancamento l WHERE l.usuario.id = :usuarioId AND l.tipo = :tipo")
    BigDecimal totalAcumuladoPorTipo(Long usuarioId, TipoLancamento tipo);
}
