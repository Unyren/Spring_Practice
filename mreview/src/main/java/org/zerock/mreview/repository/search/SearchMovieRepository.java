package org.zerock.mreview.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMovieRepository {
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
