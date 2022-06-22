package ezenweb.domain.board;

import ezenweb.domain.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
public class CategoryEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cno;
    private String cname;
    // board와 연관
    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardEntity> boardEntityList = new ArrayList<>();
}
