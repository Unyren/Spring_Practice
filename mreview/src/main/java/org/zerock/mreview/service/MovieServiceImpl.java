package org.zerock.mreview.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    //의존성 주입
    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {
        Map<String, Object> entityMap= dtoToEntity(movieDTO);
        Movie movie = (Movie)entityMap.get("movie");
        List<MovieImage>movieImageList=(List<MovieImage>)entityMap.get("imgList");

        movieRepository.save(movie);//영화 게시글 저장

        movieImageList.forEach(movieImage->{
            imageRepository.save(movieImage);//첨부된 이미지 db에 파일 저장
        });
        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        //영화글번호 역순정렬
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        //Page<Object[]> result= movieRepository.getListPage(pageable);
        Page<Object[]> result=movieRepository.searchPage(
                requestDTO.getType(),
                requestDTO.getKeyword(),
                pageable);

        Function<Object[],MovieDTO>fn=(arr->entitiesToDTO(
                (Movie)arr[0],
                (List<MovieImage>) (Arrays.asList((MovieImage)arr[1])),
                (double) (arr[2]==null?0.0:arr[2]),
                (Long)arr[3])
        );


        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {
        List<Object[]> result= movieRepository.getMovieWithAll(mno);

        //Movie 엔티티는 가장 앞에 존재하는 모든 Row가 동일한 값
        Movie movie = (Movie)result.get(0)[0];

        //영화의 이미지 개수만큼 MovieImage 객체필요
        List<MovieImage>movieImageList=new ArrayList<>();

        result.forEach(arr->{
           MovieImage movieImage=(MovieImage) arr[1];
           movieImageList.add(movieImage);
        });
        Double avg=(Double)result.get(0)[2];

        Long reviewCnt = (Long)result.get(0)[3];

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }

    @Transactional
    @Override
    public void modify(MovieDTO dto) {
        Movie movie=movieRepository.getReferenceById(dto.getMno());
        Map<String, Object> entityMap= dtoToEntity(dto);

        if(movie!=null) {
            movie.chageTitle(dto.getTitle());
            movieRepository.save(movie);//영화 게시글 저장
            //기존 그림파일 삭제
            imageRepository.deleteByMno(dto.getMno());
            //첨부된 그림파일 저장.
            List<MovieImage>movieImageList=(List<MovieImage>)entityMap.get("imgList");
            movieImageList.forEach(movieImage->{
                imageRepository.save(movieImage);//첨부된 이미지 db에 파일 저장
            });

            movieRepository.save(movie);
        }
    }
    @Transactional
    @Override
    public void removeWithMovieAll(Long mno) {
        imageRepository.deleteByMno(mno);//영화게시글관련 이미지 삭제
        reviewRepository.deleteByMovie(mno);//영화게시글관련 리뷰 삭제
        movieRepository.deleteById(mno);//영화 게시글 삭제
    }
}
