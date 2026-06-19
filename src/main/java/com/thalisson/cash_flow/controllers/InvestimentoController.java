package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.BancoDTO;
import com.thalisson.cash_flow.dtos.OfertaDTO;
import com.thalisson.cash_flow.dtos.SimulacaoRequest;
import com.thalisson.cash_flow.dtos.SimulacaoResponse;
import com.thalisson.cash_flow.models.TipoInvestimento;
import com.thalisson.cash_flow.services.InvestimentoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    @GetMapping("/tipos")
    public List<TipoInvestimento> tipos() {
        return investimentoService.tipos();
    }

    @GetMapping("/bancos")
    public List<BancoDTO> bancos() {
        return investimentoService.bancos();
    }

    @GetMapping("/ofertas")
    public List<OfertaDTO> ofertas(@RequestParam TipoInvestimento tipo) {
        return investimentoService.ofertas(tipo);
    }

    @PostMapping("/simular")
    public SimulacaoResponse simular(@Valid @RequestBody SimulacaoRequest req) {
        return investimentoService.simular(req);
    }
}
