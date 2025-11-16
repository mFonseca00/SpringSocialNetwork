package tech.social.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.social.entities.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) { // Gera um token JWT para o usuário
        try{
            Algorithm alg = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Social API")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .sign(alg);
            return token;
        } catch (JWTCreationException e){
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public String validateToken(String tokenJWT) { // Retorna o username se o token for válido
        try {
            Algorithm alg = Algorithm.HMAC256(secret);
            return JWT.require(alg)
                    .withIssuer("Social API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (Exception e) {
            return ""; // Retorna vazio se o token for inválido ou expirado
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
