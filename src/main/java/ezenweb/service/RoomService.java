package ezenweb.service;

import ezenweb.domain.RoomEntity;
import ezenweb.domain.RoomRepository;
import ezenweb.dto.RoomDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    // 1. 룸 저장
    public boolean room_save(RoomDto roomDto){
        // dto -> entity
        RoomEntity roomEntity = RoomEntity.builder()
                .roomname(roomDto.getRoomname())
                .x(roomDto.getX())
                .y(roomDto.getY())
                .build();
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
    public Map<String, List<  Map<String, String> >> room_list() {
        List<  Map<String, String> > maplist = new ArrayList<>();
        // 1. 모든 엔티티 꺼내오기
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        // 2. 엔티티 -> Map 변환
        for(RoomEntity roomEntity : roomEntityList){    // 리스트에서 엔티티 하나씩 꺼내오기
            // 3. Map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("rname", roomEntity.getRoomname());
            map.put("lng", roomEntity.getX());
            map.put("lat", roomEntity.getY());
            // 4. 리스트에 넣기
            maplist.add(map);
        }
        Map<String, List<  Map<String, String> >> object = new HashMap<>();
        System.out.println(object);
        return object;
    }

}
