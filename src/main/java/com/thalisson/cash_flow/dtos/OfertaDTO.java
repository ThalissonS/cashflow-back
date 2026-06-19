package com.thalisson.cash_flow.dtos;

import com.thalisson.cash_flow.models.TipoInvestimento;

import java.math.BigDecimal;

public record OfertaDTO(
        Long id,
        String banco,
        TipoInvestimento tipo,
        BigDecimal percentualCdi,
        int prazoMinimoMeses
) {}
