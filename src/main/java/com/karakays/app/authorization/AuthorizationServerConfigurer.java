package com.karakays.app.authorization;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;
    
    @Value("${security.jwt.grant-type}")
    private String grantType;
    
    @Value("${security.jwt.scope-read}")
    private String scopeRead;
    
    @Value("${security.jwt.scope-write}")
    private String scopeWrite;
    
    @Value("${security.jwt.resource-ids}")
    private String resourceIds;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    /**
     * @see {@linkplain org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer.configure}
    */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientId) // start building a client
            .secret(clientSecret)
            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
            .scopes(scopeRead, scopeWrite, "custom-sceop-1")
            .resourceIds(resourceIds);
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
