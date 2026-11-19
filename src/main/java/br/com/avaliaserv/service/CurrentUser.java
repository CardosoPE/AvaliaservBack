package br.com.avaliaserv.service;

import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUser {

    private final UserRepository userRepository;

    public String email() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw ApiException.unauthorized("Sessao expirada.");
        return auth.getName();
    }

    public User get() {
        return userRepository.findByEmailIgnoreCase(email())
                .orElseThrow(() -> ApiException.unauthorized("Usuario nao encontrado."));
    }
}
