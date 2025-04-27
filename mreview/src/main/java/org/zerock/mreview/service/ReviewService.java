package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    //영화의 모든 영화리뷰를 가져온다.
    List<ReviewDTO> getListOfMovie(Long mno);
    //영화 리뷰를 추가
    Long register(ReviewDTO movieReviewDTO);
    //특정한 영화리뷰 수정
    void modify(ReviewDTO movieReviewDTO);
    //영화 리뷰를 삭제
    void remove(Long reviewnum);
    //dtoToEntity
    default Review dtoToEntity(ReviewDTO movieReviewDTO) {
       Movie movie=Movie.builder()
               .mno(movieReviewDTO.getMno())
               .build();
       Member member=Member.builder()
               .mid(movieReviewDTO.getMid())
               .build();
       Review movieReview=Review.builder()
               .reviewnum(movieReviewDTO.getReviewnum())
               .movie(movie)
               .member(member)
               .grade(movieReviewDTO.getGrade())
               .text(movieReviewDTO.getText())
               .build();
       return movieReview;
    }
    //entityToDto
    default ReviewDTO entityToDto(Review movieReview) {
        ReviewDTO movieReviewDTO=ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getNickName())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return movieReviewDTO;
    }
}
