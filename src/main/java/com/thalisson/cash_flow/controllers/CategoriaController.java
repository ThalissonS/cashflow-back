package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.models.Categoria;
import com.thalisson.cash_flow.repositories.CategoriaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<Categoria> listarTodos() { return categoriaRepository.findAll(); }

    @PostMapping
    public Categoria criar(@RequestBody Categoria categoria) {return categoriaRepository.save(categoria);}
}
