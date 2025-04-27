package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;



    @Test
    public void testGetMovieReviews() {
        Movie movie= Movie.builder()
                .mno(95L)
                .build();
        List<Review> result=reviewRepository.findByMovie(movie);

        result.forEach(movieReview->{
            System.out.println(movieReview.getMember().getEmail());
        });
    }

    public void insertReviews() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            //영화. pk
            Long mno=(long)(Math.random()*100)+1;
            //회원 pk
            Long mid=(long)(Math.random()*100)+1;


            Movie movie = Movie.builder()
                    .mno(mno)
                    .build();


            Member member = Member.builder()
                    .mid(mid)
                    .build();

            Review review= Review.builder()
                    .member(member)
                    .movie(movie)
                    .grade((int)(Math.random()*10)+1)
                    .text("이 영화에 대한 느낌은..."+i)
                    .build();
            reviewRepository.save(review);

        });
    }
}
