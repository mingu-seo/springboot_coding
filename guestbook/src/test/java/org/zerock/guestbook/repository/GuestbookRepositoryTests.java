package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {
    @Autowired
    private GuestbookRepository repo;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..."+i)
                    .content("Cotnent..."+i)
                    .writer("user"+(i%10))
                    .build();
            System.out.println(repo.save(guestbook));
        });
    }

    @Test
    public void updateTest() {
        Optional<Guestbook> result = repo.findById(300L);

        if (result.isPresent()) {
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");
            repo.save(guestbook);
        }
    }

    @Test
    public void testQuerydsl1() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.title.contains(keyword);
        builder.and(expression).or(qGuestbook.title.like("2")); // and, or 추가 가능
        Page<Guestbook> result = repo.findAll(builder, pageable);
        result.stream().forEach(gb -> {
            System.out.println(gb);
        });
    }


}
