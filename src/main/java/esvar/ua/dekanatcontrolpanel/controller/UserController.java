package esvar.ua.dekanatcontrolpanel.controller;

import esvar.ua.dekanatcontrolpanel.dto.UserCreateUpdateDto;
import esvar.ua.dekanatcontrolpanel.dto.UserPasswordChangeDto;
import esvar.ua.dekanatcontrolpanel.dto.UserResponseDto;
import esvar.ua.dekanatcontrolpanel.service.UserManagementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public List<UserResponseDto> listUsers() {
        return userManagementService.listUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateUpdateDto dto) {
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must not be blank");
        }

        try {
            UserResponseDto created = userManagementService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateUpdateDto dto) {
        try {
            return userManagementService.updateUser(id, dto);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordChangeDto dto) {
        try {
            userManagementService.changePassword(id, dto.getNewPassword());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}