package hours22.daedeokserver.security;

import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.token.TokenExpiredException;
import hours22.daedeokserver.exception.token.TokenInvalidException;
import hours22.daedeokserver.user.domain.Role;
import hours22.daedeokserver.user.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    // 토큰 유효시간 30분
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    //public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 1000L;
    //public static final long REFRESH_TOKEN_EXPIRE_TIME = 2 * 60 * 1000L;

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String id, Role role) {

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(id)       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, role)        // payload "auth": "ROLE_USER"
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        Date refreshTokenExpiresIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        return new TokenDto(accessToken, refreshToken, REFRESH_TOKEN_EXPIRE_TIME / 1000);
    }

    public TokenDto.Response createAccessToken(String id, Role role) {

        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(id)       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, role)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        return new TokenDto.Response(accessToken, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getSubject(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new TokenInvalidException(ErrorCode.AUTH_INVALID);
        }

        return claims.getSubject();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
            throw new TokenInvalidException(ErrorCode.AUTH_INVALID);
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            throw new TokenExpiredException(ErrorCode.AUTH_TIME_OUT);
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            throw new TokenInvalidException(ErrorCode.AUTH_INVALID);
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
            throw new TokenInvalidException(ErrorCode.AUTH_INVALID);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
