package ezenweb;

import ezenweb.controller.BoardController;
import ezenweb.controller.HelloController;
import ezenweb.dto.BoardDto;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired
    private BoardController boardController;

    @Autowired
    private MockMvc mvc;    // GET, POST 매핑 API 테스트 사용

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello";
        mvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void boardsavetest(){
        BoardDto boardDto = BoardDto.builder()
                .btitle("title")
                .bcontent("content")
                .build();
        boolean result = boardController.save(boardDto);
        System.out.println("save test  : " +result);
    }

  /*  @Test
    public void boardlisttest(){
        JSONArray jsonArray = boardController.getblist();
        System.out.println("list test : " + jsonArray);
    }*/
    @Test
    public void boardupdate(){
        BoardDto boardDto = BoardDto.builder()
                .bno(1)
                .btitle("title update")
                .bcontent("content update")
                .build();
        boolean result = boardController.update(boardDto);
        System.out.println("update test : "+result);
    }

    @Test
    public void boarddelete(){
        boolean result = boardController.delete(1);
        System.out.println("delete test : "+result);
    }

}
