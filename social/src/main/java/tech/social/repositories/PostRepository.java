package tech.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.social.entities.Post;
import tech.social.entities.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthor(User author, Pageable pageable);
    Page<Post> findByAuthor_Username(String username, Pageable pageable);
}