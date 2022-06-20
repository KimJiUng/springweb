package ezenweb.controller;

import ezenweb.dto.BoardDto;
import ezenweb.service.BoardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BoardService boardService;


    /////////////////////////////////// 1. view 열기[템플릿 연결] 매핑 //////////////////
    // 1. 게시판 페이지 열기
    @GetMapping("/list")
    public String list(){return "/board/list";}

    // 2. 게시판 개별 조회 페이지 열기
    @GetMapping("/view/{bno}")
    public String view(@PathVariable("bno") int bno){   // { } 안에서 선언된 변수는 밖에 사용불가
        // 1. 내가 보고 있는 게시물의 번호를 세션에 저장
        request.getSession().setAttribute("bno",bno);
        return "/board/view";
    }
    /*@GetMapping("/view/{bno}")   // URL 경로에 변수 = { 변수명 }
    @ResponseBody
    public void view(@PathVariable("bno") int bno, HttpServletResponse response){   // @PathVariable("변수명")
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(boardService.getboard(bno));
        }catch(Exception e){e.printStackTrace();}
        //model.addAttribute("data",boardService.getboard(bno));
        // return "/board/view";    // 템플릿을 ajax에게 통신
    }*/


    // 3. 게시물 수정 페이지 열기
    @GetMapping("/update")
    public String update(){
        return "/board/update";
    }
    // 4. 게시물 작성 페이지 열기
    @GetMapping("/save")
    public String save(){return "/board/save";}


    /////////////////////////////////// 2. service 매핑 ///////////////////
    // 1. C 게시물 저장 메소드
    @PostMapping("/save")
    @ResponseBody   // 템플릿이 아닌 객체 반환
    public boolean save(BoardDto boardDto){
        return boardService.save(boardDto);
    }

    // 2. R 전체 게시물 출력 처리 메소드
    @GetMapping("/getboardlist")
    public void getblist(HttpServletResponse response){
        JSONArray jsonArray = boardService.getboardlist();
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        }catch(Exception e){e.printStackTrace();}
    }

    // 2-2. R 개별 게시물 조회 처리 메소드
    @GetMapping("/getboard")
    public void getboard(HttpServletResponse response){
        int bno = (Integer)request.getSession().getAttribute("bno");
        JSONObject object = boardService.getboard(bno);
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch(Exception e){e.printStackTrace();}
    }




    // 3. U 게시물 수정 메소드
    @PutMapping("/update")
    @ResponseBody
    public boolean update(BoardDto boardDto){
        int bno = (Integer)request.getSession().getAttribute("bno");
        boardDto.setBno(bno);
        return boardService.update(boardDto);
    }

    // 4. D 게시물 삭제 메소드
    @DeleteMapping("/delete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno){
        return boardService.delete(bno);
    }

}
