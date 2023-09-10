package book_board.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//단위 테스트(DB 관련 Bean이 IoC에 등록되면 된다.)

/**@AutoConfigureTestDatabase DB를 가상으로 만들고싶을때
 * Replace.ANY 가상의 DB로 테스트
 * Replace.NONE 실제 DB로 테스트 (단위 테스트이기때문에 사용할 필요가 없다.)
 * @DataJpaTest JPA관련된 객체만 메모리에 뜬다. (Repository들을 다 IoC에 등록해둔다.)
 */

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
class BookRepositoryUnitTest {
    private BookRepository bookRepository;

    @Test
    void save(){
        //given
        Book book = new Book(null,"title","author");

        //when
        Book bookEntity = bookRepository.save(book);

        //then
        assertEquals("title",bookEntity.getTitle());
    }

}