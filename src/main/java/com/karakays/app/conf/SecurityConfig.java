package com.karakays.app.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.karakays.app.authorization.TotpWebAuthenticationDetailsSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String signingKey;
    
    private final String realm;
    
    private final UserDetailsService userDetailsService;
    
    private final AuthenticationProvider totpAuthenticationProvider;
    
    public SecurityConfig(@Value("${security.signing-key}") String key, @Value("${security.security-realm}") String realm,
    		UserDetailsService userDetails, AuthenticationProvider provider) {
    	this.signingKey = key;
    	this.realm = realm;
    	this.userDetailsService = userDetails;
    	this.totpAuthenticationProvider = provider;
	}

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(totpAuthenticationProvider);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().httpBasic().realmName(this.realm)
            .and().formLogin().authenticationDetailsSource(new TotpWebAuthenticationDetailsSource())
            .and().csrf().disable();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }
    
    @Bean
    public TokenStore tokenStore() {
       return new JwtTokenStore(this.accessTokenConverter()); 
    }
    
   @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(this.tokenStore());
        services.setSupportRefreshToken(true);
        return services;
    }
}
