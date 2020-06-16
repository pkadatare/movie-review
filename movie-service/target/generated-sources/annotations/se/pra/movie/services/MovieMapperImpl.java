package se.pra.movie.services;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import se.pra.api.core.movie.Movie;
import se.pra.movie.persistence.MovieEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-15T21:43:44+0530",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie entityToApi(MovieEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setMovieId( entity.getMovieId() );
        movie.setName( entity.getName() );

        return movie;
    }

    @Override
    public MovieEntity apiToEntity(Movie api) {
        if ( api == null ) {
            return null;
        }

        MovieEntity movieEntity = new MovieEntity();

        movieEntity.setMovieId( api.getMovieId() );
        movieEntity.setName( api.getName() );

        return movieEntity;
    }
}
