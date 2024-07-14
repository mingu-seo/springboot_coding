package org.zerock.ex2.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import java.util.Collection;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>, QuerydslPredicateExecutor<Memo> {
    Memo findByMemoText(String memo);
    Collection<Memo> findByMemoTextContaining(String memo);
    List<Memo> findByMemoTextContaining(String memo, Pageable pageable);
    Collection<Memo> findByMemoTextContainingOrMemoTextContaining(String text1, String text2);
    Collection<Memo> findByOrderByMnoDesc();
    List<Memo> findByOrderByMnoDesc(Pageable paging);

//    @Query("select m from Memo m where memoText like %?1% and mno > 0 order by mno desc")
//    List<Memo> findMemoByMemoText(String memo);
    @Query("select m from Memo m where memoText like %:memo% and mno > 0 order by mno desc")
    List<Memo> findMemoByMemoText(@Param("memo") String memo);

    @Query(value="select * from tbl_memo where memo_text like concat('%', ?1, '%') and mno > 0 order by mno desc", nativeQuery=true)
    List<Memo> findMemoByMemoText2(String memo);

    @Query(value="select * from tbl_memo where memo_text like concat('%', ?1, '%') and mno > 0 order by mno desc", nativeQuery=true)
    List<Memo> findByPage(Pageable pageable, String title);
}
