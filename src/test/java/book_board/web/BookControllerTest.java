package book_board.web;

import book_board.domain.Book;
import book_board.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//단위 테스트 (컨트롤러만! 테스트,Controller 관련 로직만 띄운다. Filter,ControllerAdvice 등)

@Slf4j
@WebMvcTest //Junit 테스트를 Spring 환경에서 테스트 하고싶을때 확장할수있는 어노테이션을 포함
class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMVc;

    @MockBean // BookService가 IoC환경에 등록된다.
    private BookService bookService;

    //BDDMockito 패턴
    @Test
    public void save() throws Exception {
        //given (테스트 준비)
        Book book = new Book(null, "자바스프링", "김영한");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.save(book)).thenReturn(new Book(1L, "자바스프링", "김영한"));

        //when (테스트 실행)
        ResultActions resultActions = mockMVc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON));
        
        //then (검증)
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("자바스프링"))
                .andDo(MockMvcResultHandlers.print());

        //jsonPath에서 $는 전체를 의미한다 $.store.book[3:] (스토어의 책 경로에서 3번 뒤에있는 모든 DB를 보고싶다.)
    }

    @Test
    void findAll() throws Exception {
        //given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"자바스프링","김영한"));
        books.add(new Book(2L,"Clean Code", "클린코드"));
        when(bookService.findAll()).thenReturn(books);

        //when
        ResultActions resultActions = mockMVc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("자바스프링"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findById() throws Exception {
        //given
        Long id = 1L;
        when(bookService.saveItem(id)).thenReturn(new Book(1L,"자바스프링","김영한"));

        //when
        ResultActions resultActions = mockMVc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바스프링"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void update() throws Exception {
        //given
        Long id = 1L;
        Book book = new Book(null, "Spring JPA", "김영한");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.edit(id,book)).thenReturn(new Book(1L, "Spring JPA", "김영한"));

        //when
        ResultActions resultActions = mockMVc.perform(MockMvcRequestBuilders.put("/book/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring JPA"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void delete() throws Exception {
        //given
        Long id = 1L;

        when(bookService.delete(id)).thenReturn("ok");

        //when
        ResultActions resultActions = mockMVc.perform(MockMvcRequestBuilders.delete("/book/{id}",id));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult requestResult = resultActions.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        assertEquals("ok",result);
    }
}