package tech.social.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 500, message = "Content must be less than 500 characters")
    String content
) {}