package orz.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import orz.zerock.ex2.entity.Memo;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MenoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample...").build();
            memoRepository.save(memo);
        });
    }

    public void testSelect() {
        long mno=100L;
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("==============================");
        if(result.isPresent()){
            Memo memo=result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        long mno=10L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("==============================");
        System.out.println(memo);
    }


    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Sample...").build();
        System.out.println(memoRepository.save(memo));
    }


    public void testDelete() {
        long mno=100L;
        memoRepository.deleteById(mno);
    }

    public void testPageDefault() {
        Pageable pageable =PageRequest.of(0, 10);
        Page<Memo> result =memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("=====================================");
        //총 몇 페이지
        System.out.println("Total Pages"+result.getTotalPages());
        System.out.println("Total count"+result.getTotalElements());

        //현재 페이지 번호 0부터 시작
        System.out.println("Page Number"+result.getNumber());
        //페이지당 데이터 개수
        System.out.println("Page Size"+result.getSize());
        //다음 페이지 존재 여부
        System.out.println("has next page?: "+result.hasNext());
        //이전 페이지 존재 여부
        System.out.println("has prev page?: "+result.hasPrevious());
        //시작 페이지(0) 여부
        System.out.println("first page?: "+result.isFirst());
        System.out.println("=====================================");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    public void testSort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "mno");
        //Sort sort=sort.by("mno").descending():
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.getContent().forEach(System.out::println);
    }

    public void testQueryMethod() {
        List<Memo>list=memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    public void testQueryWithPageable() {
        Pageable pageable = PageRequest.of(0, 10,Sort.by(Sort.Direction.DESC,"mno"));
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);
        //result.get().forEach(Memo memo -> System.out.println(memo));
        result.get().forEach(memo -> System.out.println(memo));
    }
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethod() {
        memoRepository.deleteMemoByMnoLessThan(10L);

    }
}
