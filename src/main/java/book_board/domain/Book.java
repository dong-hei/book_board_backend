package book_board.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //서버 실행시 테이블이 h2에 생성
@Table(name="book")
public class Book {
    @Id // PK를 해당 변수로
    @GeneratedValue(strategy = GenerationType.IDENTITY) //해당 DB 번호 증가 전략을 따라간다.
    private Long id;

    private String title;
    private String author;


}
