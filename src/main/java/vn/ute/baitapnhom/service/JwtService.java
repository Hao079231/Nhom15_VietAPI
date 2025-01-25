package vn.ute.baitapnhom.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private Long jwtExpiration;

  // Extract username from token
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Extract specific claim from token
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claim = extractAllClaims(token);
    return claimsResolver.apply(claim);
  }

  // Generate token without extra claims
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  // Generate token with extra claims
  public String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  // Get token expiration time
  public long getExpiration() {
    return jwtExpiration;
  }

  // Build token with claims and expiration
  public String buildToken(Map<String, Objects> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey(), SignatureAlgorithm.ES256)
        .compact();
  }

  // Validate token against user details
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  // Check if token is expired
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Extract token expiration date
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // Extract all claims from token
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Get signing key from secret key
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes); // Using HMAC key
  }
}
