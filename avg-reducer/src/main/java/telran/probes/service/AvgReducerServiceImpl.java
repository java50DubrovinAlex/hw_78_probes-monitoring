package telran.probes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbesList;
import telran.probes.repo.ProbesListRepo;
@Service
@Slf4j
@RequiredArgsConstructor
public class AvgReducerServiceImpl implements AvgReducerService {
final ProbesListRepo probesListRepo;
@Value("${app.reducing.size}")
int reducingSize;
	@Override
	public Double getAvgValue(ProbeData probeData) {
		Double res = null;
		Long sensorId = probeData.id();
		ProbesList probesList = probesListRepo.findById(sensorId).orElse(null);
		if(probesList == null || probesList.getValue() == null) {
			probesList = new ProbesList(sensorId);
			log.debug("either probesList is null or value of probesList is null");
		}
		List<Double> listProbeValues = probesList.getValue();
		listProbeValues.add(probeData.value());
		if(listProbeValues.size() >= reducingSize) {
			res = listProbeValues.stream().mapToDouble(v -> v).average().getAsDouble();
			log.debug("average value for reducing size {} is {}", reducingSize, res);
			listProbeValues.clear();
		}
		probesListRepo.save(probesList);
		return res;
	}

}
