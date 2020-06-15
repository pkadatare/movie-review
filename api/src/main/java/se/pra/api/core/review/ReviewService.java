package se.pra.api.core.review;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewService {

  /**
   * Sample usage:
   *
   * curl -X POST $HOST:$PORT/review \
   *   -H "Content-Type: application/json" --data \
   *   '{"movieId":123,"reviewId":456,"author":"me","subject":"yada, yada, yada","content":"yada, yada, yada"}'
   *
   * @param body
   * @return
   */
  @PostMapping(
      value    = "/review",
      consumes = "application/json",
      produces = "application/json")
  Review createReview(@RequestBody Review body);

  /**
   * Sample usage: curl $HOST:$PORT/review?movieId=1
   *
   * @param movieId
   * @return
   */
  @GetMapping(
      value    = "/review",
      produces = "application/json")
  List<Review> getReviews(@RequestParam(value = "movieId", required = true) int movieId);

  /**
   * Sample usage:
   *
   * curl -X DELETE $HOST:$PORT/review?movieId=1
   *
   * @param movieId
   */
  @DeleteMapping(value = "/review")
  void deleteReviews(@RequestParam(value = "movieId", required = true)  int movieId);

}
