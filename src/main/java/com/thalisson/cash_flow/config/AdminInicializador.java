package com.thalisson.cash_flow.config;

import com.thalisson.cash_flow.models.Papel;
import com.thalisson.cash_flow.models.StatusUsuario;
import com.thalisson.cash_flow.models.Usuario;
import com.thalisson.cash_flow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInicializador implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:admin@cashflow.com}")
    private String adminEmail;

    @Value("${ADMIN_SENHA:admin123}")
    private String adminSenha;

    public AdminInicializador(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode(adminSenha));
            admin.setStatus(StatusUsuario.APROVADO);
            admin.setPapel(Papel.ROLE_ADMIN);
            userRepository.save(admin);
        }
    }
}
