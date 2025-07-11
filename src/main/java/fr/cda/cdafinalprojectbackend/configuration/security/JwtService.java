package fr.cda.cdafinalprojectbackend.configuration.security;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Jwt;
import fr.cda.cdafinalprojectbackend.repository.JwtRepository;
import fr.cda.cdafinalprojectbackend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {
    // TODO Placer l'encryption key dans le Vault
    private static final String ENCRYPTION_KEY =  "5c275094bb03404a10e5a0ed86f2046e93a03cd138a813ed7379243e9e87697d";
    public static final String BEARER = "bearer";
    private UserService userService;
    private JwtRepository jwtRepository;

    @Transactional
    public Map<String, String> getJwtToken(String username) {
        DBUser dbUser = this.userService.loadUserByUsername(username);
        this.disableTokens(dbUser);
        final Map<String, String> jwtMap = this.generateJwt(dbUser);
        final Jwt jwt = Jwt.builder()
                .value(jwtMap.get(BEARER))
                .isActive(true)
                .expirate(false)
                .user(dbUser)
                .build();
        this.jwtRepository.save(jwt);
        return jwtMap;
    }

    private Map<String, String> generateJwt(DBUser dbUser) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        Map<String, Object> claims = Map.of(
                "email", dbUser.getEmail(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, dbUser.getId().toString()
        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(dbUser.getId().toString())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return Map.of(
                BEARER, bearer
        );
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    @Transactional
    protected void disableTokens(DBUser user) {
        final List<Jwt> jwtList = this.jwtRepository.findTokenByUser(user.getEmail()).peek(
                jwt -> {
                    jwt.setActive(false);
                    jwt.setExpirate(true);
                }
        ).toList();

        this.jwtRepository.saveAll(jwtList);
    }


    public String extractId(String token) {
        Claims claims = this.getAllClaims(token);
        return claims.getSubject();
    }

    public String extractEmail(String token) {
        Claims claims = this.getAllClaims(token);
        return claims.get("email", String.class);
    }

    public DBUser getCurrentUser() {
        return (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Jwt getTokenByValue(String value) {
        return this.jwtRepository.findByValue(value).orElseThrow(
                () -> new RuntimeException("Token not found")
        );
    }

    public void logout() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findValidTokenByUser(
                user.getEmail(),
                true,
                false
        ).orElseThrow(() -> new RuntimeException("Invalid token"));
        jwt.setActive(false);
        jwt.setExpirate(true);
        this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "@daily")
    public void removeUselessJwt() {
        log.info("Remove useless jwt at {}", Instant.now());
        this.jwtRepository.deleteAllByIsActiveAndExpirate(false, true);
    }
}
