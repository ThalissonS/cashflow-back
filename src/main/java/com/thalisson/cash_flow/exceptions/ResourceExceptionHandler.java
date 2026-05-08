package com.thalisson.cash_flow.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.Instant;

// @RestControllerAdvice avisa ao Spring: "Fique monitorando todos os Controllers. Se der erro, jogue para cá!"
@RestControllerAdvice
public class ResourceExceptionHandler {

    // @ExceptionHandler avisa qual tipo de erro este método vai tratar.
    // O erro "DataIntegrityViolationException" é o erro padrão que o banco joga quando ferimos uma regra (como o unique do email)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> manipuladorDeEmailDuplicado(DataIntegrityViolationException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.BAD_REQUEST.value()); // HTTP 400 - Bad Request
        erroPadrao.setError("Violação de Integridade de Dados");
        erroPadrao.setMessage("Este e-mail já está cadastrado no sistema."); // Nossa mensagem amigável!
        erroPadrao.setPath(request.getRequestURI());

        // Devolvemos o JSON montado com o status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadrao);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<StandardError> manipuladorDeAtualizacaoInvalida(ObjectOptimisticLockingFailureException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.BAD_REQUEST.value()); // HTTP 400
        erroPadrao.setError("Erro de Atualização");
        erroPadrao.setMessage("Você tentou atualizar um registro enviando um ID que não existe no sistema.");
        erroPadrao.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadrao);
    }
}