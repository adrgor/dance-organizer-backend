package org.example.user.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.user.model.User;
import org.example.user.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String generateActivationToken(UserDTO user) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(jwtToken);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Claim username = decodedJWT.getClaim("username");
        return username.asString();
    }

    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Claim username = decodedJWT.getClaim("email");
        return username.asString();
    }

    public int getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Claim userId = decodedJWT.getClaim("id");
        return userId.asInt();
    }

}
