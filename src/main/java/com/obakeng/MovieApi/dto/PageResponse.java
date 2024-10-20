package com.obakeng.MovieApi.dto;

import java.util.List;

public record PageResponse (List<MovieDto> movieDtos
        , Integer pageNumber
        , Integer pageSize
        , Long totalElements
        , int totalPages
        , boolean isLast) {
}
