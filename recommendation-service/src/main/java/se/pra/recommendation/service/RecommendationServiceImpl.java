package se.pra.recommendation.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import se.pra.api.core.recommentation.Recommendation;
import se.pra.api.core.recommentation.RecommendationService;
import se.pra.recommendation.persistence.RecommendationEntity;
import se.pra.recommendation.persistence.RecommendationRepository;
import se.pra.util.exception.InvalidInputException;
import se.pra.util.http.ServiceUtil;

@Slf4j
@RestController
public class RecommendationServiceImpl implements RecommendationService {

  private final ServiceUtil serviceUtil;
  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;

  public RecommendationServiceImpl(ServiceUtil serviceUtil,
      RecommendationRepository repository,
      RecommendationMapper mapper) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    try {
      RecommendationEntity entity = mapper.apiToEntity(body);
      RecommendationEntity newEntity = repository.save(entity);

      log.debug("createRecommendation: created a recommendation entity: {}/{}", body.getMovieId(), body.getRecommendationId());
      return mapper.entityToApi(newEntity);

    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Movie Id: " + body.getMovieId() + ", Recommendation Id:" + body.getRecommendationId());
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int movieId) {

    if (movieId < 0) throw new InvalidInputException("Invalid movieId: " + movieId);

    List<RecommendationEntity> entityList = repository.findByMovieId(movieId);
    List<Recommendation> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("getRecommendations: response size: {}", list.size());

    return list;
  }

  @Override
  public void deleteRecommendations(int movieId) {
    log.debug("deleteRecommendations: tries to delete recommendations for the movie with movieId: {}", movieId);
    repository.deleteAll(repository.findByMovieId(movieId));
  }
}