package com.thalisson.cash_flow.exceptions;

import java.time.Instant;

// Essa classe é apenas um molde (DTO) para formatarmos a resposta de erro em JSON
public class StandardError {

    private Instant timestamp; // A data e hora exata do erro
    private Integer status;    // O código HTTP (ex: 400, 404, 500)
    private String error;      // O nome do erro
    private String message;    // A mensagem amigável para o usuário
    private String path;       // Qual URL o usuário tentou acessar

    public StandardError() {}

    // Getters e Setters
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}