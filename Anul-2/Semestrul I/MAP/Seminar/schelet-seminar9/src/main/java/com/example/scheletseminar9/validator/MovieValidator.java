package com.example.scheletseminar9.validator;


import com.example.scheletseminar9.domain.Movie;

import java.time.Year;

public class MovieValidator implements Validator<Movie> {

    @Override
    public void validate(Movie entity) {
        String err = "";
        if (entity.getTitle() == null || entity.getTitle().isBlank()) {
            err += "Empty title; ";
        }
        if (entity.getDirector() == null || entity.getDirector().isBlank()) {
            err += "Empty director; ";
        }
        if (entity.getYear() < 1900 || entity.getYear() > Year.now().getValue()) {
            err += "Invalid year; ";
        }
        if (!err.equals(""))
            throw new ValidationException(err);
    }
}
