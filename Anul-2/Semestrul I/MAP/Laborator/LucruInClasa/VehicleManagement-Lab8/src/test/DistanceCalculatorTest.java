package test;

import org.junit.Before;
import org.junit.Test;

import domain.GPSCoordinates;
import service.DistanceCalculator;
import service.EuclideanDistanceCalculator;

public class DistanceCalculatorTest {
	
	private DistanceCalculator dist;
	
	@Before
	public void setUp(){
		dist=new EuclideanDistanceCalculator();
	}
	
	@Test
	public void testDistanceComputation(){
		GPSCoordinates point1=new GPSCoordinates(2.0, 3.0);
		GPSCoordinates point2=new GPSCoordinates(3.5, 2.5);
		Double d=dist.computeDistance(point1, point2);

		assert(d==1.5811388300841898);

	}

}
