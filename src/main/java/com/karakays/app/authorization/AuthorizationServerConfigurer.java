package com.karakays.app.authorization;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
    private final TokenStore tokenStore;
    
    private final JwtAccessTokenConverter accessTokenConverter;
    
    private final AuthenticationManager authenticationManager;
    
    private final DataSource dataSource;
    
	public AuthorizationServerConfigurer(TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter,
			AuthenticationManager authenticationManager, DataSource dataSource) {
		this.tokenStore = tokenStore;
		this.accessTokenConverter = accessTokenConverter;
		this.authenticationManager = authenticationManager;
		this.dataSource = dataSource;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    /**
     * @see {@linkplain org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer.configure}
    */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.jdbc(dataSource);
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        endpoints.pathMapping("/oauth/token", "/tokens")
        	.tokenStore(tokenStore).accessTokenConverter(accessTokenConverter).tokenEnhancer(tokenEnhancerChain)
                    .authenticationManager(authenticationManager);
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    	security.tokenKeyAccess("permitAll()");
    }
}
