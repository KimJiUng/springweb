package ezenweb.service;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.domain.room.RoomEntity;
import ezenweb.domain.room.RoomRepository;
import ezenweb.domain.room.RoomimgEntity;
import ezenweb.domain.room.RoomimgRepository;
import ezenweb.dto.LoginDto;
import ezenweb.dto.RoomDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomimgRepository roomimgRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberRepository memberRepository;

    // 1. 룸 저장
    @Transactional
    public boolean room_save(RoomDto roomDto){

        String mid = memberService.getloginid();

        // 현재 로그인된 회원의 엔티티 찾기
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);
        if(!optional.isPresent()){
            return false;
        }
        MemberEntity memberEntity = optional.get();
        // 1. dto->entity  [ dto는 DB에 저장불가 ]
        RoomEntity roomEntity = roomDto.toentity();

       // 2. 저장 [우선적으로 룸 DB에 저장. pk 생성]
       roomRepository.save(roomEntity);

            // 현재 로그인된 회원 엔티티를 룸 엔티티에 저장
            roomEntity.setMemberEntity(memberEntity);
            // 현재 로그인된 회원 엔티티내 룸 리스트에 룸 엔티티 추가
            memberEntity.getRoomEntityList().add(roomEntity);

       // 3. 입력받은 첨부파일 저장
        String uuidfile = null;
        // 첨부파일
        if(roomDto.getRimg().size() !=0){   // 첨부파일이 1개 이상이면
            // 1. 반복문을 이용한 모든 첨부파일 호출
            for(MultipartFile file : roomDto.getRimg()){

                // 파일명이 동일하면 식별 문제 발생
                    // 1. UUID 난수 생성
                    UUID uuid = UUID.randomUUID();
                    // 2. UUID + 파일명
                    uuidfile = uuid.toString()+"_"+file.getOriginalFilename().replaceAll("_","-");
                    // UUID와 파일명 구분 _ 사용 [만약에 파일명에 _ 존재하면 문제발생 -> 파일명 _ 없애기]

                // 2. 경로설정
                    // 1. 프로젝트 내 이미지 저장
                String dir = "D:\\spring\\springweb\\src\\main\\resources\\static\\upload\\";
                    // 2. 서버에 이미지 저장 [ 다시 빌드 x = 스프링부트는 내장서버이기 때문에 = 서버 재시작시 초기화 ]
                //String dir = "D:\\spring\\springweb\\build\\resources\\main\\static\\upload";
                String filepath = dir + uuidfile;
                                    // .getOriginalFilename() : 실제 첨부파일 이름

                try{
                    // 3. ***첨부파일 업로드 처리
                    file.transferTo(new File(filepath)); // 파일명.transferTo(새로운경로 -> 파일)

                    // 관계저장
                        // 1. 이미지 엔티티 객체 생성
                        RoomimgEntity roomimgEntity = RoomimgEntity.builder()
                                .rimg(uuidfile)
                                .roomEntity(roomEntity)
                                .build();
                        // 2. 엔티티 세이브
                        roomimgRepository.save(roomimgEntity);
                        // 3. 이미지 엔티티를 룸엔티티에 추가
                        roomEntity.getRoomimgEntityList().add(roomimgEntity);
                }catch(Exception e){
                    System.out.println("파일저장 실패 : "+e);
                }


            }
        }


        // 저장
        roomRepository.save(roomEntity);
        return true;
    }

    // 2. 룸 호출
        // 반환타입 { 키 : [ {} , {} , {} ] }
        // JSON vs 컬렉션프레임워크
        // JSONObject = Map
        // JSONArray = List
                    // {키:값} = entry -> Map 컬렉션
                    // [ 요소1, 요소2, 요소3] -> List 컬렉션
                    // List<Map<String,String> >
                    // { "position" : [] }
                    // Map<String,List<Map<String,String>>>
    /*public JSONObject room_list() {
        JSONArray jsonArray = new JSONArray();
        // 1. 모든 엔티티 호출
        List<RoomEntity> roomEntityList = roomRepository.findAll(); // 엔티티에 생성자 없으면 오류 발생
        // 2. 모든 엔티티 -> json 변환
        JSONObject object = null;
        for (RoomEntity roomEntity : roomEntityList) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("rname", roomEntity.getRoomname());
            jsonObject.put("lng", roomEntity.getX());
            jsonObject.put("lat", roomEntity.getY());

            jsonArray.put(jsonObject);

            object = new JSONObject();
            object.put("positions", jsonArray);
        }
        // 3. 반환
        return object;
    }
    */

    // 2. Map 사용시
    public Map<String, List<  Map<String, String> >> room_list(Map<String,String> location) {

        // 현재 보고있는 지도 범위
        double qa = Double.parseDouble(location.get("qa"));
        double ha = Double.parseDouble(location.get("ha"));
        double oa = Double.parseDouble(location.get("oa"));
        double pa = Double.parseDouble(location.get("pa"));

        List<  Map<String, String> > maplist = new ArrayList<>();
        // 1. 모든 엔티티 꺼내오기
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        // 2. 엔티티 -> Map 변환
        for(RoomEntity roomEntity : roomEntityList){    // 리스트에서 엔티티 하나씩 꺼내오기

            // [조건]location 범위 내 좌표만 저장하기
            if(Double.parseDouble(roomEntity.getRlon()) > qa && Double.parseDouble(roomEntity.getRlon())<pa
            && Double.parseDouble(roomEntity.getRlat())>ha && Double.parseDouble(roomEntity.getRlat())<oa ){
                // 3. Map 객체 생성

                Map<String, String> map = new HashMap<>();
                map.put("rno", roomEntity.getRno()+"");
                map.put("rtitle", roomEntity.getRtitle());
                map.put("rlon", roomEntity.getRlon());
                map.put("rlat", roomEntity.getRlat());
                map.put("rimg",roomEntity.getRoomimgEntityList().get(0).getRimg());
                // 4. 리스트에 넣기
                maplist.add(map);
            }

        }
        Map<String, List<  Map<String, String> >> object = new HashMap<>();
        object.put("positions",maplist);
        System.out.println(object);
        return object;
    }

    // 개별 룸 호출
    public Map<String,String> getroom(int rno){
        Optional<RoomEntity> roomEntity = roomRepository.findById(rno);
        Map<String,String> map = new HashMap<>();
        map.put("rno", roomEntity.get().getRno()+"");
        map.put("rtitle", roomEntity.get().getRtitle());
        map.put("rlon", roomEntity.get().getRlon());
        map.put("rlat", roomEntity.get().getRlat());
        map.put("rimg",roomEntity.get().getRoomimgEntityList().get(0).getRimg());
        map.put("rtype", roomEntity.get().getRtype());
        map.put("rprice", roomEntity.get().getRprice()+"");
        map.put("rarea", roomEntity.get().getRarea());
        map.put("radministrativeexpenses", roomEntity.get().getRadministrativeexpenses()+"");
        map.put("rrescue", roomEntity.get().getRrescue());
        map.put("rcompletiondate", roomEntity.get().getRcompletiondate());
        map.put("rparking", roomEntity.get().isRparking()+"");
        map.put("relevator", roomEntity.get().isRelevator()+"" );
        map.put("rmovein", roomEntity.get().getRmovein());
        map.put("rcurrentfloor", roomEntity.get().getRcurrentfloor()+"");
        map.put("rallfloor", roomEntity.get().getRallfloor()+"");
        map.put("rbuildingtype", roomEntity.get().getRbuildingtype());
        map.put("raddress", roomEntity.get().getRaddress());
        map.put("rdetail", roomEntity.get().getRdetail());
        map.put("ractive", roomEntity.get().getRactive());

       return map;
    }

    // 해당 룸의 이미지 호출
    public JSONArray getroomimg(int rno){
        List<RoomimgEntity> roomimgEntityList = roomimgRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(RoomimgEntity roomimgEntity : roomimgEntityList){
            if(rno == roomimgEntity.getRoomEntity().getRno()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rimg",roomimgEntity.getRimg());
                jsonObject.put("mid",roomimgEntity.getRoomEntity().getMemberEntity().getMid());
                jsonArray.put(jsonObject);
            }
        }
        System.out.println(jsonArray.toString());

        return jsonArray;
    }

    // 내가 등록한 룸 리스트 호출
    public JSONArray myroom_list() {
        String mid = memberService.getloginid();
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);
        if(!optional.isPresent()){
            return null;
        }
        MemberEntity memberEntity = optional.get();
        JSONArray jsonArray = new JSONArray();
        // 1. 모든 엔티티 꺼내오기
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        // 2. 엔티티 -> Map 변환
        for(RoomEntity roomEntity : roomEntityList){    // 리스트에서 엔티티 하나씩 꺼내오기

            if(memberEntity.getMno() == roomEntity.getMemberEntity().getMno() ) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rno", roomEntity.getRno() + "");
                jsonObject.put("rtitle", roomEntity.getRtitle());
                jsonObject.put("rlon", roomEntity.getRlon());
                jsonObject.put("rlat", roomEntity.getRlat());
                jsonObject.put("rimg", roomEntity.getRoomimgEntityList().get(0).getRimg());
                jsonObject.put("rtype", roomEntity.getRtype());
                jsonObject.put("rprice", roomEntity.getRprice()+"");
                jsonObject.put("rarea", roomEntity.getRarea());
                jsonObject.put("radministrativeexpenses", roomEntity.getRadministrativeexpenses()+"");
                jsonObject.put("rrescue", roomEntity.getRrescue());
                jsonObject.put("rcompletiondate", roomEntity.getRcompletiondate());
                jsonObject.put("rparking", roomEntity.isRparking()+"");
                jsonObject.put("relevator", roomEntity.isRelevator()+"" );
                jsonObject.put("rmovein", roomEntity.getRmovein());
                jsonObject.put("rcurrentfloor", roomEntity.getRcurrentfloor()+"");
                jsonObject.put("rallfloor", roomEntity.getRallfloor()+"");
                jsonObject.put("rbuildingtype", roomEntity.getRbuildingtype());
                jsonObject.put("raddress", roomEntity.getRaddress());
                jsonObject.put("rdetail", roomEntity.getRdetail());
                jsonObject.put("ractive", roomEntity.getRactive());
                jsonObject.put("createdate", roomEntity.getCreatedate());
                jsonObject.put("modifiedate", roomEntity.getModifiedate());
                // 4. 리스트에 넣기
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray;
    }

    // 룸 삭제 메소드
    public boolean room_delete(int rno){
        RoomEntity roomEntity = roomRepository.findById(rno).get();
        if(roomEntity==null){
            return false;
        }
        roomRepository.delete(roomEntity);
        return true;
    }


}
