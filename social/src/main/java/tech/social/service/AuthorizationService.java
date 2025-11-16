package tech.social.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.social.repositories.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {
    UserRepository userRepository;
    public AuthorizationService(UserRepository userRepository) { this.userRepository = userRepository;}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
