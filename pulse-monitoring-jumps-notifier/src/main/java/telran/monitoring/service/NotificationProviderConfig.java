package telran.monitoring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NotificationProviderConfig {

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
}
