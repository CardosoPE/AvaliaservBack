package br.com.avaliaserv.controller;

import br.com.avaliaserv.dto.UserDtos.UpdateUserRequest;
import br.com.avaliaserv.dto.UserDtos.UserResponse;
import br.com.avaliaserv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }

    @PutMapping("/me")
    public UserResponse update(@RequestBody UpdateUserRequest req) {
        return userService.update(req);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete() {
        userService.deleteAccount();
        return ResponseEntity.noContent().build();
    }
}
