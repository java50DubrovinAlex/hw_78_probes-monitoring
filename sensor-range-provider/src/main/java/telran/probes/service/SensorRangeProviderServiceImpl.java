package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.dto.*;
import telran.probes.model.SensorRangeDoc;
import telran.probes.repo.SensorRangesRepo;
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
final SensorRangesRepo sensorRangesRepo;
	@Override
	public Range getSensorRange(long sensorId) {
		SensorRangeDoc res = sensorRangesRepo.findById(sensorId)
				.orElseThrow(()->new NotFoundException(String.format("sensor %d not found", sensorId)));
		log.debug("sensor {} found in DB", sensorId);
		return res.getRange();
	}

}
