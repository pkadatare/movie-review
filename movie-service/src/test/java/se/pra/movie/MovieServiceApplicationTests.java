package se.pra.movie;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment=RANDOM_PORT)
public class MovieServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Test
	public void getMovieById() {

		int movieId = 1;

		client.get()
				.uri("/movie/" + movieId)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.movieId").isEqualTo(movieId);
	}

	@Test
	public void getMovieInvalidParameterString() {

		client.get()
				.uri("/movie/no-integer")
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/movie/no-integer")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getMovieNotFound() {

		int movieIdNotFound = 13;

		client.get()
				.uri("/movie/" + movieIdNotFound)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/movie/" + movieIdNotFound)
				.jsonPath("$.message").isEqualTo("No movie found for movieId: " + movieIdNotFound);
	}

	@Test
	public void getMovieInvalidParameterNegativeValue() {

		int movieIdInvalid = -1;

		client.get()
				.uri("/movie/" + movieIdInvalid)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/movie/" + movieIdInvalid)
				.jsonPath("$.message").isEqualTo("Invalid movieId: " + movieIdInvalid);
	}


}
