package com.thalisson.cash_flow.dtos;

import java.math.BigDecimal;

public record ProjecaoMetaResponse(
        String nome,
        BigDecimal valorAlvo,
        BigDecimal aporteMensal,
        BigDecimal valorInicial,
        int mesesParaAtingir,
        BigDecimal cdiAnual,
        String dataProjetada
) {}
