package se.pra.reviewservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment=RANDOM_PORT)
class ReviewServiceApplicationTests {

	@Autowired
	private WebTestClient client;

	@Test
	public void getReviewsByMovieId() {

		int movieId = 1;

		client.get()
				.uri("/review?movieId=" + movieId)
				.accept(APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(3)
				.jsonPath("$[0].movieId").isEqualTo(movieId);
	}

	@Test
	public void getReviewsMissingParameter() {

		client.get()
				.uri("/review")
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/review")
				.jsonPath("$.message").isEqualTo("Required int parameter 'movieId' is not present");
	}

	@Test
	public void getReviewsInvalidParameter() {

		client.get()
				.uri("/review?movieId=no-integer")
				.accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/review")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getReviewsNotFound() {

		int movieIdNotFound = 213;

		client.get()
				.uri("/review?movieId=" + movieIdNotFound)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	public void getReviewsInvalidParameterNegativeValue() {

		int movieIdInvalid = -1;

		client.get()
				.uri("/review?movieId=" + movieIdInvalid)
				.accept(MediaType.valueOf(APPLICATION_JSON_VALUE))
				.exchange()
				.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/review")
				.jsonPath("$.message").isEqualTo("Invalid movieId: " + movieIdInvalid);
	}

}
