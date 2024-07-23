package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
select m, 
(select mi2.imgName from MovieImage mi2 where mi2.movie = m order by mi2.inum desc limit 1), 
avg(coalesce(r.grade,0)), count(distinct r) from Movie m
left outer join MovieImage mi on mi.movie = m 
left outer join Review r on r.movie = m 
group by m 
""")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("""
select m, mi, avg(coalesce(r.grade,0)), count(distinct(r)) from Movie m 
left outer join MovieImage mi on mi.movie = m
left outer join Review r on r.movie = m
where m.mno = :mno group by mi
""")
    List<Object[]> getMovieWithAll(Long mno);
}
