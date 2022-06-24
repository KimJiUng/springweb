package ezenweb.domain.member;

import ezenweb.domain.BaseTime;
import ezenweb.domain.board.BoardEntity;
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

    @Enumerated(EnumType.STRING)
    private Role role;  // 권한

    public String getrolekey() {
        return role.getKey();
    }

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<RoomEntity> roomEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

}
