package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
