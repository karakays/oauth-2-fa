package com.karakays.app.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
//@EnableResourceServer
public class ResourceServerConfigurator { ///extends ResourceServerConfigurerAdapter {
    private final ResourceServerTokenServices tokenServices;
    private final String resourceIds;
    
    public ResourceServerConfigurator(ResourceServerTokenServices tokenServices,
    		@Value("${security.jwt.resource-ids}") String resource) {
    	this.tokenServices = tokenServices;
    	this.resourceIds = resource;
	}
    
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId("web-resourcej").tokenServices(tokenServices);
//    }
//    
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.requestMatchers().and().authorizeRequests()
//            .antMatchers("/check-health/**").permitAll()
//            .antMatchers("/app/**").authenticated()
//            .antMatchers("/foo").access("#oauth2.hasScope('write')");
//    }
    
	@Bean
	protected ResourceServerConfiguration adminResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {	
			// Switch off the Spring Boot @Autowired configurers
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId("oauth2/admin");
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.antMatcher("/admin/**").authorizeRequests().anyRequest()
						.access("#oauth2.hasScope('read')");
			}

		}));
		resource.setOrder(3);

		return resource;

	}
    
	@Bean
	protected ResourceServerConfiguration otherResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			// Switch off the Spring Boot @Autowired configurers
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId("oauth2/other");
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().anyRequest().access("#oauth2.hasScope('trust')");
			}
		}));
		resource.setOrder(4);

		return resource;

	}
}
