package com.karakays.app.authorization;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.karakays.app.domain.UserCredentials;

@Component
public class TotpAuthenticationProvider extends DaoAuthenticationProvider {
	private final TotpAuthenticator totpAuthenticator;
	
	public TotpAuthenticationProvider(TotpAuthenticator authenticator, UserDetailsService userDetailsService) {
		this.totpAuthenticator = authenticator;
		this.setUserDetailsService(userDetailsService);
		this.setPasswordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		super.additionalAuthenticationChecks(userDetails, authentication);
		
		UserCredentials userCredentials = ((UserCredentials) userDetails); 

		if (userCredentials.is2FaEnabled()) {
			String secret = ((UserCredentials) userDetails).getSecret();
			String totpKeyString = (String) ((LinkedHashMap<?, ?>) authentication.getDetails()).get("otp");
			Integer totpKey = null;

			try {
				totpKey = Integer.valueOf(totpKeyString);
			} catch (NumberFormatException e) {
				totpKey = null;
			}

			if (totpKey != null) {
				try {
					if (!totpAuthenticator.verifyCode(secret, totpKey, 2)) {
						throw new BadCredentialsException("Invalid OTP code");
					}
				} catch (InvalidKeyException | NoSuchAlgorithmException e) {
					throw new InternalAuthenticationServiceException("OTP code verification failed", e);
				}
			} else {
				throw new BadCredentialsException("OTP code is mandatory");
			}
		}
	}

	public TotpAuthenticator getTotpAuthenticator() {
		return totpAuthenticator;
	}
}
