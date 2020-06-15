package se.pra.api.core.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {

  private int movieId;
  private String name;
  private int year;
  private String serviceAddress;

}
