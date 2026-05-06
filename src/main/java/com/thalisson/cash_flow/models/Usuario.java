package com.thalisson.cash_flow.models;

import jakarta.persistence.*;

// @Entity avisa ao Spring: "Essa classe não é uma classe comum, ela representa uma tabela no banco de dados!"
@Entity
// @Table permite dar um nome específico para a tabela. Se não colocarmos, ele usaria "Usuario".
@Table(name = "usuarios")
public class Usuario {

    // @Id avisa que essa variável é a Chave Primária (Primary Key) da tabela.
    @Id
    // @GeneratedValue avisa que não somos nós que vamos digitar o ID, o próprio banco vai gerar na ordem (1, 2, 3...) (Auto-Increment).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column configura a coluna no banco. nullable = false significa que é obrigatório (NOT NULL).
    @Column(nullable = false)
    private String nome;

    // unique = true garante que o banco de dados não vai deixar salvar dois usuários com o mesmo email.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    // Construtor vazio: O JPA (Hibernate) OBRIGA a ter um construtor vazio para conseguir montar o objeto depois que busca do banco.
    public Usuario() {
    }

    // A partir daqui, são apenas os Getters e Setters tradicionais.
    // Como a classe é privada, precisamos deles para acessar e modificar os valores.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}