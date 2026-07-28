package com.ms.security.jwt;

import static com.ms.security.config.Constants.AUTHORITIES_KEY;

import com.ms.security.service.SecurityMetersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

  private SecretKey key;

  private long tokenValidityInMilliseconds;

  private final SecurityMetersService securityMetersService;
  private final JwtProperties jwtProperties;

  /** initializes the token provider based on the yml config file */
  @PostConstruct
  public void init() {
    log.info("JwtProperties : {}", jwtProperties);
    byte[] keyBytes;
    String secret = jwtProperties.getSecret();
    if (StringUtils.hasLength(secret)) {
      log.warn(
          "Warning: the JWT key used is not Base64-encoded. We recommend using the `jwtProperties.jwt.base64-secret` key for optimum security.");
      keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    } else {
      log.debug("Using a Base64-encoded JWT secret key");
      keyBytes = Decoders.BASE64.decode(jwtProperties.getBase64Secret());
    }
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.tokenValidityInMilliseconds = jwtProperties.getTokenValidityInMilliSeconds();
  }

  /**
   * Create JWT Token a fully populated <code>Authentication</code> object.
   *
   * @param authentication Authentication Object
   * @return JWT Token
   */
  @NonNull
  public String createToken(Authentication authentication) {
    return createToken(authentication, tokenValidityInMilliseconds);
  }

  /**
   * Create JWT Token a fully populated <code>Authentication</code> object.
   *
   * @param authentication Authentication Object
   * @param duration the Token lifetime in milliseconds
   * @return JWT Token
   */
  @NonNull
  public String createToken(Authentication authentication, long duration) {
    long validity = System.currentTimeMillis() + duration;
    return createToken(authentication, null, new Date(validity));
  }

  /**
   * Create JWT Token a fully populated {@link Authentication} object.
   *
   * @param authentication Authentication Object
   * @param issuedAt Date when the token was issued, if null set to now
   * @param expiration Date when the token expires
   * @return JWT Token
   */
  @NonNull
  public String createToken(
      Authentication authentication, @Nullable Date issuedAt, Date expiration) {
    String authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    JwtBuilder jwtBuilder =
        Jwts.builder()
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .issuedAt(issuedAt != null ? issuedAt : new Date());

    return jwtBuilder.signWith(key, Jwts.SIG.HS512).expiration(expiration).compact();
  }

  /**
   * Convert JWT Authorization Token into UsernamePasswordAuthenticationToken, including a USer
   * object and its authorities
   *
   * @param token JWT Authorization Token
   * @return UsernamePasswordAuthenticationToken with principal, token and authorities that were
   *     stored in the token or null if no authorities were found
   */
  @Nullable
  public Authentication getAuthentication(String token) {
    Claims claims = parseClaims(token);
    var authorityClaim = claims.get(AUTHORITIES_KEY);
    if (authorityClaim == null) {
      // leads to a 401 unauthorized error
      return null;
    }
    List<? extends GrantedAuthority> authorities =
        Arrays.stream(authorityClaim.toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();

    User principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  /**
   * Gets the validity for the generated tokens.
   *
   * @return long The validity of the generated tokens in milliseconds
   */
  public long getTokenValidity() {
    return this.tokenValidityInMilliseconds;
  }

  /**
   * Validate an JWT Authorization Token
   *
   * @param authToken JWT Authorization Token
   * @return boolean indicating if token is valid
   */
  public boolean validateTokenForAuthority(String authToken) {
    return validateJwsToken(authToken);
  }

  /**
   * Validate an JWT Authorization Token
   *
   * @param authToken JWT Authorization Token
   * @return boolean indicating if token is valid
   */
  private boolean validateJwsToken(String authToken) {
    try {
      parseClaims(authToken);
      return true;
    } catch (ExpiredJwtException e) {
      this.securityMetersService.trackTokenExpired();
      log.trace("Invalid (expired) JWT token.", e);
    } catch (UnsupportedJwtException e) {
      this.securityMetersService.trackTokenUnsupported();
      log.trace("Invalid (unsupported) JWT token.", e);
    } catch (MalformedJwtException e) {
      this.securityMetersService.trackTokenMalformed();
      log.trace("Invalid (malformed) JWT token.", e);
    } catch (SignatureException e) {
      this.securityMetersService.trackTokenInvalidSignature();
      log.trace("Invalid (signature) JWT token.", e);
    } catch (IllegalArgumentException e) {
      log.error("Token validation error {}", e.getMessage());
    }
    log.debug("Invalid JWT token: {}", authToken);
    return false;
  }

  @NonNull
  private Claims parseClaims(String authToken) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken).getPayload();
  }

  @NonNull
  public <T> T getClaim(String token, String claimName, Class<T> claimType) {
    Claims claims = parseClaims(token);
    return claims.get(claimName, claimType);
  }

  @NonNull
  public Date getExpirationDate(String authToken) {
    return parseClaims(authToken).getExpiration();
  }

  @NonNull
  public Date getIssuedAtDate(String authToken) {
    return parseClaims(authToken).getIssuedAt();
  }
}
