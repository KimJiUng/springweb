package ezenweb.domain.member;

import ezenweb.domain.BaseTime;
import ezenweb.domain.board.BoardEntity;
import ezenweb.domain.message.MessageEntity;
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
    private String memail;
    private String oauth;   // 일반회원/oauth 회원 구분용

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

    // 보낸 메세지 리스트
    @Builder.Default
    @OneToMany(mappedBy = "fromentity", cascade = CascadeType.ALL)
    private List<MessageEntity> fromentityList = new ArrayList<>();

    // 받은 메세지 리스트
    @Builder.Default
    @OneToMany(mappedBy = "toentity", cascade = CascadeType.ALL)
    private List<MessageEntity> toentityList = new ArrayList<>();

}
