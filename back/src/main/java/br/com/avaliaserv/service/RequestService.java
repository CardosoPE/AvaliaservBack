package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.RequestDtos.CreateRequest;
import br.com.avaliaserv.dto.RequestDtos.RequestResponse;
import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.RequestStatus;
import br.com.avaliaserv.model.ServiceRequest;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ServiceRequestRepository requestRepository;
    private final CurrentUser currentUser;

    @Transactional
    public RequestResponse create(CreateRequest req) {
        User client = currentUser.get();
        ServiceRequest request = ServiceRequest.builder()
                .client(client)
                .title(req.title().trim())
                .description(req.description().trim())
                .category(req.category())
                .budget(req.budget())
                .status(RequestStatus.OPEN)
                .build();
        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<RequestResponse> mine() {
        return requestRepository.findByClientIdOrderByCreatedAtDesc(currentUser.get().getId())
                .stream().map(RequestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<RequestResponse> open(String category) {
        var list = (category == null || category.isBlank())
                ? requestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.OPEN)
                : requestRepository.findByStatusAndCategoryOrderByCreatedAtDesc(RequestStatus.OPEN, category);
        return list.stream().map(RequestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RequestResponse byId(Long id) {
        return RequestResponse.from(find(id));
    }

    @Transactional
    public RequestResponse close(Long id) {
        ServiceRequest request = ownedByCurrentUser(id);
        request.setStatus(RequestStatus.CLOSED);
        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional
    public void delete(Long id) {
        requestRepository.delete(ownedByCurrentUser(id));
    }

    private ServiceRequest find(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Solicitacao nao encontrada."));
    }

    private ServiceRequest ownedByCurrentUser(Long id) {
        ServiceRequest request = find(id);
        if (!request.getClient().getEmail().equalsIgnoreCase(currentUser.email())) {
            throw ApiException.forbidden("Esta solicitacao pertence a outro usuario.");
        }
        return request;
    }
}
