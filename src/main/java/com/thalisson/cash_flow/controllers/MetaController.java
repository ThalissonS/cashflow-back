package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.dtos.MetaRequest;
import com.thalisson.cash_flow.dtos.ProjecaoMetaResponse;
import com.thalisson.cash_flow.models.Meta;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.services.MetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metas")
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Meta criar(@Valid @RequestBody MetaRequest req,
                      @AuthenticationPrincipal Usuario usuario) {
        return metaService.criar(req, usuario);
    }

    @GetMapping
    public List<Meta> listar(@AuthenticationPrincipal Usuario usuario) {
        return metaService.listar(usuario.getId());
    }

    @GetMapping("/{id}/projecao")
    public ProjecaoMetaResponse projecao(@PathVariable Long id,
                                         @AuthenticationPrincipal Usuario usuario) {
        return metaService.projecao(id, usuario.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        metaService.deletar(id, usuario.getId());
    }
}
