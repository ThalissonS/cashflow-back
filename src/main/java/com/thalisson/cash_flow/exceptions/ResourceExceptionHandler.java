package com.thalisson.cash_flow.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> manipuladorDeDadoIncorreto(HttpMessageNotReadableException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.BAD_REQUEST.value()); // HTTP 400
        erroPadrao.setError("Formato de Dado Inválido");
        erroPadrao.setMessage("Verifique se você preencheu os números e datas no formato correto.");
        erroPadrao.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadrao);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> manipuladorDeRegraDeNegocio(IllegalArgumentException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.BAD_REQUEST.value());
        erroPadrao.setError("Regra de Negócio Violada");
        erroPadrao.setMessage(e.getMessage());
        erroPadrao.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadrao);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<StandardError> manipuladorDeContaPendente(DisabledException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.FORBIDDEN.value());
        erroPadrao.setError("Conta Pendente");
        erroPadrao.setMessage("Sua conta ainda não foi aprovada por um administrador.");
        erroPadrao.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erroPadrao);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> manipuladorDeCredenciaisInvalidas(BadCredentialsException e, HttpServletRequest request) {

        StandardError erroPadrao = new StandardError();

        erroPadrao.setTimestamp(Instant.now());
        erroPadrao.setStatus(HttpStatus.UNAUTHORIZED.value());
        erroPadrao.setError("Credenciais Inválidas");
        erroPadrao.setMessage("E-mail ou senha incorretos.");
        erroPadrao.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erroPadrao);
    }

}