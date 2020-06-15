package se.pra.reviewservice.persistence;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews", indexes = { @Index(name = "reviews_unique_idx", unique = true, columnList = "movieId,reviewId") })
public class ReviewEntity {

    @Id @GeneratedValue
    private int id;

    @Version
    private int version;

    private int movieId;
    private int reviewId;
    private String author;
    private String subject;
    private String content;

    public ReviewEntity(int movieId, int reviewId, String author, String subject, String content) {
        this.movieId = movieId;
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
        this.content = content;
    }

}
