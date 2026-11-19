package br.com.avaliaserv.repository;

import br.com.avaliaserv.model.RequestStatus;
import br.com.avaliaserv.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByClientIdOrderByCreatedAtDesc(Long clientId);
    List<ServiceRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<ServiceRequest> findByStatusAndCategoryOrderByCreatedAtDesc(RequestStatus status, String category);
}
