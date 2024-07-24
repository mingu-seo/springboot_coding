package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieImageRepository imageRepository;

//    @Commit // 테스트코드에서 자동 롤백되는 경우
//    @Transactional
//    @Test
    public void insertMoviews() {
        IntStream.rangeClosed(1,100).forEach(i->{
            Movie movie = Movie.builder().title("Movie..."+i).build();
            System.out.println("----------------------");
            movieRepository.save(movie);

            int count = (int)(Math.random()*5)+1;
            for (int j=0; j<count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test"+j+".jpg").build();
                imageRepository.save(movieImage);
            }
            System.out.println("=========================");
        });
    }

    @Test
    public void testListPage() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "mno"));
        Page<Object[]> result = movieRepository.getListPage(pageRequest);
        for (Object[] objects : result.getContent()) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void testGetMovieWithAll() {
        List<Object[]> result = movieRepository.getMovieWithAll(97L);
        System.out.println(result);
        for (Object[] arr: result) {
            System.out.println(Arrays.toString(arr));
        }
    }
}
