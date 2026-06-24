package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.AuthResponse;
import com.thalisson.cash_flow.dtos.LoginRequest;
import com.thalisson.cash_flow.dtos.RegistroRequest;
import com.thalisson.cash_flow.models.Papel;
import com.thalisson.cash_flow.models.StatusUsuario;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.UserRepository;
import com.thalisson.cash_flow.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@RequestBody @Valid RegistroRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setStatus(StatusUsuario.PENDENTE);
        usuario.setPapel(Papel.ROLE_USER);
        userRepository.save(usuario);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtUtil.gerarToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getNome(), usuario.getPapel().name());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String novoToken = jwtUtil.gerarToken(usuario.getEmail());
        return new AuthResponse(novoToken, usuario.getNome(), usuario.getPapel().name());
    }
}
