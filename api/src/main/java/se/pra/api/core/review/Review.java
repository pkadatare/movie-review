package se.pra.api.core.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {

  private int movieId;
  private int reviewId;
  private String author;
  private String subject;
  private String content;
  private String serviceAddress;

}
