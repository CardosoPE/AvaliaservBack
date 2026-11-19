package br.com.avaliaserv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class AuthDtos {

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password) {}

    public record SignupRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, message = "Senha minima de 6 caracteres") String password,
            @NotBlank String type,          // "client" | "professional"
            String phone,
            String city,
            String gender,
            String birth,
            String cpf,
            ProfessionalDtos.ProfileRequest profile) {}

    public record AuthResponse(String token, UserDtos.UserResponse user) {}

    public record ForgotPasswordRequest(@NotBlank @Email String email) {}

    /** Em producao o token vai por e-mail; aqui ele volta na resposta para facilitar testes. */
    public record ForgotPasswordResponse(String message, String devToken) {}

    public record VerifyTokenRequest(@NotBlank @Email String email, @NotBlank String token) {}

    public record ResetPasswordRequest(
            @NotBlank @Email String email,
            @NotBlank String token,
            @NotBlank @Size(min = 6, message = "Senha minima de 6 caracteres") String newPassword) {}

    public record MessageResponse(String message) {}

    public record ErrorResponse(String message, List<String> details) {}
}
