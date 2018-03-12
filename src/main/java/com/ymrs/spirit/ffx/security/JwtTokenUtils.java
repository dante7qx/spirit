package com.ymrs.spirit.ffx.security;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.ymrs.spirit.ffx.constant.JWTConsts;
import com.ymrs.spirit.ffx.prop.SpiritProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = 6940696655173940036L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtils.class);
	
    @Autowired
    private SpiritProperties spiritProperties;
    
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(Claims.ISSUED_AT));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    public String generateToken(Long id, String account) {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put(Claims.SUBJECT, account + JWTConsts.TOKEN_SPLIT + id);
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }
    
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(spiritProperties.getJwt().getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
        	LOGGER.error("getClaimsFromToken {} error.", token, e);
            claims = null;
        }
        return claims;
    }
    
    private Date generateExpirationDate() {
        return Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + spiritProperties.getJwt().getExpiration() * 1000));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, spiritProperties.getJwt().getSecret())
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
    	SpiritPrincipal user = (SpiritPrincipal) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        SpiritLoginUser loginUser = user.getSpiritLoginUser();
        String sub = loginUser.getAccount() + JWTConsts.TOKEN_SPLIT + loginUser.getId();
//        Date lastPwdUpdateDate = loginUser.getLastPwdUpdateDate();
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(sub)
                        && !isTokenExpired(token));
//                        && !isCreatedBeforeLastPasswordReset(created, lastPwdUpdateDate));
    }
    
}
