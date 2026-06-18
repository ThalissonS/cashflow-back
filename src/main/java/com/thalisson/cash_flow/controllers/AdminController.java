package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.UsuarioAdminDTO;
import com.thalisson.cash_flow.models.StatusUsuario;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/usuarios")
    public List<UsuarioAdminDTO> listarUsuarios() {
        return userRepository.findAll().stream()
                .map(UsuarioAdminDTO::de)
                .toList();
    }

    @PostMapping("/usuarios/{id}/aprovar")
    public ResponseEntity<UsuarioAdminDTO> aprovar(@PathVariable Long id) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        usuario.setStatus(StatusUsuario.APROVADO);
        return ResponseEntity.ok(UsuarioAdminDTO.de(userRepository.save(usuario)));
    }
}
