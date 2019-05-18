package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionsHandler {
	
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

}
