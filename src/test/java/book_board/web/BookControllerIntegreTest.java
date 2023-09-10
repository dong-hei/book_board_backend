package book_board.web;


import book_board.domain.Book;
import book_board.domain.BookRepository;
import book_board.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트 (컨트롤러로 전체 스프링을 테스트 한다, 모든 Bean을 IoC에 올리고 테스트)
 * WebEnvironment.Mock = 실제 톰캣을 올리는게 아니라 다른 톰캣으로 테스트
 * WebEnvironment.RANDOM_PORT = 실제 톰캣으로 테스트
 * @AutoConfigureMockMvc MockMvc를 IoC에 등록
 * @Transactional 각각의 테스트함수가 종료될 때마다 트랜잭션을 rollback 해주는 어노테이션!
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //모든게 메모리에 다 뜬다.
public class BookControllerIntegreTest {

// Mockito :  테스트할 수 있는 라이브러리
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

//    @MockBean  BookService가 IoC환경에 등록된다.
//    private BookService bookService; 진짜가 있으니까 굳이 가짜 객체를 주입할 필요가 없다

    // 테스트 실행 직전 할일
    @BeforeEach
    public void init(){
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @AfterEach
    public void end(){
        bookRepository.deleteAll();
    }

    //BDDMockito 패턴
    @Test
    public void save() throws Exception {
        //given (테스트 준비)
        Book book = new Book(null, "자바스프링", "김영한");
        String content = new ObjectMapper().writeValueAsString(book);
//        when(bookService.save(book)).thenReturn(new Book(1L, "자바스프링", "김영한")); 실제 서비스가 실행되기때문에 필요 없다.

        //when (테스트 실행)
        ResultActions resultActions = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        //then (검증)
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("자바스프링"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findAll() throws Exception {
        //given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"자바스프링","김영한"));
        books.add(new Book(2L,"Clean Code", "클린코드"));
        books.add(new Book(3L,"노르웨이 숲", "무라카미 하루키"));
        books.add(new Book(4L,"최저", "사쿠나 마나"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        //Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[3].title").value("최저"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findById() throws Exception {
        //given
        Long id = 4L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"자바스프링","김영한"));
        books.add(new Book(2L,"Clean Code", "클린코드"));
        books.add(new Book(3L,"노르웨이 숲", "무라카미 하루키"));
        books.add(new Book(4L,"최저", "사쿠나 마나"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("최저"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update() throws Exception {
        //given
        Long id = 4L;
        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"자바스프링","김영한"));
        books.add(new Book(null,"비밀","히가시노 게이고"));
        books.add(new Book(null,"달러구트 백화점","이미예"));
        books.add(new Book(null,"달러구트 백화점 2","이미예"));
        bookRepository.saveAll(books);

        Book book = new Book(null,"1Q84","무라카미 하루키");
        String content = new ObjectMapper().writeValueAsString(book);

        //when
        ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.title").value("1Q84"))
                .andDo(MockMvcResultHandlers.print());
    }


}
