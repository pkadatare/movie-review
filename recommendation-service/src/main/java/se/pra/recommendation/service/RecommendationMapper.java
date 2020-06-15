package se.pra.recommendation.service;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.pra.api.core.recommentation.Recommendation;
import se.pra.recommendation.persistence.RecommendationEntity;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mappings({
        @Mapping(target = "rate", source="entity.rating"),
        @Mapping(target = "serviceAddress", ignore = true)
    })
    Recommendation entityToApi(RecommendationEntity entity);

    @Mappings({
        @Mapping(target = "rating", source="api.rate"),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    RecommendationEntity apiToEntity(Recommendation api);

    List<Recommendation> entityListToApiList(List<RecommendationEntity> entity);
    List<RecommendationEntity> apiListToEntityList(List<Recommendation> api);
}