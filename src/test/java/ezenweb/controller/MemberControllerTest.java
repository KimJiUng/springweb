package ezenweb.controller;

import ezenweb.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void testlogin() throws Exception {
        mvc.perform(get("/member/login")).andDo(print());
    }

    @Test
    void testsignup() throws Exception {
        mvc.perform(get("/member/signup")).andDo(print());
    }

    @Test
    void signuptest() throws Exception {
        mvc.perform(post("/member/signup")
                .param(("mid"),"testid")
                .param(("mpassword"),"testpw")
                .param(("mname"),"testname")
        ).andDo(print());
    }

//    @Test
//    void logintest() throws Exception {
//        mvc.perform(post("/member/login")
//                .param("mid","testid")
//                .param("mpassword","testpw")).andDo(print());
//    }

    @Test
    void testlogout() throws Exception {
        mvc.perform(get("/member/logout")).andDo(print());
    }

    @Test
    void testupdate() throws Exception {
        mvc.perform(get("/member/update")).andDo(print());
    }

//    @Test
//    void updatetest() throws Exception {
//        LoginDto loginDto = LoginDto.builder()
//                .mno(1)
//                .mid("testid")
//                .mname("testname")
//                .build();
//        MockHttpSession mockHttpSession = new MockHttpSession();
//        mockHttpSession.setAttribute("login",loginDto);
//        mvc.perform(put("/member/update")
//                .param("mname","updatename")
//                .session(mockHttpSession)).andDo(print());
//    }

    @Test
    void testmyroom() throws Exception{
        mvc.perform(get("/member/myroom")).andDo(print());
    }

//    @Test
//    void testmdelete() throws Exception{
//        LoginDto loginDto = LoginDto.builder()
//                .mno(1)
//                .mid("testid")
//                .mname("updatename")
//                .build();
//        MockHttpSession mockHttpSession = new MockHttpSession();
//        mockHttpSession.setAttribute("login",loginDto);
//        mvc.perform(delete("/member/mdelete")
//                .param("mpassword","testpw")
//                .session(mockHttpSession)).andDo(print());
//    }


}
