package ezenweb.domain.test;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity // JPA : DB내 테이블과 매핑(연결)
@Table(name = "hello")  // 테이블 이름 설정
@Getter // 롬복
@NoArgsConstructor // 롬복
public class HelloEntity {
    @Id // JPA : pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA : autokey
    private Long id;

        // length = 필드길이(디폴트 255), nullable = null 포함 여부(디폴트 null 포함)
    @Column(length =500, nullable = false)  // JPA : Column(속성명=값, 속성명=값)
    private String title;

        // columnDefinition = "TEXT" : 긴글 자료형
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;
}
