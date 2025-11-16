package tech.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.social.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
