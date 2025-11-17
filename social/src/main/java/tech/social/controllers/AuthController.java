package tech.social.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.social.dtos.AlterRoleDTO;
import tech.social.dtos.AuthResponseDTO;
import tech.social.dtos.AuthenticationDTO;
import tech.social.entities.User;
import tech.social.entities.enums.UserRole;
import tech.social.repositories.UserRepository;
import tech.social.service.TokenService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de usuários")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository repository;
    private TokenService tokenService;
    public AuthController(AuthenticationManager authenticationManager, UserRepository repository, TokenService tokenService) {
        this.tokenService = tokenService;
        this.repository = repository;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthenticationDTO authDTO) {
        var usernamepassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        var auth = this.authenticationManager.authenticate(usernamepassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário com role USER")
    public ResponseEntity register(@RequestBody @Valid AuthenticationDTO authDTO) {
        if(this.repository.findByUsername(authDTO.username()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(authDTO.password());
        User newUser = new User(authDTO.username(), encodedPassword, UserRole.USER);
        this.repository.save(newUser);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/alterrole")
    @Operation(summary = "Alterar a role de um usuário", description = "Requer permissão de ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity promote(@RequestBody @Valid AlterRoleDTO alterRoleDTO) {
        User user = (User) this.repository.findByUsername(alterRoleDTO.username());
        if(user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        if(user.getRole() == UserRole.valueOf(alterRoleDTO.role())) {
            return ResponseEntity.badRequest().body("User already has the role " + alterRoleDTO.role());
        }
        user.setRole(UserRole.valueOf(alterRoleDTO.role()));
        this.repository.save(user);
        return ResponseEntity.ok().build();
    }

}
