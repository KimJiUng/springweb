package ezenweb.dto;

import ezenweb.domain.room.RoomEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class RoomDto {

    private int rno;                        // 건물번호
    private String rtitle;                  // 건물이름
    private String rlat;                     // 위도
    private String rlon;                     // 경도
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
    private String rdetail;                 // 상세설명
    private String ractive;                 // 거래 상태
    private List<MultipartFile> rimg;       // 이미지


    // Dto -> entity 변환 메소드
        // 1. 생성자
        // 2. 빌더 패턴
        // 3. ModelMapper 라이브러리
    public RoomEntity toentity(){
        return RoomEntity.builder()
                .rno(this.rno)
                .rtitle(this.rtitle)
                .rlat(this.rlat)
                .rlon(this.rlon)
                .rtype(this.rtype)
                .rprice(this.rprice)
                .rarea(this.rarea)
                .radministrativeexpenses(this.radministrativeexpenses)
                .rrescue(this.rrescue)
                .rcompletiondate(this.rcompletiondate)
                .rparking(this.rparking)
                .relevator(this.relevator)
                .rmovein(this.rmovein)
                .rcurrentfloor(this.rcurrentfloor)
                .rallfloor(this.rallfloor)
                .rbuildingtype(this.rbuildingtype)
                .raddress(this.raddress)
                .rdetail(this.rdetail)
                .ractive(this.ractive)
                .build();
    }


}
