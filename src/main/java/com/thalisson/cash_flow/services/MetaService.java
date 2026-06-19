package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.dtos.MetaRequest;
import com.thalisson.cash_flow.dtos.ProjecaoMetaResponse;
import com.thalisson.cash_flow.models.Meta;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.MetaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
public class MetaService {

    private final MetaRepository metaRepository;
    private final BancoCentralService bancoCentralService;

    public MetaService(MetaRepository metaRepository, BancoCentralService bancoCentralService) {
        this.metaRepository = metaRepository;
        this.bancoCentralService = bancoCentralService;
    }

    public Meta criar(MetaRequest req, Usuario usuario) {
        Meta meta = new Meta();
        meta.setNome(req.nome());
        meta.setValorAlvo(req.valorAlvo());
        meta.setAporteMensal(req.aporteMensal());
        meta.setValorInicial(req.valorInicial() != null ? req.valorInicial() : BigDecimal.ZERO);
        meta.setUsuario(usuario);
        return metaRepository.save(meta);
    }

    public List<Meta> listar(Long usuarioId) {
        return metaRepository.findAllByUsuarioId(usuarioId);
    }

    public void deletar(Long id, Long usuarioId) {
        Meta meta = metaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta nao encontrada"));
        metaRepository.delete(meta);
    }

    public ProjecaoMetaResponse projecao(Long id, Long usuarioId) {
        Meta meta = metaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta nao encontrada"));

        BigDecimal cdiAnual = bancoCentralService.getCdiAnual();

        // taxa mensal via juros compostos: (1 + cdiAnual/100)^(1/12) - 1
        double taxaAnual = cdiAnual.doubleValue() / 100.0;
        double taxaMensal = Math.pow(1 + taxaAnual, 1.0 / 12) - 1;

        double valorAlvo = meta.getValorAlvo().doubleValue();
        double saldo = meta.getValorInicial() != null ? meta.getValorInicial().doubleValue() : 0.0;
        double aporte = meta.getAporteMensal().doubleValue();

        if (saldo >= valorAlvo) {
            return new ProjecaoMetaResponse(meta.getNome(), meta.getValorAlvo(),
                    meta.getAporteMensal(), meta.getValorInicial(), 0, cdiAnual, "Ja atingida");
        }

        if (aporte <= 0 && taxaMensal == 0) {
            return new ProjecaoMetaResponse(meta.getNome(), meta.getValorAlvo(),
                    meta.getAporteMensal(), meta.getValorInicial(), -1, cdiAnual, "Inalcancavel sem aporte");
        }

        int meses = 0;
        // limite de 600 meses (50 anos) para evitar loop infinito
        while (saldo < valorAlvo && meses < 600) {
            saldo = saldo * (1 + taxaMensal) + aporte;
            meses++;
        }

        String dataProjetada = meses < 600
                ? YearMonth.now().plusMonths(meses).toString()
                : "Mais de 50 anos";

        return new ProjecaoMetaResponse(meta.getNome(), meta.getValorAlvo(),
                meta.getAporteMensal(), meta.getValorInicial(), meses, cdiAnual, dataProjetada);
    }
}
