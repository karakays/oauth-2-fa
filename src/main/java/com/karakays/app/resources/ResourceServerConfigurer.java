package com.karakays.app.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    private final ResourceServerTokenServices tokenServices;
    private final String resourceIds;
    
    public ResourceServerConfigurer(ResourceServerTokenServices tokenServices,
    		@Value("${security.jwt.resource-ids}") String resource) {
    	this.tokenServices = tokenServices;
    	this.resourceIds = resource;
	}
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceIds).tokenServices(tokenServices);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().and().authorizeRequests()
            .antMatchers("/check-health/**").permitAll()
            .antMatchers("/app/**").authenticated();
    }
}
