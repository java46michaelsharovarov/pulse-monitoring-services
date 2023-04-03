package telran.monitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import telran.monitoring.model.NotificationData;

@Service
@Slf4j
public class NotificationDataProviderImpl implements NotificationDataProvider {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.data.provider.mapping.url}")
	String mappingUrl;
	
	@Value("${app.data.provider.host}")
	String host;
	
	@Value("${app.data.provider.port}")
	int port;
	
	@Override
	public NotificationData getData(long patientId) {
		ResponseEntity<NotificationData> response = 
				restTemplate.exchange(getFullUrl(patientId), HttpMethod.GET, null, NotificationData.class);
		NotificationData notificationData = response.getBody();
		log.debug("notificationData - doctorEmail: {}, doctorName: {}, patientName: {}", notificationData.doctorEmail, notificationData.doctorName, notificationData.patientName);
		return notificationData;
	}
	
	private String getFullUrl(long patientId) {
		String uri = String.format("http://%s:%d/%s/%d", host, port, mappingUrl, patientId);
		log.debug("URI for communication with data provider is {}", uri);
		return uri;
	}

}
