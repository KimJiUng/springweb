package ezenweb.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class RoomDto {

    private int rno;                        // 건물번호
    private String rname;                   // 건물이름
    private String x;                       // 좌표x
    private String y;                       // 좌표y
    private List<MultipartFile> rimg;       // 이미지
    private String rtype;                   // 거래방식
    private int rprice;                     // 가격
    private String rarea;                   // 면적
    private int radministrativeexpenses;    // 관리비
    private String rrescue;                 // 구조
    private String rcompletiondate;         // 준공날짜
    private String rparking;                // 주차여부
    private String relevator;               // 엘리베이터 여부
    private String rmovein;                 // 입주가능일
    private String rcurrentfloor;           // 현재층
    private String rallfloor;               // 건물전체층
    private String rbuildingtype;           // 건물종류
    private String raddress;                // 주소
    private String rdetail;                 // 상세설명

    // COS VS MultipartFile


}
