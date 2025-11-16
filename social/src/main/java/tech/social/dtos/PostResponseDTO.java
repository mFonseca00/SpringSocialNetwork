package tech.social.dtos;

import java.time.LocalDateTime;

import tech.social.entities.Post;

public record PostResponseDTO(
    Long id,
    String content,
    String authorUsername,
    Long authorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public PostResponseDTO(Post post) {
        this(
            post.getId(),
            post.getContent(),
            post.getAuthor().getUsername(),
            post.getAuthor().getId(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}