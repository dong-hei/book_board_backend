package book_board.service;

import book_board.domain.Book;
import book_board.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 기능을 정의할 수 있고, 트랜잭션 관리 가능
@RequiredArgsConstructor
@Service // 스프링에 문제가 생기면 service가 메모리에 떴는지 어떻게 떴는지 확인
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    //서비스 함수가 종료될떄 commit or rollback 트랜젝션 관리하겠다는 것 이다.
    //ReadOnly 붙이는 이유, JPA에는 변경감지라는 내부 기능이 있는데 ReadOnly를 붙이면 내부기능 활성화를 안함,update시 정합성을 유지,insert의 유령데이터현상(팬텀현상) 못막음
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)

    public Book saveItem(Long id){
        return bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("아이디를 확인하세요"));
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    @Transactional
    public Book edit(Long id,Book book){
        Book bookEntity = bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("아이디를 확인하세요"));
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        //Book 오브젝트가 영속화된다.(DB에서 데이터를 가져오는것을 영속화라고 한다.)
        return bookEntity;
        //함수가 종료될때 트랜젝션이 종료되고 => 영속화 되어있는 데이터를 DB로 갱신(flush) => 이때 커밋이 된다 ====> 이것을 더티 체킹이라고 한다
        //업데이트 할때는 더티체킹을 하는게 좋다.
    }

    @Transactional
    public String delete(Long id){
        bookRepository.deleteById(id);
        return "ok"; //오류가 터지면 익셉션타야하니까 신경쓸 필요 없다.
    }

}
