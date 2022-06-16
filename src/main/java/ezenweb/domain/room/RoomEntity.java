package ezenweb.domain.room;

import ezenweb.domain.BaseTime;
import ezenweb.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Table(name = "room")
public class RoomEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;                        // 건물번호
    private String rtitle;                  // 건물이름
    private String rlat;                    // 위도
    private String rlon;                    // 경도
    private String rtype;                   // 거래방식
    private int rprice;                     // 가격
    private String rarea;                   // 면적
    private int radministrativeexpenses;    // 관리비
    private String rrescue;                 // 구조
    private String rcompletiondate;         // 준공날짜
    private boolean rparking;               // 주차여부
    private boolean relevator;              // 엘리베이터 여부
    private String rmovein;                 // 입주가능일
    private int rcurrentfloor;              // 현재층
    private int rallfloor;                  // 건물전체층
    private String rbuildingtype;           // 건물종류
    private String raddress;                // 주소
    @Column(columnDefinition = "TEXT")
    private String rdetail;                 // 상세설명
    private String ractive;                 // 거래 상태
    @Builder.Default
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    private List<RoomimgEntity> roomimgEntityList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;

}
