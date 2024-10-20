package com.obakeng.MovieApi.repository;

import com.obakeng.MovieApi.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Movie findByPoster(String file);
}
