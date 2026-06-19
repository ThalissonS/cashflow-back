package com.thalisson.cash_flow.services;

import com.thalisson.cash_flow.dtos.BancoDTO;
import com.thalisson.cash_flow.dtos.OfertaDTO;
import com.thalisson.cash_flow.dtos.SimulacaoRequest;
import com.thalisson.cash_flow.dtos.SimulacaoResponse;
import com.thalisson.cash_flow.models.TipoInvestimento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvestimentoService {

    private final BancoCentralService bancoCentralService;

    // Dados estaticos de bancos e ofertas (sem banco de dados)
    private static final List<BancoDTO> BANCOS = List.of(
            new BancoDTO(1L, "Nubank"),
            new BancoDTO(2L, "XP Investimentos"),
            new BancoDTO(3L, "Inter"),
            new BancoDTO(4L, "C6 Bank"),
            new BancoDTO(5L, "Banco do Brasil")
    );

    private static final List<OfertaDTO> OFERTAS = List.of(
            // CDB
            new OfertaDTO(1L, "Nubank",           TipoInvestimento.CDB, bd(100), 1),
            new OfertaDTO(2L, "XP Investimentos", TipoInvestimento.CDB, bd(115), 3),
            new OfertaDTO(3L, "Inter",             TipoInvestimento.CDB, bd(100), 1),
            new OfertaDTO(4L, "C6 Bank",           TipoInvestimento.CDB, bd(103), 1),
            new OfertaDTO(5L, "Banco do Brasil",   TipoInvestimento.CDB, bd(95),  1),
            // LCI
            new OfertaDTO(6L,  "XP Investimentos", TipoInvestimento.LCI, bd(95),  3),
            new OfertaDTO(7L,  "Inter",             TipoInvestimento.LCI, bd(90),  3),
            new OfertaDTO(8L,  "Banco do Brasil",   TipoInvestimento.LCI, bd(88),  3),
            // LCA
            new OfertaDTO(9L,  "XP Investimentos", TipoInvestimento.LCA, bd(93),  3),
            new OfertaDTO(10L, "Banco do Brasil",   TipoInvestimento.LCA, bd(86),  3),
            // Poupanca (70% CDI por regra geral — Selic > 8,5%)
            new OfertaDTO(11L, "Todos os bancos",   TipoInvestimento.POUPANCA, bd(70), 1)
    );

    public InvestimentoService(BancoCentralService bancoCentralService) {
        this.bancoCentralService = bancoCentralService;
    }

    public List<TipoInvestimento> tipos() {
        return List.of(TipoInvestimento.values());
    }

    public List<OfertaDTO> ofertas(TipoInvestimento tipo) {
        return OFERTAS.stream()
                .filter(o -> o.tipo() == tipo)
                .toList();
    }

    public List<BancoDTO> bancos() {
        return BANCOS;
    }

    public SimulacaoResponse simular(SimulacaoRequest req) {
        BigDecimal cdiAnual = bancoCentralService.getCdiAnual();

        // Percentual do CDI: poupanca fixa em 70%; demais usam o fornecido ou 100%
        BigDecimal pctCdi;
        if (req.tipo() == TipoInvestimento.POUPANCA) {
            pctCdi = bd(70);
        } else if (req.bancoId() != null) {
            pctCdi = OFERTAS.stream()
                    .filter(o -> o.tipo() == req.tipo() && o.id().equals(req.bancoId()))
                    .map(OfertaDTO::percentualCdi)
                    .findFirst()
                    .orElse(req.percentualCdi() != null ? req.percentualCdi() : bd(100));
        } else {
            pctCdi = req.percentualCdi() != null ? req.percentualCdi() : bd(100);
        }

        // taxa anual efetiva = CDI * percentualCdi / 100
        double taxaAnualEfetiva = cdiAnual.doubleValue() * pctCdi.doubleValue() / 10000.0;

        // taxa mensal via juros compostos: (1 + anual)^(1/12) - 1
        double taxaMensal = Math.pow(1 + taxaAnualEfetiva, 1.0 / 12) - 1;

        double valorInicial = req.valorInicial().doubleValue();
        double aporte = req.aporteMensal() != null ? req.aporteMensal().doubleValue() : 0.0;
        int n = req.prazoMeses();

        // FV = valorInicial * (1+i)^n + aporte * ((1+i)^n - 1) / i
        double fator = Math.pow(1 + taxaMensal, n);
        double valorBruto = taxaMensal == 0
                ? valorInicial + aporte * n
                : valorInicial * fator + aporte * (fator - 1) / taxaMensal;

        double totalInvestido = valorInicial + aporte * n;
        double rendimentoBruto = valorBruto - totalInvestido;

        // IR regressivo — somente CDB; LCI/LCA/Poupanca sao isentos
        double aliquotaIR = 0.0;
        if (req.tipo() == TipoInvestimento.CDB) {
            int dias = n * 30;
            if (dias <= 180)      aliquotaIR = 22.5;
            else if (dias <= 360) aliquotaIR = 20.0;
            else if (dias <= 720) aliquotaIR = 17.5;
            else                  aliquotaIR = 15.0;
        }

        double impostoRenda = rendimentoBruto * aliquotaIR / 100.0;
        double valorLiquido = valorBruto - impostoRenda;
        double rendimentoLiquido = valorLiquido - totalInvestido;

        return new SimulacaoResponse(
                req.tipo(),
                r(totalInvestido),
                r(valorBruto),
                r(rendimentoBruto),
                r(impostoRenda),
                r(valorLiquido),
                r(rendimentoLiquido),
                r(aliquotaIR),
                r(taxaMensal * 100),
                cdiAnual
        );
    }

    private static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v);
    }

    private static BigDecimal r(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }
}
