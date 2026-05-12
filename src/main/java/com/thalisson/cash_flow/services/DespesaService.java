package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.models.Despesa;
import com.thalisson.cash_flow.repositories.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service // Isso avisa ao Spring: "Eu sou o Cérebro/Gerente de Despesas"
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository; // Chamamos o arquivista

    // Nosso método principal
    public Despesa salvarDespesa(Despesa despesa) {

        // REGRA DE NEGÓCIO 1: O valor não pode ser menor ou igual a zero
        if (despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da despesa deve ser maior que zero!");
        }

        // Se passou pelo if acima sem estourar o erro, mandamos salvar no banco!
        return despesaRepository.save(despesa);
    }

    public List<Despesa> listarTodas() {
        return despesaRepository.findAll();
    }


    public BigDecimal obterTotalGasto() {
        BigDecimal total = despesaRepository.calcularTotalDespesas();

        // Se vier nulo do banco (não tem despesas), devolvemos ZERO.
        if (total == null) {
            return BigDecimal.ZERO;
        }

        return total;
    }

    
}