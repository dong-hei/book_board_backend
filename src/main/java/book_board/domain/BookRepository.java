package book_board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 적어야 스프링 IoC 빈으로 등록되는데
//JpaRepository를 상속받으면 생략 가능
//JpaRepository는 CRUD기능을 들고온다.

public interface BookRepository extends JpaRepository<Book, Long> {
}
