package com.obakeng.MovieApi.service;

import com.obakeng.MovieApi.dto.MovieDto;
import com.obakeng.MovieApi.dto.PageResponse;
import com.obakeng.MovieApi.exceptions.FileExistsException;
import com.obakeng.MovieApi.exceptions.MovieNotFoundException;
import com.obakeng.MovieApi.models.Movie;
import com.obakeng.MovieApi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements iMovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;
    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // upload the file
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! Please enter another file name!");
        }
        String uploadedFileName =  fileService.uploadFile(path, file);
        // set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);
        //mao dto to movie object
        Movie movie = new Movie(null
                ,movieDto.getTitle()
                ,movieDto.getDirector()
                ,movieDto.getStudio()
                ,movieDto.getMovieCast()
                ,movieDto.getReleaseYear()
                ,movieDto.getPoster()
        );
        // save the movie object -> saved Movie object
        Movie saveMovie = movieRepository.save(movie);
        // generate the posterUrl
        String posterUrl = baseUrl + "/file/" + uploadedFileName;
        // map movie object to dto object and return it
        MovieDto response = new MovieDto(
                saveMovie.getMovieId()
                ,saveMovie.getTitle()
                ,saveMovie.getDirector()
                ,saveMovie.getStudio()
                ,saveMovie.getMovieCast()
                ,saveMovie.getReleaseYear()
                ,saveMovie.getPoster()
                ,posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //1. check the data in the DB and if it exists, fetch the data of the given ID
        Movie movie = movieRepository.findById(movieId).orElseThrow(() ->
                new MovieNotFoundException("Movie not found with id = " + movieId));
        // generate posterUrl
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        // map to MovieDto and return it
        MovieDto response = new MovieDto(
                movie.getMovieId()
                ,movie.getTitle()
                ,movie.getDirector()
                ,movie.getStudio()
                ,movie.getMovieCast()
                ,movie.getReleaseYear()
                ,movie.getPoster()
                ,posterUrl
        );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        // fetch all data from DB
        List<Movie> movies =movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();
        // iterate through the list. generate posterUrl for each movie objects
        // and map to the movieDto obj
        for (Movie movie : movies){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto getMovies = new MovieDto(
                    movie.getMovieId()
                    ,movie.getTitle()
                    ,movie.getDirector()
                    ,movie.getStudio()
                    ,movie.getMovieCast()
                    ,movie.getReleaseYear()
                    ,movie.getPoster()
                    ,posterUrl
            );
            movieDtos.add(getMovies);
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
            // 1. check if movie object exists
            Movie mv = movieRepository.findById(movieId).orElseThrow(() ->
                    new MovieNotFoundException("Movie not found with id = " + movieId));

            // 2. if file is null do nothing
            //  if file is not null, then delete existing file associated with the record
            //and upload the new file
            String fileName = mv.getPoster();
            if (file != null){
                Files.deleteIfExists(Paths.get(path + File.separator + fileName));
                fileName = fileService.uploadFile(path, file);
            }

            // 3. set MovieDto poster value, according to step 2
            movieDto.setPoster(fileName);
            // 4. map it to Movie object
            Movie movie = new Movie(
                    mv.getMovieId()
                    ,movieDto.getTitle()
                    ,movieDto.getDirector()
                    ,movieDto.getStudio()
                    ,movieDto.getMovieCast()
                    ,movieDto.getReleaseYear()
                    ,movieDto.getPoster()
            );
            // 5. save the movie object -> return saved movie object
            Movie updatedMovie = movieRepository.save(movie);
            // 6. generate postUrl
            String posterUrl = baseUrl + "/file/" + fileName;
            // 7.  map to MovieDto and return it

            MovieDto response = new MovieDto(
                    movie.getMovieId()
                    ,movie.getTitle()
                    ,movie.getDirector()
                    ,movie.getStudio()
                    ,movie.getMovieCast()
                    ,movie.getReleaseYear()
                    ,movie.getPoster()
                    ,posterUrl
            );
            return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        // 1. check if movie object exist in database
        Movie mv = movieRepository.findById(movieId).orElseThrow(() ->
                new MovieNotFoundException("Movie not found with id = " + movieId));
        Integer id = mv.getMovieId();

        // 2. delete the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
        // 3. delete the movie object
        movieRepository.delete(mv);
        return "Movie deleted with id: " + id;
    }

    @Override
    public PageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        // iterate through the list. generate posterUrl for each movie objects
        // and map to the movieDto obj
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto getMovies = new MovieDto(
                    movie.getMovieId()
                    , movie.getTitle()
                    , movie.getDirector()
                    , movie.getStudio()
                    , movie.getMovieCast()
                    , movie.getReleaseYear()
                    , movie.getPoster()
                    , posterUrl
            );
            movieDtos.add(getMovies);
        }
        return new PageResponse(movieDtos, pageNumber,pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    @Override
    public PageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        // iterate through the list. generate posterUrl for each movie objects
        // and map to the movieDto obj
        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto getMovies = new MovieDto(
                    movie.getMovieId()
                    , movie.getTitle()
                    , movie.getDirector()
                    , movie.getStudio()
                    , movie.getMovieCast()
                    , movie.getReleaseYear()
                    , movie.getPoster()
                    , posterUrl
            );
            movieDtos.add(getMovies);
        }
        return new PageResponse(movieDtos, pageNumber,pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }
}
