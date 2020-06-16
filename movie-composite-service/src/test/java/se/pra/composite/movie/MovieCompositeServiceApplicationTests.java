package se.pra.composite.movie;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.pra.api.core.movie.Movie;
import se.pra.api.core.recommentation.Recommendation;
import se.pra.api.core.review.Review;
import se.pra.composite.movie.service.MovieCompositeIntegration;
import se.pra.util.exception.InvalidInputException;
import se.pra.util.exception.NotFoundException;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false"})
class MovieCompositeServiceApplicationTests {

	private static final int MOVIE_ID_OK = 1;
	private static final int MOVIE_ID_NOT_FOUND = 2;
	private static final int MOVIE_ID_INVALID = 3;

	@Autowired
	private WebTestClient client;

	@MockBean
	private MovieCompositeIntegration compositeIntegration;

	@BeforeEach
	public void setUp() {

		when(compositeIntegration.getMovie(MOVIE_ID_OK)).
				thenReturn(new Movie(MOVIE_ID_OK, "name", 1, "mock-address"));
		when(compositeIntegration.getRecommendations(MOVIE_ID_OK)).
				thenReturn(singletonList(new Recommendation(MOVIE_ID_OK, 1, "author", 1, "content", "mock address")));
		when(compositeIntegration.getReviews(MOVIE_ID_OK)).
				thenReturn(singletonList(new Review(MOVIE_ID_OK, 1, "author", "subject", "content", "mock address")));

		when(compositeIntegration.getMovie(MOVIE_ID_NOT_FOUND)).thenThrow(new NotFoundException("NOT FOUND: " + MOVIE_ID_NOT_FOUND));

		when(compositeIntegration.getMovie(MOVIE_ID_INVALID)).thenThrow(new InvalidInputException("INVALID: " + MOVIE_ID_INVALID));
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void getMovieById() {

		client.get()
				.uri("/movie-composite/" + MOVIE_ID_OK)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.movieId").isEqualTo(MOVIE_ID_OK)
				.jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1);
	}

	@Test
	public void getMovieNotFound() {

		client.get()
				.uri("/movie-composite/" + MOVIE_ID_NOT_FOUND)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/movie-composite/" + MOVIE_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("NOT FOUND: " + MOVIE_ID_NOT_FOUND);
	}

	@Test
	public void getMovieInvalidInput() {

		client.get()
				.uri("/movie-composite/" + MOVIE_ID_INVALID)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/movie-composite/" + MOVIE_ID_INVALID)
				.jsonPath("$.message").isEqualTo("INVALID: " + MOVIE_ID_INVALID);
	}

}
