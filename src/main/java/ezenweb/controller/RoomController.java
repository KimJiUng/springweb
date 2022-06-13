package ezenweb.controller;

import ezenweb.dto.RoomDto;
import ezenweb.service.RoomService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller // 해당 클래스가 템플릿 영역으로 사용
@RequestMapping("/room")    // 해당 클래스의 요청 매핑(room)
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/write")   // 1. 등록페이지 이동
    public String write(){
        return "room/write";
        // templates -> room -> write
    }

    @PutMapping(value = "/write", produces = "application/json; charset=UTF-8")   // 2. 등록처리
    @ResponseBody   // 템플릿이 아닌 객체 반환시 사용되는 어노테이션
    public boolean write_save(RoomDto roomDto){
                                // 요청변수중 DTO 필드와 변수명이 동일할 경우 자동 주입
        // 서비스에 dto 전달
        roomService.room_save(roomDto);
        System.out.println("입력값 : "+roomDto.toString());

        return true;
    }

    // 3. 방 목록 페이지 이동
    @GetMapping("/list")
    public String list(){
        return "room/list";
    }

    @GetMapping("/roomlist")
    @ResponseBody
    public void roomlist(HttpServletResponse response){

        JSONObject jsonObject = roomService.room_list();

        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonObject);
        }catch(Exception e){e.printStackTrace();}

    }


}


/*
    --------- @RequestMapping("경로")---------------
    @GetMapping     : FIND, GET [@RequestMapping("경로", method=RequestMapping)]
    @PutMapping     : UPDATE    [@RequestMapping("경로", method=RequestMapping)]
    @PostMapping    : SAVE      [@RequestMapping("경로", method=RequestMapping)]
    @DeleteMapping  : DELETE    [@RequestMapping("경로", method=RequestMapping)]

 */

/*
    view --> controller 변수 요청 방식
    // 1.   HttpServletRequest request
            String roomname = request.getParmeter("roomname"),
            String x = request.getParmeter("x"),
            String y = request.getParmeter("y")
            
    // 2.   @RequestParam("roomname") String roomname,
            @RequestParam("x") String x,
            @RequestParam("y") String y

    // 3. Mapping 사용시 DTO로 자동 주입된다.
        // 조건 1. : Mapping 사용해야 한다.
        // 조건 2. : 요청변수명과 DTO 필드명 동일해야 한다.

 */