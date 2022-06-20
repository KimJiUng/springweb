package ezenweb.controller;

import ezenweb.dto.BoardDto;
import ezenweb.service.BoardService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {

    //
    @Autowired
    private BoardService boardService;

    /////////////////////////////////// 1. view 열기[템플릿 연결] 매핑 //////////////////
    // 1. 게시판 페이지 열기
    @GetMapping("/list")
    public String list(){return "/board/list";}
    // 2. 게시판 개별 조회 페이지
    @GetMapping("/view")
    public String view(){return "/board/view";}
    // 3. 게시물 수정 페이지
    @GetMapping("/update")
    public String update(){return "/board/update";}
    // 4. 게시물 작성 페이지
    @GetMapping("/save")
    public String save(){return "/board/save";}
    /////////////////////////////////// 2. service 매핑 ///////////////////
    // 1. C
    @PostMapping("/save")
    @ResponseBody   // 템플릿이 아닌 객체 반환
    public boolean save(BoardDto boardDto){
        return boardService.save(boardDto);
    }

    // 2. R
    @GetMapping("/getboardlist")
    public void getblist(HttpServletResponse response){
        JSONArray jsonArray = boardService.getboardlist();
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        }catch(Exception e){e.printStackTrace();}

    }

    // 3. U
    @PutMapping("/update")
    @ResponseBody
    public boolean update(BoardDto boardDto){
        return boardService.update(boardDto);
    }

    // 4. D
    @DeleteMapping("/delete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno){
        return boardService.delete(bno);
    }

}
