package org.zerock.ex2.repository;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;
import org.zerock.ex2.entity.QMemo;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1,100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("=============================================================");
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }

    }

    @Transactional
//    @Test
    public void testSelect2() {
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("=============================================================");
        System.out.println(memo);

    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

    // memo_text로 조회
    @Test
    public void testSearch() {
//        Memo memo = memoRepository.findByMemoText("Sample...30");
//        System.out.println(memo);
    }

    @Test
    public void testSearch2() {
        memoRepository.findByMemoTextContaining("5").forEach(memo->System.out.println(memo));
    }

    @Test
    public void testSearch3() {
        memoRepository.findByMemoTextContainingOrMemoTextContaining("5", "1").forEach(memo->System.out.println(memo));
    }

    @Test
    public void testSearch4() {
        memoRepository.findByOrderByMnoDesc().forEach(memo->System.out.println(memo));
    }

    @Test
    public void testPageDefault() {
        Pageable paging = PageRequest.of(0, 10); // 페이지번호, 페이당개수
        Page<Memo> result = memoRepository.findAll(paging);
        System.out.println(result);
        System.out.println("----------------------------------------");
        System.out.println("Total Pages: " + result.getTotalPages()); // 총페이지수
        System.out.println("Total Count: " + result.getTotalElements()); // 총개수
        System.out.println("Page Number: " + result.getNumber()); // 현재페이지번호
        System.out.println("Page Size: " + result.getSize()); // 페이지당 개수
        System.out.println("has next page: " + result.hasNext()); // 다음페이지 존재여부
        System.out.println("first page:"+result.isFirst()); // 시작페이지 여부
        System.out.println("----------------------------------------");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }
    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    // 추가
    @Test
    public void testPaging() {
        Pageable paging = PageRequest.of(0, 10); // 페이지번호, 페이당개수
        memoRepository.findByOrderByMnoDesc(paging).forEach(memo->System.out.println(memo));
    }
    @Test
    public void testByMemoTextContainingByPaging() {
        //Pageable paging = PageRequest.of(0, 10, Sort.Direction.DESC, "mno");
        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno").and(Sort.by(Sort.Direction.ASC, "memoText")));
        memoRepository.findByMemoTextContaining("5", paging).forEach(memo->System.out.println(memo));
    }

    @Test
    public void testByMemo() {
        memoRepository.findMemoByMemoText("5").forEach(memo->System.out.println(memo));
    }
    @Test
    public void testByMemo2() {
        memoRepository.findMemoByMemoText2("5").forEach(memo->System.out.println(memo));
    }

    @Test
    public void testByPage() {
        Pageable paging = PageRequest.of(0, 10); // 페이지번호, 페이당개수
        memoRepository.findByPage(paging,"5").forEach(memo->System.out.println(memo));
    }

    // querydsl 테스트
    @Test
    public void testPredicate() {
        String type = "t";
        String keyword = "5";

        BooleanBuilder builder = new BooleanBuilder();
        QMemo memo = QMemo.memo;
        if (type.equals("t")) {
            builder.and(memo.memoText.like("%"+keyword+"%"));
        }
        builder.and(memo.mno.gt(0));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(builder, pageable);

        System.out.println("PAGE SIZE:"+result.getSize());
        System.out.println("TOTAL PAGES:"+result.getTotalPages());
        System.out.println("TOTAL COUNT:"+result.getTotalElements());
        System.out.println("NEXT:"+result.nextPageable());

        result.get().forEach(m->System.out.println(m));
    }
}
