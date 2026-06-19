package com.thalisson.cash_flow.dtos;

import com.thalisson.cash_flow.models.TipoInvestimento;

import java.math.BigDecimal;

public record SimulacaoResponse(
        TipoInvestimento tipo,
        BigDecimal totalInvestido,
        BigDecimal valorBruto,
        BigDecimal rendimentoBruto,
        BigDecimal impostoRenda,
        BigDecimal valorLiquido,
        BigDecimal rendimentoLiquido,
        BigDecimal aliquotaIR,
        BigDecimal taxaEfetivaMensalPercent,
        BigDecimal cdiAnual
) {}
