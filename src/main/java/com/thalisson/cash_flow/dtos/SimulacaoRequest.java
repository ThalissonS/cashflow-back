package com.thalisson.cash_flow.dtos;

import com.thalisson.cash_flow.models.TipoInvestimento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record SimulacaoRequest(
        @NotNull(message = "Tipo de investimento e obrigatorio") TipoInvestimento tipo,
        @NotNull @PositiveOrZero BigDecimal valorInicial,
        BigDecimal aporteMensal,
        @NotNull @Positive(message = "Prazo deve ser maior que zero") Integer prazoMeses,
        BigDecimal percentualCdi,
        Long bancoId
) {}
