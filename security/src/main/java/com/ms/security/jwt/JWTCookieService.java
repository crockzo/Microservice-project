package com.ms.security.jwt;

import com.ms.security.config.Constants;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class JWTCookieService {

  private static final String DEVELOPMENT_PROFILE = "dev";

  private final TokenProvider tokenProvider;

  private final Environment environment;

  /**
   * Builds a cookie with the tool claim in the jwt
   *
   * @return the login ResponseCookie containing the JWT
   */
  public ResponseCookie buildLoginCookie() {
    long duration = tokenProvider.getTokenValidity();
    String jwt =
        tokenProvider.createToken(SecurityContextHolder.getContext().getAuthentication(), duration);
    return buildJWTCookie(jwt, Duration.of(duration, ChronoUnit.MILLIS));
  }

  /**
   * Builds the cookie containing the jwt for a login
   *
   * @param rotatedJwtToken with the updated values
   * @param durationInMilliseconds of the cookie in milliseconds and the jwt
   * @return the login {@link ResponseCookie} containing the JWT
   */
  public ResponseCookie buildRotatedCookie(String rotatedJwtToken, long durationInMilliseconds) {
    return buildJWTCookie(rotatedJwtToken, Duration.of(durationInMilliseconds, ChronoUnit.MILLIS));
  }

  /**
   * Builds the cookie containing the jwt
   *
   * @param jwt the token that will be used as the cookie's value
   * @param duration the validity of the cookie
   * @return the response cookie that should be set containing the jwt
   */
  private ResponseCookie buildJWTCookie(String jwt, Duration duration) {
    Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
    boolean isSecure = !activeProfiles.contains(DEVELOPMENT_PROFILE);
    return ResponseCookie.from(Constants.JWT_COOKIE_NAME, jwt)
        .httpOnly(true) // Must be httpOnly
        .sameSite("Lax") // Must be Lax to allow navigation links to Artemis to work
        .secure(isSecure) // Must be secure
        .path("/") // Must be "/" to be sent in ALL request
        .maxAge(duration) // Duration should match the duration of the jwt
        .build(); // Build cookie
  }

  /**
   * Builds the cookie containing the jwt for a logout and sets it in the response
   *
   * @return the logout ResponseCookie
   */
  public ResponseCookie buildLogoutCookie() {
    return buildJWTCookie("", Duration.ZERO);
  }
}
