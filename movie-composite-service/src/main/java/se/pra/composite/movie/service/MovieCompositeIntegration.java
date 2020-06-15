package se.pra.composite.movie.service;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.pra.api.core.movie.Movie;
import se.pra.api.core.movie.MovieService;
import se.pra.api.core.recommentation.Recommendation;
import se.pra.api.core.recommentation.RecommendationService;
import se.pra.api.core.review.Review;
import se.pra.api.core.review.ReviewService;
import se.pra.util.exception.InvalidInputException;
import se.pra.util.exception.NotFoundException;
import se.pra.util.http.HttpErrorInfo;

@Slf4j
@Component
public class MovieCompositeIntegration implements MovieService, ReviewService,
    RecommendationService {

  private RestTemplate restTemplate;
  private ObjectMapper mapper;

  private String movieServiceUrl;
  private String reviewServiceUrl;
  private String recommendationServiceUrl;

  @Autowired
  public MovieCompositeIntegration(
      RestTemplate restTemplate,
      ObjectMapper mapper,

      @Value("${app.movie-service.host}") String movieServiceHost,
      @Value("${app.movie-service.port}") int movieServicePort,

      @Value("${app.recommendation-service.host}") String recommendationServiceHost,
      @Value("${app.recommendation-service.port}") int recommendationServicePort,

      @Value("${app.review-service.host}") String reviewServiceHost,
      @Value("${app.review-service.port}") int reviewServicePort
  ) {

    this.restTemplate = restTemplate;
    this.mapper = mapper;

    movieServiceUrl = "http://" + movieServiceHost + ":" + movieServicePort + "/movie/";
    recommendationServiceUrl =
        "http://" + recommendationServiceHost + ":" + recommendationServicePort
            + "/recommendation?movieId=";
    reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?movieId=";
  }

  @Override
  public Movie createMovie(Movie body) {

    try {
      String url = movieServiceUrl;
      log.debug("Will post a new movie to URL: {}", url);

      Movie movie = restTemplate.postForObject(url, body, Movie.class);
      log.debug("Created a movie with id: {}", movie.getMovieId());

      return movie;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Movie getMovie(int movieId) {

    try {
      String url = movieServiceUrl +  movieId;
      log.debug("Will call the getMovie API on URL: {}", url);

      Movie movie = restTemplate.getForObject(url, Movie.class);
      log.debug("Found a movie with id: {}", movie.getMovieId());

      return movie;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public void deleteMovie(int movieId) {
    try {
      String url = movieServiceUrl + "/" + movieId;
      log.debug("Will call the deleteMovie API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {

    try {
      String url = recommendationServiceUrl;
      log.debug("Will post a new recommendation to URL: {}", url);

      Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
      log.debug("Created a recommendation with id: {}", recommendation.getMovieId());

      return recommendation;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int movieId) {

    try {
      String url = recommendationServiceUrl + movieId;

      log.debug("Will call the getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations = restTemplate
          .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
          }).getBody();

      log.debug("Found {} recommendations for a movie with id: {}", recommendations.size(),
          movieId);
      return recommendations;

    } catch (Exception ex) {
      log.warn("Got an exception while requesting recommendations, return zero recommendations: {}",
          ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteRecommendations(int movieId) {
    try {
      String url = recommendationServiceUrl + movieId;
      log.debug("Will call the deleteRecommendations API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Review createReview(Review body) {

    try {
      String url = reviewServiceUrl;
      log.debug("Will post a new review to URL: {}", url);

      Review review = restTemplate.postForObject(url, body, Review.class);
      log.debug("Created a review with id: {}", review.getMovieId());

      return review;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public void deleteReviews(int movieId) {
    try {
      String url = reviewServiceUrl + movieId;
      log.debug("Will call the deleteReviews API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public List<Review> getReviews(int movieId) {

    try {
      String url = reviewServiceUrl + movieId;

      log.debug("Will call the getReviews API on URL: {}", url);
      List<Review> reviews = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {}).getBody();

      log.debug("Found {} reviews for a product with id: {}", reviews.size(), movieId);
      return reviews;

    } catch (Exception ex) {
      log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
    switch (ex.getStatusCode()) {

      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(ex));

      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(ex));

      default:
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }
}
