package tech.social.dtos;

import jakarta.validation.constraints.NotBlank;

public record AlterRoleDTO (
    @NotBlank
    String username,
    @NotBlank
    String role
    ){}
