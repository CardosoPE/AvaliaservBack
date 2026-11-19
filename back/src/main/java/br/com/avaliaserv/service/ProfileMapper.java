package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.ProfessionalDtos.ProfileRequest;
import br.com.avaliaserv.model.ProfessionalProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileMapper {

    /** Aplica no perfil apenas os campos enviados (null = manter valor atual). */
    public void apply(ProfessionalProfile p, ProfileRequest r) {
        if (r == null) return;
        if (r.category() != null)        p.setCategory(r.category());
        if (r.jobTitle() != null)        p.setJobTitle(r.jobTitle());
        if (r.specialty() != null)       p.setSpecialty(r.specialty());
        if (r.experience() != null)      p.setExperience(r.experience());
        if (r.description() != null)     p.setDescription(r.description());
        if (r.whatsapp() != null)        p.setWhatsapp(r.whatsapp());
        if (r.whatsappRaw() != null)     p.setWhatsappRaw(r.whatsappRaw());
        if (r.whatsappCountry() != null) p.setWhatsappCountry(r.whatsappCountry());
        if (r.location() != null)        p.setLocation(r.location());
        if (r.serviceRadius() != null)   p.setServiceRadius(r.serviceRadius());
        if (r.availability() != null)    p.setAvailability(r.availability());
        if (r.serviceType() != null)     p.setServiceType(r.serviceType());
        if (r.transport() != null)       p.setTransport(r.transport());
        if (r.hourlyRate() != null)      p.setHourlyRate(r.hourlyRate());
        if (r.minValue() != null)        p.setMinValue(r.minValue());
        if (r.register() != null)        p.setRegister(r.register());
        if (r.insurance() != null)       p.setInsurance(r.insurance());
        if (r.instagram() != null)       p.setInstagram(r.instagram());
        if (r.website() != null)         p.setWebsite(r.website());
        if (r.status() != null)          p.setStatus(r.status());
        if (r.paymentMethods() != null)  p.setPaymentMethods(new ArrayList<>(r.paymentMethods()));
        if (r.languages() != null)       p.setLanguages(new ArrayList<>(r.languages()));
        if (r.formations() != null)      p.setFormations(new ArrayList<>(r.formations()));
        if (p.getPaymentMethods() == null) p.setPaymentMethods(new ArrayList<>());
        if (p.getLanguages() == null)      p.setLanguages(new ArrayList<>());
        if (p.getFormations() == null)     p.setFormations(new ArrayList<>());
    }

    public List<String> orEmpty(List<String> v) { return v == null ? new ArrayList<>() : v; }
}
