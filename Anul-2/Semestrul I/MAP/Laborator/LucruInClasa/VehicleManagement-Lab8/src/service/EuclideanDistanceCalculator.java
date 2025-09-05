package service;

import domain.GPSCoordinates;

import static java.lang.Math.sqrt;

public class EuclideanDistanceCalculator implements DistanceCalculator{

	@Override
	public Double computeDistance(GPSCoordinates point1, GPSCoordinates point2) {
		Double firstMember=(point2.getLatitude()-point1.getLatitude())*(point2.getLatitude()-point1.getLatitude());
		Double secondMember=(point2.getLongitude()-point1.getLongitude())*(point2.getLongitude()-point1.getLongitude());
		Double distance=sqrt((firstMember+secondMember));
		return distance;
	}

}
