package com.thalisson.cash_flow.dtos;

import com.thalisson.cash_flow.models.Papel;
import com.thalisson.cash_flow.models.StatusUsuario;
import com.thalisson.cash_flow.models.Usuario;

public record UsuarioAdminDTO(Long id, String nome, String email, Papel papel, StatusUsuario status) {
    public static UsuarioAdminDTO de(Usuario u) {
        return new UsuarioAdminDTO(u.getId(), u.getNome(), u.getEmail(), u.getPapel(), u.getStatus());
    }
}
