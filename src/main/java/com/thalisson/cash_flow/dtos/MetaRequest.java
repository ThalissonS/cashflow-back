package com.thalisson.cash_flow.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MetaRequest(
        @NotBlank(message = "Nome e obrigatorio") String nome,
        @NotNull @PositiveOrZero BigDecimal valorAlvo,
        @NotNull @PositiveOrZero BigDecimal aporteMensal,
        BigDecimal valorInicial
) {}
