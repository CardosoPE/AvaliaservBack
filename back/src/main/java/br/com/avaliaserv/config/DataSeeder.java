package br.com.avaliaserv.config;

import br.com.avaliaserv.model.ProfessionalProfile;
import br.com.avaliaserv.model.Review;
import br.com.avaliaserv.repository.ProfessionalProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/** Carrega os profissionais de demonstracao na primeira execucao. */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProfessionalProfileRepository profileRepository;

    @Override
    public void run(String... args) {
        if (profileRepository.existsByDemoTrue()) return;

        profileRepository.saveAll(List.of(
                demo("Joao Silva", "Eletricista", "Manutencao", "Instalacoes Eletricas", "online",
                        "Sao Paulo, SP", "8 anos", 90.0, 200.0, "Tempo Integral", "50km",
                        "Especialista em instalacoes eletricas residenciais e comerciais.",
                        "5511999999999", "CREA 123456", "Sim", "Presencial",
                        List.of("PIX", "Dinheiro", "Cartao Credito"), List.of("Portugues"),
                        List.of("Curso Tecnico SENAI", "Especializacao em Instalacoes Residenciais"),
                        List.of(
                                review("Maria Santos", "Excelente trabalho, muito profissional.", 5, "2024-03-10"),
                                review("Pedro Alves", "Resolveu rapido. Recomendo!", 5, "2024-02-20"),
                                review("Ana Lima", "Bom servico, atrasou um pouco.", 4, "2024-01-15"))),

                demo("Ana Costa", "Encanadora", "Manutencao", "Reparos Hidraulicos", "online",
                        "Rio de Janeiro, RJ", "10 anos", 85.0, 180.0, "24/7", "30km",
                        "Especialista em emergencias e manutencao hidraulica. Atendimento 24h.",
                        "5521988888888", "CREA 654321", "Sim", "Presencial",
                        List.of("PIX", "Dinheiro"), List.of("Portugues", "Espanhol"),
                        List.of("Curso de Encanamento SENAI", "Treinamento em Emergencias"),
                        List.of(
                                review("Carlos Mendes", "Atendeu emergencia as 2h. Salvou meu apartamento.", 5, "2024-03-05"),
                                review("Fernanda Lima", "Muito atenciosa e eficiente.", 5, "2024-02-10"))),

                demo("Carlos Mendes", "Pintor", "Construcao", "Pintura Interna e Externa", "offline",
                        "Belo Horizonte, MG", "12 anos", 75.0, 400.0, "Horario Comercial", "20km",
                        "Pintura interna e externa com materiais de primeira linha.",
                        "5531977777777", "", "Em andamento", "Presencial",
                        List.of("PIX", "Dinheiro", "Transferencia"), List.of("Portugues"),
                        List.of("Curso de Pintura Decorativa", "Especializacao em Tintas Ecologicas"),
                        List.of(
                                review("Laura Ferreira", "Resultado impecavel. A casa ficou linda.", 5, "2024-01-30"),
                                review("Roberto Nunes", "Otimo trabalho, prazo demorou um pouco.", 4, "2023-12-15"))),

                demo("Beatriz Souza", "Dev Full Stack", "Tecnologia", "Desenvolvimento Web", "online",
                        "Sao Paulo, SP", "5 anos", 150.0, 1500.0, "Sob Demanda", "Remoto",
                        "Desenvolvedora full stack com foco em React, Node.js e solucoes cloud.",
                        "5511966666666", "", "Nao", "Online / Remoto",
                        List.of("PIX", "Transferencia", "Boleto"), List.of("Portugues", "Ingles", "Espanhol"),
                        List.of("Bacharelado CC - USP", "MBA em Gestao de TI"),
                        List.of(
                                review("Startup XYZ", "Entregou antes do prazo com qualidade excepcional.", 5, "2024-03-12"),
                                review("Empresa ABC", "Profissional incrivel, comunicacao perfeita.", 5, "2024-02-28"))),

                demo("Rafael Gomes", "Personal Trainer", "Saude", "Treinamento Fisico", "online",
                        "Rio de Janeiro, RJ", "7 anos", 120.0, 300.0, "Horario Comercial", "15km",
                        "Personal trainer especializado em emagrecimento e hipertrofia.",
                        "5521955555555", "CREF 012345-G/RJ", "Sim", "Presencial e Remoto",
                        List.of("PIX", "Cartao Credito", "Dinheiro"), List.of("Portugues", "Ingles"),
                        List.of("Educacao Fisica - UFRJ", "Especializacao em Nutricao Esportiva"),
                        List.of(
                                review("Juliana Carvalho", "Resultados acima do esperado em 4 meses.", 5, "2024-03-08"),
                                review("Marcelo Dias", "Treinos personalizados e motivador.", 5, "2024-01-20"))),

                demo("Camila Andrade", "Cabeleireira", "Beleza", "Corte e Coloracao", "offline",
                        "Belo Horizonte, MG", "9 anos", 80.0, 120.0, "Fins de Semana", "10km",
                        "Especialista em corte feminino, coloracao e tratamentos capilares.",
                        "5531944444444", "", "Nao", "Presencial",
                        List.of("PIX", "Dinheiro", "Cartao Debito"), List.of("Portugues"),
                        List.of("Curso Tecnico SENAC", "Especializacao em Colorimetria"),
                        List.of(
                                review("Lara Fonseca", "Ficou lindo o corte, muito caprichosa.", 5, "2024-02-14"),
                                review("Sonia Braga", "Coloracao perfeita. Super recomendo!", 5, "2024-01-05")))
        ));

        log.info("Profissionais de demonstracao carregados.");
    }

    private ProfessionalProfile demo(String name, String specialty, String category, String jobTitle,
                                     String status, String location, String experience, Double hourly,
                                     Double minValue, String availability, String radius, String description,
                                     String whatsapp, String register, String insurance, String serviceType,
                                     List<String> payments, List<String> languages, List<String> formations,
                                     List<Review> reviews) {

        ProfessionalProfile profile = ProfessionalProfile.builder()
                .demo(true).demoName(name)
                .specialty(specialty).category(category).jobTitle(jobTitle).status(status)
                .location(location).experience(experience).hourlyRate(hourly).minValue(minValue)
                .availability(availability).serviceRadius(radius).description(description)
                .whatsapp(whatsapp).register(register).insurance(insurance).serviceType(serviceType)
                .paymentMethods(new java.util.ArrayList<>(payments))
                .languages(new java.util.ArrayList<>(languages))
                .formations(new java.util.ArrayList<>(formations))
                .reviews(new java.util.ArrayList<>())
                .build();

        reviews.forEach(r -> { r.setProfile(profile); profile.getReviews().add(r); });
        return profile;
    }

    private Review review(String author, String text, int rating, String date) {
        return Review.builder().authorName(author).text(text).rating(rating)
                .date(LocalDate.parse(date)).build();
    }
}
