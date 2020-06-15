package se.pra.movie.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.pra.api.core.movie.Movie;
import se.pra.movie.persistence.MovieEntity;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })
    Movie entityToApi(MovieEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    MovieEntity apiToEntity(Movie api);
}
