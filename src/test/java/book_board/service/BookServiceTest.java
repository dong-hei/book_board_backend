package book_board.service;

import book_board.domain.Book;
import book_board.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * 단위 테스트(서비스 관련 기능만 IoC에 띄운다.)
 * 그런데 생각해보자 Service를 테스트하기위해서는 bookRepository를 의존관계 주입을 받아야한다.
 *  즉 통합테스트를 해야하는데 나는 단위테스트를 하고싶다 이럴때는 어떻게 해야할까?
 *  @ExtendWith(MockitoExtension.class) Mockito 환경으로 바꿔준다.
 */

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @InjectMocks // BookService 객체가 만들어 질때 해당 파일에 @Mock로 등록된 모든 의존관계들을 주입받는다.
    private BookService bookService;

    @Mock // Mock안에 가짜 객체로 주입받는다.
    private BookRepository bookRepository;
    

    @Test
    void save() {
        //given
        Book book = new Book();
        book.setTitle("첵제목!");
        book.setAuthor("책저자");

        //stub - 동작지정
        when(bookRepository.save(book)).thenReturn(book);

        //text execute
        Book bookEntity = bookService.save(book);

        //then
        assertEquals(bookEntity, book);

    }

    @Test
    void saveItem() {
    }

    @Test
    void findAll() {
    }

    @Test
    void edit() {
    }

    @Test
    void delete() {
    }
}