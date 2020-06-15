package se.pra.reviewservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import se.pra.api.core.review.Review;
import se.pra.api.core.review.ReviewService;
import se.pra.reviewservice.persistence.ReviewEntity;
import se.pra.reviewservice.persistence.ReviewRepository;
import se.pra.util.exception.InvalidInputException;
import se.pra.util.http.ServiceUtil;

@Slf4j
@RestController
public class ReviewServiceImpl implements ReviewService {

  private final ServiceUtil serviceUtil;
  private final ReviewRepository repository;
  private final ReviewMapper mapper;

  public ReviewServiceImpl(ServiceUtil serviceUtil,
      ReviewRepository repository, ReviewMapper mapper) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Review createReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      log.debug("createReview: created a review entity: {}/{}", body.getMovieId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, movie Id: " + body.getMovieId() + ", Review Id:" + body.getReviewId());
    }
  }

  @Override
  public List<Review> getReviews(int movieId) {

    if (movieId < 0) throw new InvalidInputException("Invalid movieId: " + movieId);

    List<ReviewEntity> entityList = repository.findByMovieId(movieId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("getReviews: response size: {}", list.size());

    return list;
  }

  @Override
  public void deleteReviews(int movieId) {
    log.debug("deleteReviews: tries to delete reviews for the movie with movieId: {}", movieId);
    repository.deleteAll(repository.findByMovieId(movieId));
  }
}
