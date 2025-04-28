package com.ecommerce.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expiration = 86400000;
    
    public JwtUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    
	public String generateToken(String email, String role, String userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }
	
	public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }	

    public String extractUserId(String token) {
        return extractClaims(token).get("id", String.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("roles", String.class);
    }
   
	public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                System.out.println("Token is expired!");
                return false;
            }
            System.out.println("true validation in jwutil");
            return true;
        } 
        catch (ExpiredJwtException e) {
            System.out.println("Token is expired: " + e.getMessage());
            return false;
        }
        catch (JwtException | IllegalArgumentException e) {
            System.out.println("false validation in jwutil");
            return false;
        }
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println("Extracted Token from Header: " + bearerToken);
            return bearerToken.substring(7);
        }
        
        System.out.println("No Authorization token found in header.");
        return null;
    }

	
}
