package ezenweb.domain.member;

import ezenweb.domain.BaseTime;
import ezenweb.domain.room.RoomEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
public class MemberEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    private String mid;
    private String mpassword;
    private String mname;

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity")
    private List<RoomEntity> roomEntityList = new ArrayList<>();

}
