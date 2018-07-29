/**
 * Copyright (C) 2018 Selçuk Karakayalı (skarakayali@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
