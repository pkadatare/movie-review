package se.pra.composite.movie.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.pra.api.composite.movie.MovieAggregate;
import se.pra.api.composite.movie.MovieCompositeService;
import se.pra.api.composite.movie.RecommendationSummary;
import se.pra.api.composite.movie.ReviewSummary;
import se.pra.api.composite.movie.ServiceAddresses;
import se.pra.api.core.movie.Movie;
import se.pra.api.core.recommentation.Recommendation;
import se.pra.api.core.review.Review;
import se.pra.util.exception.NotFoundException;
import se.pra.util.http.ServiceUtil;

@Slf4j
@RestController
public class MovieCompositeServiceImpl implements MovieCompositeService {

  private ServiceUtil serviceUtil;
  private  MovieCompositeIntegration integration;

  @Autowired
  public MovieCompositeServiceImpl(ServiceUtil serviceUtil,
      MovieCompositeIntegration integration) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public void createCompositeMovie(MovieAggregate body) {
    try {

      log.debug("createCompositeMovie: creates a new composite entity for movieId: {}", body.getMovieId());

      Movie movie = new Movie(body.getMovieId(), body.getName(), body.getWeight(), null);
      integration.createMovie(movie);

      if (body.getRecommendations() != null) {
        body.getRecommendations().forEach(r -> {
          Recommendation recommendation = new Recommendation(body.getMovieId(), r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent(), null);
          integration.createRecommendation(recommendation);
        });
      }

      if (body.getReviews() != null) {
        body.getReviews().forEach(r -> {
          Review review = new Review(body.getMovieId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
          integration.createReview(review);
        });
      }

      log.debug("createCompositeMovie: composite entites created for movieId: {}", body.getMovieId());

    } catch (RuntimeException re) {
      log.warn("createCompositeMovie failed", re);
      throw re;
    }
  }

  @Override
  public MovieAggregate getMovie(int movieId) {

    Movie Movie = integration.getMovie(movieId);
    if (Movie == null) throw new NotFoundException("No Movie found for movieId: " + movieId);

    List<Recommendation> recommendations = integration.getRecommendations(movieId);

    List<Review> reviews = integration.getReviews(movieId);

    return createMovieAggregate(Movie, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  @Override
  public void deleteCompositeMovie(int movieId) {
    log.debug("deleteCompositeMovie: Deletes a movie aggregate for movieId: {}", movieId);

    integration.deleteMovie(movieId);

    integration.deleteRecommendations(movieId);

    integration.deleteReviews(movieId);

    log.debug("getCompositeMovie: aggregate entities deleted for movieId: {}", movieId);
  }

  private MovieAggregate createMovieAggregate(Movie Movie, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress) {

    // 1. Setup Movie info
    int movieId = Movie.getMovieId();
    String name = Movie.getName();
    int weight = Movie.getYear();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null :
        recommendations.stream()
            .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
            .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries = (reviews == null)  ? null :
        reviews.stream()
            .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
            .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String MovieAddress = Movie.getServiceAddress();
    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, MovieAddress, reviewAddress, recommendationAddress);

    return new MovieAggregate(movieId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}
