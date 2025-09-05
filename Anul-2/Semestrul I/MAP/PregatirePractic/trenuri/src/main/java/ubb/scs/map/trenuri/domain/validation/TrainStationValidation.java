package ubb.scs.map.trenuri.domain.validation;

import ubb.scs.map.trenuri.domain.City;
import ubb.scs.map.trenuri.domain.TrainStation;

public class TrainStationValidation implements Validation<TrainStation> {
    @Override
    public void validate(TrainStation entity) {
        if (entity.getDepartureCityId().isEmpty() || entity.getDestinationCityId().isEmpty()) {
            throw new ValidationException("It cannot be empty");
        }

    }
}
