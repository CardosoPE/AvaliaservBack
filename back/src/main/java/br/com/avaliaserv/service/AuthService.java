package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.AuthDtos.*;
import br.com.avaliaserv.dto.UserDtos.UserResponse;
import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.*;
import br.com.avaliaserv.repository.*;
import br.com.avaliaserv.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfessionalProfileRepository profileRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfileMapper profileMapper;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public AuthResponse signup(SignupRequest req) {
        String email = req.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw ApiException.badRequest("Este e-mail ja esta cadastrado.");
        }

        UserType type = "professional".equalsIgnoreCase(req.type()) ? UserType.PROFESSIONAL : UserType.CLIENT;

        User user = User.builder()
                .name(req.name().trim())
                .email(email)
                .password(passwordEncoder.encode(req.password()))
                .type(type)
                .phone(req.phone())
                .city(req.city())
                .gender(req.gender())
                .cpf(req.cpf())
                .createdAt(Instant.now())
                .build();

        if (req.birth() != null && !req.birth().isBlank()) {
            try { user.setBirth(LocalDate.parse(req.birth())); } catch (Exception ignored) { }
        }

        user = userRepository.save(user);

        if (type == UserType.PROFESSIONAL) {
            ProfessionalProfile profile = ProfessionalProfile.builder().user(user).build();
            profileMapper.apply(profile, req.profile());
            profile = profileRepository.save(profile);
            user.setProfile(profile);
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail(), user.getType().name()),
                UserResponse.from(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmailIgnoreCase(req.email().trim())
                .orElseThrow(() -> ApiException.unauthorized("E-mail ou senha incorretos."));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw ApiException.unauthorized("E-mail ou senha incorretos.");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail(), user.getType().name()),
                UserResponse.from(user));
    }

    @Transactional
    public ForgotPasswordResponse requestReset(ForgotPasswordRequest req) {
        String email = req.email().trim().toLowerCase();
        userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> ApiException.notFound("E-mail nao encontrado."));

        String code = String.valueOf(100000 + RANDOM.nextInt(900000));
        tokenRepository.save(PasswordResetToken.builder()
                .email(email)
                .token(code)
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .build());

        // TODO: integrar envio de e-mail (spring-boot-starter-mail). Por enquanto o codigo volta na resposta.
        return new ForgotPasswordResponse("Codigo de verificacao gerado. Valido por 15 minutos.", code);
    }

    @Transactional(readOnly = true)
    public MessageResponse verifyToken(VerifyTokenRequest req) {
        findValidToken(req.email(), req.token());
        return new MessageResponse("Codigo valido.");
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest req) {
        PasswordResetToken token = findValidToken(req.email(), req.token());
        User user = userRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> ApiException.notFound("Usuario nao encontrado."));

        user.setPassword(passwordEncoder.encode(req.newPassword()));
        token.setUsed(true);
        return new MessageResponse("Senha alterada com sucesso.");
    }

    private PasswordResetToken findValidToken(String email, String code) {
        PasswordResetToken token = tokenRepository
                .findFirstByEmailAndUsedFalseOrderByIdDesc(email.trim().toLowerCase())
                .orElseThrow(() -> ApiException.badRequest("Codigo invalido."));

        if (!token.getToken().equals(code.trim())) throw ApiException.badRequest("Codigo invalido.");
        if (token.getExpiresAt().isBefore(Instant.now())) throw ApiException.badRequest("Codigo expirado. Peca um novo.");
        return token;
    }
}
