package telran.probes.service;

public interface EmailsProviderClientService {
	String SERVICE_EMAIL = "dubrovinjava50@gmail.com";

	String[] getMails(long sensorId);
}
