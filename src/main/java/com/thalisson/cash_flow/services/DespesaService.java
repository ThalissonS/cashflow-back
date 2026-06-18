package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.models.Despesa;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.DespesaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;

    public DespesaService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    public Despesa salvarDespesa(Despesa despesa, Usuario usuario) {
        if (despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da despesa deve ser maior que zero!");
        }
        despesa.setUsuario(usuario);
        return despesaRepository.save(despesa);
    }

    public List<Despesa> listarTodas(Long usuarioId) {
        return despesaRepository.findAllByUsuarioId(usuarioId);
    }

    public BigDecimal obterTotalGasto(Long usuarioId) {
        BigDecimal total = despesaRepository.calcularTotalDespesasByUsuarioId(usuarioId);
        return total == null ? BigDecimal.ZERO : total;
    }
}
