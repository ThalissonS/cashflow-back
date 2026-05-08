package com.thalisson.cash_flow.controllers;

import com.thalisson.cash_flow.models.Categoria;
import com.thalisson.cash_flow.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarTodos() { return categoriaRepository.findAll(); }

    @PostMapping
    public Categoria criar(@RequestBody Categoria categoria) {return categoriaRepository.save(categoria);}
}
