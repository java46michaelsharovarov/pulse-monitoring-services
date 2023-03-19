package telran.monitoring.service;

import telran.monititoring.model.NotificationData;

public interface DataProviderService {

	NotificationData getNotificationData(long patientId);
	
}
