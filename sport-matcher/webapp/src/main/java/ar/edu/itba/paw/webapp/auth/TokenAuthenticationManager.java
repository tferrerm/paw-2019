package ar.edu.itba.paw.webapp.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationManager {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAuthenticated() {
		Authentication auth = getAuthentication();
		return auth != null && auth.isAuthenticated() && 
				!(auth instanceof AnonymousAuthenticationToken);
	}
	
	public boolean isAdmin() {
		Authentication auth = getAuthentication();
		if(!isAuthenticated())
			return false;
		for(GrantedAuthority authority : auth.getAuthorities()) {
			if(authority.getAuthority().equals("ROLE_ADMIN"))
				return true;
		}
		return false;
	}
	
	public void authenticate(String username, String password, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				username, password);
		authToken.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
