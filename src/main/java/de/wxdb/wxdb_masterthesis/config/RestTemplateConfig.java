package de.wxdb.wxdb_masterthesis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Konfigurationsklasse für das RestTemplate.
 * 
 * @author Kaan Mustafa Celik
 */
@Configuration
public class RestTemplateConfig {
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
