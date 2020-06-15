package se.pra.movie.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import se.pra.api.core.movie.Movie;
import se.pra.api.core.movie.MovieService;
import se.pra.movie.persistence.MovieEntity;
import se.pra.movie.persistence.MovieRepository;
import se.pra.util.exception.InvalidInputException;
import se.pra.util.exception.NotFoundException;
import se.pra.util.http.ServiceUtil;

@Slf4j
@RestController
public class MovieServiceImpl implements MovieService {

  private final ServiceUtil serviceUtil;
  private final MovieRepository repository;
  private final MovieMapper mapper;

  public MovieServiceImpl(ServiceUtil serviceUtil, MovieRepository repository, MovieMapper mapper) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Movie getMovie(int movieId) {
    log.debug("/movie return the found movie for {}", movieId);

    if (movieId < 0) {
      throw new InvalidInputException("Invalid movieId: " + movieId);
    }
    MovieEntity entity = repository.findByMovieId(movieId)
        .orElseThrow(() -> new NotFoundException("No movie found for movieId: " + movieId));

    Movie response = mapper.entityToApi(entity);
    response.setServiceAddress(serviceUtil.getServiceAddress());

    log.debug("getMovie: found movieId: {}", response.getMovieId());

    return response;
  }

  @Override
  public Movie createMovie(Movie body) {
    try {
      MovieEntity entity = mapper.apiToEntity(body);
      MovieEntity newEntity = repository.save(entity);
      return mapper.entityToApi(newEntity);
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, movie Id: " + body.getMovieId());
    }
  }

  @Override
  public void deleteMovie(int movieId) {
    log.debug("deleteMovie: tries to delete an entity with movieId: {}", movieId);
    repository.findByMovieId(movieId).ifPresent(e -> repository.delete(e));
  }
}
