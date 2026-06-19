package com.thalisson.cash_flow.dtos;

import java.math.BigDecimal;

public record ResumoMensalDTO(
        int ano,
        int mes,
        BigDecimal totalReceitas,
        BigDecimal totalGastosFixos,
        BigDecimal totalGastosVariaveis,
        BigDecimal totalInvestidoNoMes,
        BigDecimal totalGastos,
        BigDecimal saldo,
        BigDecimal patrimonioInvestido,
        BigDecimal cdiAnual,
        BigDecimal rendimentoEstimadoMes
) {}
