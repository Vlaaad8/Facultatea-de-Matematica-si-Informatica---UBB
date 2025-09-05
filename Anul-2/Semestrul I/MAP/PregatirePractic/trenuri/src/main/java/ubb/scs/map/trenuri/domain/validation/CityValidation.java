package ubb.scs.map.trenuri.domain.validation;

import ubb.scs.map.trenuri.domain.City;

public class CityValidation implements Validation<City> {
    @Override
    public void validate(City entity) {
        if (entity.getName().isEmpty()) {
            throw new ValidationException("It cannot be empty");
        }

    }
}
