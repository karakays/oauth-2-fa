/**
 * Copyright (C) 2018 Selçuk Karakayalı (skarakayali@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
