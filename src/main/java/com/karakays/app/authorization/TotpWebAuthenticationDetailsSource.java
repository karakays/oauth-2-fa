package com.karakays.app.authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class TotpWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

	@Override
	public TotpWebAuthenticationDetails buildDetails(HttpServletRequest request) {
		return new TotpWebAuthenticationDetails(request);
	}
}
