package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.LancamentoRequest;
import com.thalisson.cash_flow.models.Lancamento;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.services.LancamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lancamento criar(@Valid @RequestBody LancamentoRequest req,
                            @AuthenticationPrincipal Usuario usuario) {
        return lancamentoService.salvar(req, usuario);
    }

    @GetMapping
    public List<Lancamento> listarTodos(@AuthenticationPrincipal Usuario usuario) {
        return lancamentoService.listarTodos(usuario.getId());
    }

    @GetMapping("/mes/{ano}/{mes}")
    public List<Lancamento> listarPorMes(@PathVariable int ano,
                                         @PathVariable int mes,
                                         @AuthenticationPrincipal Usuario usuario) {
        return lancamentoService.listarPorMes(usuario.getId(), ano, mes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        lancamentoService.deletar(id, usuario.getId());
    }
}
