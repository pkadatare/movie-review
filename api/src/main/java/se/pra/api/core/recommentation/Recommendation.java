package se.pra.api.core.recommentation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recommendation {

  private int movieId;
  private int recommendationId;
  private String author;
  private int rate;
  private String content;
  private String serviceAddress;

}
