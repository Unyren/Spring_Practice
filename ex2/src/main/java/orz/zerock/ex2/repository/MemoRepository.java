package orz.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import orz.zerock.ex2.entity.Memo;

import java.util.List;


public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByMnoBetweenOrderByMnoDesc(long from, long to);

    Page<Memo> findByMnoBetween(long mnoAfter, long mnoBefore, Pageable pageable);

    void deleteMemoByMnoLessThan(long mno);
}
