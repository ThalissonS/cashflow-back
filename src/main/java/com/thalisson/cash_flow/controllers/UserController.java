package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController avisa ao Spring que esta classe vai expor endpoints (URLs) para a internet.
@RestController
// @RequestMapping define o "endereço base". Todas as rotas aqui começarão com /usuarios
@RequestMapping("/usuarios")
public class UserController {

    // @Autowired é a "Injeção de Dependência".
    // Dizemos ao Spring: "Traga uma instância do UserRepository para eu usar aqui dentro".
    @Autowired
    private UserRepository userRepository;

    // @GetMapping indica que este método responde a pedidos de LEITURA (GET)
    @GetMapping
    public List<Usuario> listarTodos() {
        return userRepository.findAll();
    }

    // @PostMapping indica que este método responde a pedidos de CRIAÇÃO (POST)
    @PostMapping
    // @RequestBody diz que os dados do usuário virão no "corpo" da requisição (em formato JSON)
    public Usuario criar(@RequestBody Usuario usuario) {
        return userRepository.save(usuario);
    }
}