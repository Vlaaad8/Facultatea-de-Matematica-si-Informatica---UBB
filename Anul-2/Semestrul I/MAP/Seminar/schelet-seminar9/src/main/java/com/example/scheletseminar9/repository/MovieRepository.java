package com.example.scheletseminar9.repository;

import com.example.scheletseminar9.domain.Movie;

import java.util.List;

public interface MovieRepository extends Repository<Long, Movie> {
    List<Integer> getYears();
}
