package com.thalisson.cash_flow.dtos;

import com.thalisson.cash_flow.models.TipoLancamento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LancamentoRequest(
        @NotBlank(message = "Descricao e obrigatoria") String descricao,
        @NotNull @Positive(message = "Valor deve ser maior que zero") BigDecimal valor,
        @NotNull(message = "Tipo e obrigatorio") TipoLancamento tipo,
        @NotNull @Min(value = 2000, message = "Ano deve ser >= 2000") Integer ano,
        @NotNull @Min(1) @Max(12) Integer mes,
        Long categoriaId
) {}
