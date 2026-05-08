package com.thalisson.cash_flow.repositories;

import com.thalisson.cash_flow.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository avisa ao Spring que essa interface é responsável pelo acesso aos dados.
@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    // Aqui acontece a mágica! Não precisamos escrever código para salvar ou buscar.
}