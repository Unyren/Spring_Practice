package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.repository.search.SearchMovieRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> , SearchMovieRepository{
   @Query(" select m, mi ,avg (coalesce(r.grade,0)), count(distinct r) from Movie m "+
           " left outer join Review r on r.movie=m " +
            " left outer join MovieImage mi on mi.movie= m group by m ")
   Page<Object[]> getListPage(Pageable pageable);//페이지 처리

    @Query(" select m, mi,avg(coalesce (r.grade,0) ),count(distinct r)  from Movie m "+
            " left outer join MovieImage mi on mi.movie= m "+
            " left outer join Review r on r.movie= m "+
            " where m.mno=:mno group by mi")
    //특정영화 처리
    List<Object[]> getMovieWithAll(@Param("mno") Long mno);

}
