package ar.edu.itba.paw.webapp.auth;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class PlatformUrlAuthenticationSuccessHandler 
	extends SimpleUrlAuthenticationSuccessHandler
	implements AuthenticationSuccessHandler {
	
	private static final String DEFAULT_LOGIN_SUCCESS_URL = "/home";
	private static final String DEFAULT_ADMIN_SUCCESS_URL = "/admin/";
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	public PlatformUrlAuthenticationSuccessHandler() {
		super();
		setUseReferer(true);
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String redirectUrl = null;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals("ROLE_ADMIN"))
				redirectUrl = DEFAULT_ADMIN_SUCCESS_URL; // An ADMIN will be redirected here
			else if(authority.getAuthority().equals("ROLE_USER")) {
				SavedRequest savedRequest = requestCache.getRequest(request, response);
				if(savedRequest == null) {
					redirectUrl = DEFAULT_LOGIN_SUCCESS_URL;
				}
				else {
					String targetUrl = savedRequest.getRedirectUrl();
					if(targetUrl == null || targetUrl.equals("/")) {
						redirectUrl = DEFAULT_LOGIN_SUCCESS_URL;
					}
					else {
						clearAuthenticationAttributes(request);
						getRedirectStrategy().sendRedirect(request, response, targetUrl);
						return;
					}
				}
				
			}
				
			else
				throw new IllegalStateException();
		}
		requestCache.removeRequest(request, response);
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		
		HttpSession session = request.getSession(false);
		if (session == null)
            return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
	
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}
