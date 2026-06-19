package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.dtos.LancamentoRequest;
import com.thalisson.cash_flow.models.Categoria;
import com.thalisson.cash_flow.models.Lancamento;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.CategoriaRepository;
import com.thalisson.cash_flow.repositories.LancamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository,
                             CategoriaRepository categoriaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Lancamento salvar(LancamentoRequest req, Usuario usuario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(req.descricao());
        lancamento.setValor(req.valor());
        lancamento.setTipo(req.tipo());
        lancamento.setAno(req.ano());
        lancamento.setMes(req.mes());
        lancamento.setUsuario(usuario);

        if (req.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(req.categoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria nao encontrada"));
            lancamento.setCategoria(categoria);
        }

        return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> listarTodos(Long usuarioId) {
        return lancamentoRepository.findAllByUsuarioId(usuarioId);
    }

    public List<Lancamento> listarPorMes(Long usuarioId, int ano, int mes) {
        return lancamentoRepository.findAllByUsuarioIdAndAnoAndMes(usuarioId, ano, mes);
    }

    public void deletar(Long id, Long usuarioId) {
        Lancamento lancamento = lancamentoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lancamento nao encontrado"));
        lancamentoRepository.delete(lancamento);
    }
}
