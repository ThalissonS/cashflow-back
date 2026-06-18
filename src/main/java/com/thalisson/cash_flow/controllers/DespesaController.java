package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.models.Despesa;
import com.thalisson.cash_flow.repositories.DespesaRepository;
import com.thalisson.cash_flow.services.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired private DespesaService despesaService;

    @GetMapping
    public List<Despesa> listarTodas() {
        return despesaService.listarTodas();
    }

    @PostMapping
    public Despesa criar(@RequestBody Despesa despesa) {
        return despesaService.salvarDespesa(despesa);
    }

    @GetMapping("/total")
    public BigDecimal obterTotal() { return despesaService.obterTotalGasto(); }

}