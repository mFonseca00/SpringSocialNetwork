package tech.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import tech.social.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);
}
