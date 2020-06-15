package se.pra.api.composite.movie;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewSummary {

  private int reviewId;
  private String author;
  private String subject;
  private String content;

  public ReviewSummary(int reviewId, String author, String subject) {
    this.reviewId = reviewId;
    this.author = author;
    this.subject = subject;
  }
}
