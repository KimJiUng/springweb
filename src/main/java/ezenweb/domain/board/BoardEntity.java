package ezenweb.domain.board;

import ezenweb.domain.BaseTime;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class BoardEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    private String btitle;
    private String bcontent;
    private int bview;
    private int blike;
    // 작성자 [연관관계]
    // 첨부파일 [연관관계]
    // 카테고리 [연관관계]
    // 댓글 [연관관계]



}
