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
import org.springframework.web.filter.OncePerRequestFilter;

import com.tornado.common.api.prop.TornadoProperties;
import com.tornado.common.api.security.JwtTokenUtils;


public class SpiritUserAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Autowired
	private UserAuthDetailsService userAuthDetailsService;
	@Autowired
	private TornadoProperties tornadoProperties;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tokenHead = tornadoProperties.getJwt().getTokenHead();
		String authHeader = request.getHeader(tornadoProperties.getJwt().getHeader());
		if (authHeader != null && authHeader.startsWith(tokenHead)) {
			// The part after "Bearer"
			final String authToken = authHeader.substring(tokenHead.length()); 
			String username = jwtTokenUtils.getUsernameFromToken(authToken);

			logger.info("checking authentication " + username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = userAuthDetailsService.loadUserByUsername(username);

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
