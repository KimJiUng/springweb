package ezenweb.service;

import ezenweb.domain.RoomEntity;
import ezenweb.domain.RoomRepository;
import ezenweb.dto.RoomDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    // 1. 룸 저장
    public boolean room_save(RoomDto roomDto){
        // dto -> entity
        RoomEntity roomEntity = RoomEntity.builder()
                .rname(roomDto.getRname())
                .x(roomDto.getX())
                .y(roomDto.getY())
                .rtype(roomDto.getRtype())
                .rprice(roomDto.getRprice())
                .rarea(roomDto.getRarea())
                .radministrativeexpenses(roomDto.getRadministrativeexpenses())
                .rrescue(roomDto.getRrescue())
                .rcompletiondate(roomDto.getRcompletiondate())
                .rparking(roomDto.getRparking())
                .relevator(roomDto.getRelevator())
                .rmovein(roomDto.getRmovein())
                .rcurrentfloor(roomDto.getRcurrentfloor())
                .rallfloor(roomDto.getRallfloor())
                .rbuildingtype(roomDto.getRbuildingtype())
                .raddress(roomDto.getRaddress())
                .rdetail(roomDto.getRdetail())
                .build();

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
                String dir = "D:\\spring\\springweb\\src\\main\\resources\\static\\upload\\";
                String filepath = dir + uuidfile;
                                    // .getOriginalFilename() : 실제 첨부파일 이름

                try{
                    // 3. ***첨부파일 업로드 처리
                    file.transferTo(new File(filepath)); // 파일명.transferTo(새로운경로 -> 파일)
                    // 4. 엔티티에 파일명 저장
                    roomEntity.setRimg(uuidfile);
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
            if(Double.parseDouble(roomEntity.getY()) > qa && Double.parseDouble(roomEntity.getY())<pa
            && Double.parseDouble(roomEntity.getX())>ha && Double.parseDouble(roomEntity.getX())<oa ){
                // 3. Map 객체 생성
                Map<String, String> map = new HashMap<>();
                map.put("rname", roomEntity.getRname());
                map.put("lng", roomEntity.getX());
                map.put("lat", roomEntity.getY());
                map.put("rno", roomEntity.getRno()+"");
                map.put("rimg", roomEntity.getRimg());
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
    public Optional<RoomEntity> getroom(int rno){
      return roomRepository.findById(rno);
    }



}
