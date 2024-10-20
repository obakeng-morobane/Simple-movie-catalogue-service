package com.obakeng.MovieApi.service;

import com.obakeng.MovieApi.dto.MovieDto;
import com.obakeng.MovieApi.dto.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface iMovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);
    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException;

    String deleteMovie(Integer movieId) throws IOException;

    PageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    PageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize
            , String sortBy, String dir);
}
