package com.obakeng.MovieApi.controllers;

import com.obakeng.MovieApi.dto.MovieDto;
import com.obakeng.MovieApi.dto.PageResponse;
import com.obakeng.MovieApi.exceptions.EmptyFileException;
import com.obakeng.MovieApi.service.MovieService;
import com.obakeng.MovieApi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/movie")
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final AppConstants appConstants;

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file
            , @RequestPart String movieDto) throws IOException, EmptyFileException {
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty! Please send another file");
        }
        MovieDto dto = appConstants.convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId
            , @RequestPart MultipartFile file
            , @RequestPart String movieObject) throws IOException {

        if (file.isEmpty()) file= null;

        MovieDto movieDto = appConstants.convertToMovieDto(movieObject);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto,file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return  ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<PageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber
            ,@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<PageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber
            ,@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
            ,@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy
            ,@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, dir));
    }

}



