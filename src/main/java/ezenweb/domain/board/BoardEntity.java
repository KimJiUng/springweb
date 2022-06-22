package ezenweb.domain.board;

import ezenweb.domain.BaseTime;
import ezenweb.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter@Setter
@ToString(exclude = {"memberEntity","categoryEntity"})  // toString 사용시 무한루프 방지용
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "board")
public class BoardEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    private String btitle;
    private String bcontent;
    private int bview;
    private int blike;
    // 작성자 [연관관계]
    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;
    // 첨부파일 [연관관계]
    // 카테고리 [연관관계]
    @ManyToOne
    @JoinColumn(name = "cno")
    private CategoryEntity categoryEntity;
    // 댓글 [연관관계]



}
