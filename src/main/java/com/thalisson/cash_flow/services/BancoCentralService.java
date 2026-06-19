package com.thalisson.cash_flow.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class BancoCentralService {

    // CDI/Selic anualizado base 252 (serie SGS 4391 do Banco Central)
    private static final String BCB_URL =
            "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4391/dados/ultimos/1?formato=json";

    private static final BigDecimal CDI_FALLBACK = BigDecimal.valueOf(10.75);

    private final RestTemplate restTemplate;

    public BancoCentralService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getCdiAnual() {
        try {
            List<?> resposta = restTemplate.getForObject(BCB_URL, List.class);
            if (resposta != null && !resposta.isEmpty()) {
                Map<?, ?> item = (Map<?, ?>) resposta.get(0);
                return new BigDecimal(item.get("valor").toString());
            }
        } catch (Exception e) {
            // API indisponivel — usa valor de fallback
        }
        return CDI_FALLBACK;
    }
}
