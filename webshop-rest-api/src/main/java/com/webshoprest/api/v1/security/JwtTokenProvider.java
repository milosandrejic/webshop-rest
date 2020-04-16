package com.webshoprest.api.v1.security;

import com.webshoprest.domain.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.webshoprest.api.v1.security.SecurityConstants.SECRET;
import static com.webshoprest.api.v1.security.SecurityConstants.TOKEN_EXPIRATION;

@Slf4j
@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRATION);

        String userId = String.valueOf(user.getUserId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        log.info("UserId: " + userId + " inserted into claims");

        claims.put("username", user.getUsername());
        log.info("Username: " + user.getUsername() + " inserted into claims");

        claims.put("role", user.getRole().getRole().name());
        log.info("User role: " + user.getRole().getRole().name() + " inserted into claims");

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            log.error("Invalid Jwt token signature.");
        } catch (MalformedJwtException ex){
            log.error("Invalid token.");
        } catch (ExpiredJwtException ex){
            log.error("Token has expired.");
        } catch (UnsupportedJwtException ex){
            log.error("Unsupported Jwt token");
        } catch (IllegalArgumentException ex){
            log.error("Claims are empty.");
        }
        return false;
    }

    public Long extractUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        log.info(getClass() + ": extracted user id - " + claims.get("id") + " from token");
        return Long.parseLong(claims.get("id").toString());
    }

}
