package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; // 반드시 final로 선언

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO--------------");
        log.info(dto);
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        // querydsl 처리 후 수정
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        // 앞 매개변수에 추가
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if (result.isPresent()) {
            Guestbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }

    // 검색관련 querydsl(동적쿼리)
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno  > 0
        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        // 검색조건
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }
        // 모든조건통합
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}
