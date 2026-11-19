package br.com.avaliaserv.controller;

import br.com.avaliaserv.dto.RequestDtos.CreateRequest;
import br.com.avaliaserv.dto.RequestDtos.RequestResponse;
import br.com.avaliaserv.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestResponse> create(@Valid @RequestBody CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.create(req));
    }

    /** Solicitacoes do cliente logado. */
    @GetMapping("/mine")
    public List<RequestResponse> mine() {
        return requestService.mine();
    }

    /** Solicitacoes abertas, para o painel do profissional. */
    @GetMapping("/open")
    public List<RequestResponse> open(@RequestParam(required = false) String category) {
        return requestService.open(category);
    }

    @GetMapping("/{id}")
    public RequestResponse byId(@PathVariable Long id) {
        return requestService.byId(id);
    }

    @PatchMapping("/{id}/close")
    public RequestResponse close(@PathVariable Long id) {
        return requestService.close(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
