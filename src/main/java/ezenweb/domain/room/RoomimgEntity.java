package ezenweb.domain.room;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Table(name = "roomimg")
@Entity
public class RoomimgEntity {

    // pk 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rimgno;
    // 이미지이름
    private String rimg;
    // 방번호[FK]
    @ManyToOne
    @JoinColumn(name = "rno")
    private RoomEntity roomEntity;


}
