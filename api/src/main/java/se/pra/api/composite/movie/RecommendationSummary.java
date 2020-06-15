package se.pra.api.composite.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecommendationSummary {

  private int recommendationId;
  private String author;
  private int rate;
  private String content;

  public RecommendationSummary(int recommendationId, String author, int rate) {
    this.recommendationId = recommendationId;
    this.author = author;
    this.rate = rate;
  }
}
