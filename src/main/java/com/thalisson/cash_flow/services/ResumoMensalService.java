package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.dtos.ResumoMensalDTO;
import com.thalisson.cash_flow.models.TipoLancamento;
import com.thalisson.cash_flow.repositories.LancamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class ResumoMensalService {

    private final LancamentoRepository lancamentoRepository;
    private final BancoCentralService bancoCentralService;

    public ResumoMensalService(LancamentoRepository lancamentoRepository,
                               BancoCentralService bancoCentralService) {
        this.lancamentoRepository = lancamentoRepository;
        this.bancoCentralService = bancoCentralService;
    }

    public ResumoMensalDTO calcular(Long usuarioId, int ano, int mes) {
        BigDecimal totalReceitas = lancamentoRepository
                .somarPorTipoNoPeriodo(usuarioId, TipoLancamento.RECEITA, ano, mes);

        BigDecimal totalGastosFixos = lancamentoRepository
                .somarPorTipoNoPeriodo(usuarioId, TipoLancamento.GASTO_FIXO, ano, mes);

        BigDecimal totalGastosVariaveis = lancamentoRepository
                .somarPorTipoNoPeriodo(usuarioId, TipoLancamento.GASTO_VARIAVEL, ano, mes);

        BigDecimal totalInvestidoNoMes = lancamentoRepository
                .somarPorTipoNoPeriodo(usuarioId, TipoLancamento.INVESTIMENTO, ano, mes);

        BigDecimal patrimonioInvestido = lancamentoRepository
                .totalAcumuladoPorTipo(usuarioId, TipoLancamento.INVESTIMENTO);

        BigDecimal totalGastos = totalGastosFixos.add(totalGastosVariaveis);
        BigDecimal saldo = totalReceitas.subtract(totalGastos).subtract(totalInvestidoNoMes);

        BigDecimal cdiAnual = bancoCentralService.getCdiAnual();

        // rendimento mensal: patrimonio * ((1 + cdi/100)^(1/12) - 1)
        double taxaMensal = Math.pow(1 + cdiAnual.doubleValue() / 100.0, 1.0 / 12) - 1;
        BigDecimal rendimentoEstimadoMes = patrimonioInvestido
                .multiply(BigDecimal.valueOf(taxaMensal), MathContext.DECIMAL64);

        return new ResumoMensalDTO(
                ano, mes,
                totalReceitas,
                totalGastosFixos,
                totalGastosVariaveis,
                totalInvestidoNoMes,
                totalGastos,
                saldo,
                patrimonioInvestido,
                cdiAnual,
                rendimentoEstimadoMes
        );
    }
}
