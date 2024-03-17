package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;

import telran.probes.service.RangeProviderClientService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
	String producerBindingName = "analyzerProducer-out-0";
	final RangeProviderClientService clientService;
	final StreamBridge streamBridge;
	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);

	}
	@Bean
	Consumer<ProbeData> analyzerConsumer() {
		return probeData -> probeDataAnalyzing(probeData);
	}
	private void probeDataAnalyzing(ProbeData probeData) {
		// in the case probeData value doesn't fall into a range received from RangeProviderClientService
		// create a proper deviation and  streamBridge.send(producerBindingName, deviation);
		log.trace("received probe: {}", probeData);
		long sensorId = probeData.id();
		Range range = clientService.getRange(sensorId);
		double value = probeData.value();
		
		double border = Double.NaN;
		if (value < range.minValue()) {
			border = range.minValue();
		} else if(value > range.maxValue()) {
			border = range.maxValue();
		}
		if (!Double.isNaN(border)) {
			double deviation = value - border;
			log.debug("deviation: {}", deviation);
			DeviationData dataDeviation =
					new DeviationData(sensorId, deviation, value, System.currentTimeMillis());
			streamBridge.send(producerBindingName, dataDeviation);
			log.debug("deviation data {} sent to {}", dataDeviation, producerBindingName);
			
		}
		
	}

}
