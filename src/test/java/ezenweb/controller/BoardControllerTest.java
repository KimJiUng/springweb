package ezenweb.controller;

import ezenweb.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

// 스프링 테스트를 위한 MockMvcRequest 메소드 호출
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc   // MVC 테스트중 C,S,M 가능
//@WebMvcTest // MVC 테스트중 C 가능
public class BoardControllerTest {

    @Autowired
    MockMvc mvc;    // MVC 테스트에 사용 되는 클래스

    // status = 200 성공 status = 400 실패 status = 404 경로문제
    // MockHttpServletRequest


    // 1. 게시판 열기 테스트
    @Test
    public void testlist() throws Exception {

        // view 없는 가정 하에 HTTP 테스트할 수 있는 메소드 = perform
        // perform(HTTP 요청메소드("URL") )
        mvc.perform(get("/board/list")).andExpect(status().isOk()).andDo(print());

    }

    // 2. 게시판 개별 조회 페이지 열기 테스트
    @Test
    void testview() throws Exception {
        mvc.perform(get("/board/view/1")).andExpect(status().isOk()).andDo(print());
    }

    // 3. 게시판 수정 페이지 열기 테스트
    @Test
    void testupdate() throws Exception {
        mvc.perform(get("/board/update")).andExpect(status().isOk()).andDo(print());
    }

    // 4. 게시판 쓰기 페이지 열기 테스트
    @Test
    void testsave() throws Exception {
        mvc.perform(get("/board/save")).andExpect(status().isOk()).andDo(print());
    }

    ////////////////////

    // 게시물 작성 테스트
//    @Test
//    void testsaveservice() throws Exception {
//
//        // 변수 전달 테스트
//            // http 요청메소드("URL").param("필드명",데이터)
//        // 세션 전달 테스트
//            // MockHttpSession 클래스
//            // http요청메소드("URL").session(세션객체명);
//
//        LoginDto loginDto = LoginDto.builder()
//                .mno(1)
//                .mid("123")
//                .mname("123")
//                .build();
//        MockHttpSession mockHttpSession = new MockHttpSession();
//        mockHttpSession.setAttribute("login",loginDto);
//        mvc.perform(post("/board/save")
//                        .param("btitle","테스트제목")
//                        .param("bcontent","테스트내용")
//                        .param("category","자유게시판")
//                        .session(mockHttpSession))
//                .andDo(print());
//    }


    // 모든 게시물 호출 테스트
    @Test
    void testgetboardlist() throws Exception {
        mvc.perform(get("/board/getboardlist")
                .param("cno","2")
                .param("key","")
                .param("keyword","")
                .param("page","1")).andDo(print());
    }

    // 게시물 검색 테스트
    @Test
    void getboardlist() throws Exception {
        mvc.perform(get("/board/getboardlist")
                .param("cno","2")
                .param("key","btitle")
                .param("keyword","123")
                .param("page","1")
                ).andDo(print());
    }

    // 게시물 개별 조회 테스트
    @Test
    void testgetboard() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("bno",1);
        mvc.perform(get("/board/getboard")
                .session(mockHttpSession)).andDo(print());
    }

    // 특정 게시물 수정 테스트
    @Test
    void testupdate2() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("bno",1);
        mvc.perform(put("/board/update")
                        .param("btitle","제목수정테스트")
                        .param("bcontent","내용수정테스트")
                        .session(mockHttpSession)).andDo(print());
    }

    // 특정 게시물 삭제 테스트
    @Test
    void testdelete() throws Exception {
        mvc.perform(delete("/board/delete")
                .param("bno","1")).andDo(print());
    }

    // 카테고리 출력 테스트
    @Test
    void testgetcategory() throws Exception {
        mvc.perform(get("/board/getcategory")).andDo(print());
    }


}
