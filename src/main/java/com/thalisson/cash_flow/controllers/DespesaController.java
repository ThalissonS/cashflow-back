package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.models.Despesa;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.services.DespesaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public List<Despesa> listarTodas(@AuthenticationPrincipal Usuario usuario) {
        return despesaService.listarTodas(usuario.getId());
    }

    @PostMapping
    public Despesa criar(@RequestBody Despesa despesa, @AuthenticationPrincipal Usuario usuario) {
        return despesaService.salvarDespesa(despesa, usuario);
    }

    @GetMapping("/total")
    public BigDecimal obterTotal(@AuthenticationPrincipal Usuario usuario) {
        return despesaService.obterTotalGasto(usuario.getId());
    }
}
