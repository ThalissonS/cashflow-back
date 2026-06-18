package com.thalisson.cash_flow.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String senha
) {}
