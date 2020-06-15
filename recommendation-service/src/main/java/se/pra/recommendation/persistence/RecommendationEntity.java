package se.pra.recommendation.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection="recommendations")
@CompoundIndex(name = "prod-rec-id", unique = true, def = "{'movieId': 1, 'recommendationId' : 1}")
public class RecommendationEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    private int movieId;
    private int recommendationId;
    private String author;
    private int rating;
    private String content;

    public RecommendationEntity(int movieId, int recommendationId, String author, int rating, String content) {
        this.movieId = movieId;
        this.recommendationId = recommendationId;
        this.author = author;
        this.rating = rating;
        this.content = content;
    }


}
