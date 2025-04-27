package org.zerock.mreview.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mreview.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchMovieRepositoryImpl extends QuerydslRepositorySupport
        implements SearchMovieRepository {

    public SearchMovieRepositoryImpl() {
        super(Movie.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("SearchPage...");
        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QMember member =QMember.member;
        QReview review =QReview.review;

        JPQLQuery<Movie> jpqlQuery = from(movie);
        jpqlQuery.leftJoin(movieImage).on(movie.mno.eq(movieImage.movie.mno));
        jpqlQuery.leftJoin(review).on(review.movie.eq(movie));


        JPQLQuery<Tuple> tuple =jpqlQuery.select(movie,movieImage,review.grade.avg(),review.count());


        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = movie.mno.gt(0L);

        booleanBuilder.and(expression);

        if(type!=null){
            String[] typeArr=type.split("");

            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String s:typeArr){
                switch(s){
                    case "t":
                        conditionBuilder.or(movie.title.contains(keyword));
                        break;

                }


            }

            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);
        tuple.groupBy(movie);
        this.getQuerydsl().applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        log.info(result);
        long count=tuple.fetchCount();
        log.info(count);
        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,count);
    }
}
