package se.pra.api.core.recommentation;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface RecommendationService {

  /**
   * Sample usage:
   *
   * curl -X POST $HOST:$PORT/recommendation \
   *   -H "Content-Type: application/json" --data \
   *   '{"movieId":123,"recommendationId":456,"author":"me","rate":5,"content":"yada, yada, yada"}'
   *
   * @param body
   * @return
   */
  @PostMapping(
      value    = "/recommendation",
      consumes = "application/json",
      produces = "application/json")
  Recommendation createRecommendation(@RequestBody Recommendation body);

  /**
   * Sample usage:
   *
   * curl $HOST:$PORT/recommendation?movieId=1
   *
   * @param movieId
   * @return
   */
  @GetMapping(
      value    = "/recommendation",
      produces = "application/json")
  List<Recommendation> getRecommendations(@RequestParam(value = "movieId", required = true) int movieId);

  /**
   * Sample usage:
   *
   * curl -X DELETE $HOST:$PORT/recommendation?movieId=1
   *
   * @param movieId
   */
  @DeleteMapping(value = "/recommendation")
  void deleteRecommendations(@RequestParam(value = "movieId", required = true)  int movieId);

}
