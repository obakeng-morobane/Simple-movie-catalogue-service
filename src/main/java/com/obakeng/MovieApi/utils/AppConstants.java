package com.obakeng.MovieApi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obakeng.MovieApi.dto.MovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AppConstants {

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "3";
    public static final String SORT_BY  = "movieId";
    public static final String SORT_DIR = "asc";

    public MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);

    }
}
