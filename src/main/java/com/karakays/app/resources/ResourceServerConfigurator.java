package com.karakays.app.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfigurator {
   	@Bean
	protected ResourceServerConfiguration webResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {	
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId("web-resource");
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.antMatcher("/app/**").authorizeRequests().anyRequest()
						.access("#oauth2.hasScope('read')");
			}

		}));
		resource.setOrder(3);

		return resource;
	}
    
	@Bean
	protected ResourceServerConfiguration adminResources() {

		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};

		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId("admin-resource");
			}

			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.antMatcher("/foo/**").authorizeRequests().anyRequest().access("#oauth2.hasScope('write')");
			}
		}));
		resource.setOrder(4);

		return resource;
	}
}
