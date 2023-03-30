package telran.monitoring.repo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import telran.monitoring.entities.AvgPulseDoc;

public interface PulseProbesRepository extends MongoRepository<AvgPulseDoc, Long>{

	@Aggregation(pipeline = {
			"{ $match : { patientId : ?0 } }",
			"{ $group: {_id: 0, avgPulse: { $avg: '$value' }}}",
			"{ $project: {_id: 0, avgPulse: 1}}"
	})
	int getAvgValueByPatientId(long patientId);
	
	@Aggregation(pipeline = {
			"{ $match : { $and: [{patientId : ?0}, {dateTime: {$gte: ?1}}, {dateTime: {$lte: ?2}}]}}",
			"{ $group: {_id: 0, avgPulse: { $avg: '$value' }}}",
			"{ $project: {_id: 0, avgPulse: 1}}"
	})
	int getAvgValueByPatientIdAndDateRange(long patientId, LocalDateTime from, LocalDateTime to);

	boolean existsByPatientId(long patientId);
	
}
