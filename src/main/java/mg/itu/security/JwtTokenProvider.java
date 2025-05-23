package mg.itu.security;

import io.jsonwebtoken.*;
import mg.itu.model.Utilisateur;
import mg.itu.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import mg.itu.dto.LoginRequest;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    @Value("${app.refreshTokenExpirationInMs}")
    private int refreshTokenExpirationInMs;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";

    public String generateToken(String username, String role) {
        long now = System.currentTimeMillis();
        long expiryTime = now + jwtExpirationInMs;

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", username)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiryTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String refreshToken(String refreshToken, String username) {
        if (validateRefreshToken(refreshToken, username)) {
            Utilisateur user = utilisateurRepository.findByTelephone(username);
            if (user == null) {
                throw new IllegalArgumentException("Utilisateur non trouvé : " + username);
            }
            String role = user.getIdRole().getIdRole(); 
            String newJwt = generateToken(username, role);
            
            invalidateToken(refreshToken);
            
            String newRefreshToken = generateRefreshToken(username);
            storeRefreshToken(newRefreshToken, username);
            return newJwt;
        }
        throw new IllegalArgumentException("Refresh token invalide ou expiré");
    }

    public LoginRequest getUserDetailsFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Utilisateur u = utilisateurRepository.findByTelephone(claims.getSubject());
            return new LoginRequest(u.getTelephone(), u.getPassword(), claims.get("role", String.class));
        } catch (ExpiredJwtException ex) {
            Claims claims = ex.getClaims();
            Utilisateur u = utilisateurRepository.findByTelephone(claims.getSubject());
            return new LoginRequest(u.getTelephone(), u.getPassword(), claims.get("role", String.class));
        }
    }

    public String getUsernameFromJWT(String token) {
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.getSubject();
            } catch (ExpiredJwtException ex) {
                return ex.getClaims().getSubject();
            }
        }
        return null;
    }

    public Date getExpirationFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration();
        } catch (JwtException ex) {
            return new Date(0); 
        }
    }

    public boolean validateToken(String authToken) {
        try {
            if (isTokenBlacklisted(authToken)) {
                return false;
            }
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        long ttl = getExpirationFromJWT(token).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public String generateRefreshToken(String username) {
        long now = System.currentTimeMillis();
        long expiryTime = now + refreshTokenExpirationInMs;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date expiryDate = new Date(expiryTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public void storeRefreshToken(String refreshToken, String username) {
        long ttl = getExpirationFromJWT(refreshToken).getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + username, refreshToken, ttl, TimeUnit.MILLISECONDS);
    }

    public String getStoredRefreshToken(String username) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
    }

    public boolean validateRefreshToken(String refreshToken, String username) {
        String storedToken = getStoredRefreshToken(username);
        if (refreshToken == null || storedToken == null || !refreshToken.equals(storedToken)) {
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(refreshToken);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public void invalidateRefreshToken(String username) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + username);
    }
}