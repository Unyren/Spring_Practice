package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository imageRepository;



    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object[]> result =  movieRepository.searchPage("t","1",pageable);
    }

    public void testGetMovieWithAll(){
        List<Object[]> result= movieRepository.getMovieWithAll(95L);
        System.out.println(result);
        for(Object[] row:result){
            System.out.println(Arrays.toString(row));
        }
    }

    public void testListPage(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
        Page<Object[]> result=movieRepository.getListPage(pageRequest);
        for(Object[] objects:result.getContent()){
            System.out.println(Arrays.toString(objects));
        }
    }
    @Transactional
    @Commit
    public void insertMovies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie= Movie.builder()
                    .title("Movie..."+i)
                    .build();
            movieRepository.save(movie);

            int count=(int)(Math.random()*5)+1;

            for(int j=0;j<count;j++){
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test"+j+".jpg")
                        .build();
                imageRepository.save(movieImage);
            }
        });
    }
}
