package book_board.web;

import book_board.domain.Book;
import book_board.service.BookService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book){
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

//    나중에 리턴할게 뭔지 모르기때문에 <?>
    @GetMapping("/book")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return new ResponseEntity<>(bookService.saveItem(id), HttpStatus.OK);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book){
        return new ResponseEntity<>(bookService.edit(id, book), HttpStatus.OK);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return new ResponseEntity<>(bookService.delete(id), HttpStatus.OK);
    }
}
