package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.ResumoMensalDTO;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.services.ResumoMensalService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private final ResumoMensalService resumoMensalService;

    public ResumoController(ResumoMensalService resumoMensalService) {
        this.resumoMensalService = resumoMensalService;
    }

    @GetMapping("/{ano}/{mes}")
    public ResumoMensalDTO resumo(@PathVariable int ano,
                                  @PathVariable int mes,
                                  @AuthenticationPrincipal Usuario usuario) {
        return resumoMensalService.calcular(usuario.getId(), ano, mes);
    }
}
