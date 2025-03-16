package fr.cda.cdafinalprojectbackend.configuration.security;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {
    // TODO Placer l'encryption key dans le Vault
    private static final String ENCRYPTION_KEY =  "5c275094bb03404a10e5a0ed86f2046e93a03cd138a813ed7379243e9e87697d";
    private UserServiceImpl userService;

    public Map<String, String> getJwtToken(String username) {
        DBUser dbUser = this.userService.loadUserByUsername(username);
        return this.generateJwt(dbUser);
    }

    private Map<String, String> generateJwt(DBUser dbUser) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        Map<String, Object> claims = Map.of(
                "nickname", dbUser.getNickname(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, dbUser.getEmail()
        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(dbUser.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return Map.of(
                "bearer", bearer
        );
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public String extractUsername(String token) {
        Claims claims = this.getAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        log.info(token + " expiration date: " + expirationDate);
        if (expirationDate == null) {
            // Logique de gestion si la date d'expiration est nulle
            throw new IllegalArgumentException("Le jeton ne contient pas de date d'expiration.");
        }
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        Claims claims = this.getAllClaims(token);
        return claims.getExpiration();
    }

//    private <T> T getClaim(String token, Function<Claims, T> function) {
//        Claims claims = getAllClaims(token);
//        return function.apply(claims);
//    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
