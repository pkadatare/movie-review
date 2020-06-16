package se.pra.recommendation;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"eureka.client.enabled=false"})
class RecommendationServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Test
	public void getRecommendationsByMovieId(
			MediaType APPLICATION_JSON_VALUE) {

		int movieId = 1;

		client.get()
				.uri("/recommendation?movieId=" + movieId)
				.accept(APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(3)
				.jsonPath("$[0].movieId").isEqualTo(movieId);
	}

	@Test
	public void getRecommendationsMissingParameter() {

		client.get()
				.uri("/recommendation")
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/recommendation")
				.jsonPath("$.message").isEqualTo("Required int parameter 'movieId' is not present");
	}

	@Test
	public void getRecommendationsInvalidParameter() {

		client.get()
				.uri("/recommendation?movieId=no-integer")
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/recommendation")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getRecommendationsNotFound() {

		int movieIdNotFound = 113;

		client.get()
				.uri("/recommendation?movieId=" + movieIdNotFound)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	public void getRecommendationsInvalidParameterNegativeValue() {

		int movieIdInvalid = -1;

		client.get()
				.uri("/recommendation?movieId=" + movieIdInvalid)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/recommendation")
				.jsonPath("$.message").isEqualTo("Invalid movieId: " + movieIdInvalid);
	}

}
