package com.ymrs.spirit.ffx.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ymrs.spirit.ffx.constant.JWTConsts;
import com.ymrs.spirit.ffx.prop.SpiritProperties;
import com.ymrs.spirit.ffx.util.StringUtils;


public class SpiritJwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private SpiritJwtTokenUtils jwtTokenUtils;
	@Autowired
	private SpiritUserDetailsService spiritUserDetailsService;
	@Autowired
	private SpiritProperties spiritProperties;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(CorsUtils.isPreFlightRequest(request)) {
			System.out.println("=====================================");
		}
		
		String tokenHead = spiritProperties.getJwt().getTokenHead();
		String authHeader = request.getHeader(spiritProperties.getJwt().getHeader());
		if (authHeader != null && authHeader.startsWith(tokenHead)) {
			// The part after "Bearer"
			final String authToken = authHeader.substring(tokenHead.length()); 
			String username = jwtTokenUtils.getUsernameFromToken(authToken);
			if(StringUtils.isNotEmpty(username)) {
				username = username.split(JWTConsts.TOKEN_SPLIT)[0];
				logger.info("checking authentication " + username);
			}
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = spiritUserDetailsService.loadUserByUsername(username);

				if (jwtTokenUtils.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} 
		} 

		filterChain.doFilter(request, response);
	}

}
