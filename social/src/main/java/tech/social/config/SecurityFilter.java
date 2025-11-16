package tech.social.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.social.repositories.UserRepository;
import tech.social.service.TokenService;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UserRepository userRepository;
    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var login = tokenService.validateToken(token);
            if (!login.isEmpty()) {
                UserDetails user = userRepository.findByUsername(login);
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // Cria o objeto de autenticação
                    SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto de segurança (Salva o usuário autenticado no contexto da aplicação)
                }
            }
        }
        filterChain.doFilter(request,response); // Executa o próximo filtro na cadeia
    }

    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (token == null) {
            return null;
        }
        return token.replace("Bearer ", "");

    }
}
