package se.pra.api.core.movie;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MovieService {

  /**
   * Sample usage:
   *
   * curl -X POST $HOST:$PORT/movie \
   *   -H "Content-Type: application/json" --data \
   *   '{"movieId":123,"name":"movie 123","year":1234}'
   *
   * @param body
   * @return
   */
  @PostMapping(
      value    = "/movie",
      consumes = "application/json",
      produces = "application/json")
  Movie createMovie(@RequestBody Movie body);


  /**
   * Sample usage: curl $HOST:$PORT/movie/1
   *
   * @param movieId
   * @return the movie, if found, else null
   */
  @GetMapping(value = "movie/{movieId}")
  Movie getMovie(@PathVariable int movieId);

  /**
   * Sample usage:
   *
   * curl -X DELETE $HOST:$PORT/Movie/1
   *
   * @param movieId
   */
  @DeleteMapping(value = "/movie/{movieId}")
  void deleteMovie(@PathVariable int movieId);

}
