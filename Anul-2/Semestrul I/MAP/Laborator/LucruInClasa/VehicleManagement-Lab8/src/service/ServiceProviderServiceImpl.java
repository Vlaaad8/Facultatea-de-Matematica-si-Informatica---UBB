package service;

import java.util.Comparator;
import java.util.List;

import domain.GPSCoordinates;
import domain.ServiceProvider;
import repository.ServiceProviderRepository;

public class ServiceProviderServiceImpl implements ServiceProviderService {
	private DistanceCalculator distanceCalculator;
	private ServiceProviderRepository serviceProviderRepository;

	public ServiceProviderServiceImpl(DistanceCalculator distanceCalculator, ServiceProviderRepository repo) {
		this.distanceCalculator = distanceCalculator;
		this.serviceProviderRepository=repo;
	}

	@Override
	public ServiceProvider getNearestServiceProvider(GPSCoordinates customerBreakdown) {
		List<ServiceProvider>serviceProviders = serviceProviderRepository.getServiceProviders();
		
		serviceProviders.forEach(s->{
			Double distance = distanceCalculator.computeDistance(s.getCoordinates(), customerBreakdown);
			s.setDistance(distance);

			
		});

		serviceProviderRepository.getServiceProviders().stream().sorted(Comparator.comparingDouble(ServiceProvider::getDistance).reversed());
		serviceProviderRepository.getServiceProviders().forEach(s->{
			System.out.println(s.toString());
		});
		return serviceProviders.get(0);
	}
}
