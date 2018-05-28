package com.karakays.app.authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Encapsulates TOTP that the end-user entered during login in addition to the username/password details.
 */
public class TotpWebAuthenticationDetails extends WebAuthenticationDetails {
	private Integer totpKey;

	public TotpWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		String totpKeyString = request.getParameter("otp");
		if (totpKeyString != null && totpKeyString.isEmpty()) {
			try {
				this.totpKey = Integer.valueOf(totpKeyString);
			} catch (NumberFormatException e) {
				this.totpKey = null;
			}
		}
	}

	public Integer getTotpKey() {
		return this.totpKey;
	}
}
