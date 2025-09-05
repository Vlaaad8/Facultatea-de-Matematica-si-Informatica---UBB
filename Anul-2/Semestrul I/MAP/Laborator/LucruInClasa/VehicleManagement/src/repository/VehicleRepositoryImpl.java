package repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import domain.Vehicle;

public class VehicleRepositoryImpl implements VehicleRepository {

	private ArrayList<Vehicle> list;

	public VehicleRepositoryImpl() {
		this.list=new ArrayList<>();
	}

	public void addVehicle(Vehicle newVehicle) {
		if (!newVehicle.isInactive()) {
			list.add(newVehicle);
		}
	}

	public Vehicle getVehicleAtPosition(int position) {
		return list.get(position);
	}

	public int getNumberOfVehicles() {
		return list.size();
	}

	public ArrayList<Vehicle> getVehicles() {
		return list;
	}

	@Override
	public void deleteVehicle(Vehicle vehicle) {
		Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);

		if ((currentYear - vehicle.getYear()) > 30) {
			// we will delete the vehicle, mark it as being inactive
			vehicle.setInactive();
		}
	}
}
