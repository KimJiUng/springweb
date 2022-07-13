package ezenweb.controller;

import ezenweb.dto.MemberDto;
import ezenweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller // 템플릿 영역
@RequestMapping("/member")
public class MemberController {

    // 아이디 / 비밀번호 찾기 페이지 이동 매핑
    @GetMapping("/find")
    public String find(){ return "member/find";}

    // 아이디 찾기
    @GetMapping("/findid")
    @ResponseBody
    public String findid(@RequestParam("mname") String mname, @RequestParam("memail") String memail){
        return memberService.findid(mname,memail);
    }

    // 비밀번호 찾기
    @GetMapping("/findpw")
    @ResponseBody
    public Boolean findpw(@RequestParam("mid") String mid, @RequestParam("memail") String memail){
        Boolean result = memberService.findpw(mid,memail);
        return result;
    }

    // 로그인시 이메일 인증 체크
    @GetMapping("/authmailcheck")
    @ResponseBody
    public int authmailcheck(@RequestParam("mid") String mid){
        return memberService.authmailcheck(mid);
    }


    // 1. 로그인 페이지 이동 매핑
    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

    // 2. 회원가입 페이지 이동 매핑
    @GetMapping("/signup")
    public String signup(){
        return "member/write";
    }

    //
    @GetMapping("/email/{authkey}/{mid}")
    public String signupemail(@PathVariable("authkey") String authkey,@PathVariable("mid") String mid){
        // @PathVariable : 경로상(URL) 변수 요청

        // 이메일 검증 처리
        boolean result = memberService.authsuccess(authkey,mid);
        if(result){
            // 인증 성공 화면 전환
            return "member/authsuccess";
        }else{  // 인증 실패 화면 전환
            return "";
        }

    }

    @Autowired
    MemberService memberService;    // member 서비스 객체 생성

    // 3. 회원가입 처리 매핑
    @PostMapping("/signup")
    @ResponseBody
    public boolean save(MemberDto memberDto){
        // 서비스 호출
        boolean result = memberService.signup(memberDto);
        return result;
    }

    // 시큐리티 사용시에는 시큐리티내 로그인 서비스 사용
    // 4.로그인 처리 매핑
//    @PostMapping("/login")
//    @ResponseBody
//    public boolean login(@RequestParam("mid") String mid, @RequestParam("mpassword") String mpassword){
//       return memberService.login(mid,mpassword);
//    }

    // 시큐리티 사용시에는 시큐리티내 로그아웃 서비스 사용
    // 5. 로그아웃
//    @GetMapping("/logout")
//    public String logout(){
//        memberService.logout();
//        return "main";    // 타임리프 반환
//        //return "redirect:/";    // URL 이동
//    }

    // 6. 회원수정 경로 매핑
    @GetMapping("/update")
    public String update(){
        return "member/update";
    }

    // 7. 회원수정 처리 매핑
    @PutMapping("/update")
    @ResponseBody
    public boolean memberupdate(@RequestParam("mname") String mname){
        return memberService.update(mname);
    }

    @GetMapping("/myroom")
    public String myroom(){
        return "member/myroom";
    }

    // 회원 탈퇴
    @DeleteMapping("/mdelete")
    @ResponseBody
    public boolean mdelete(@RequestParam("mpassword") String mpassword){
        return memberService.mdelete(mpassword);
    }


    ////////////////////////////////// 쪽지 관련 /////////////////////////////////

    // 쪽지 페이지로 매핑
    @GetMapping("/message")
    public String message(){return "member/message";}

    // 안읽은 메세지 처리 컨트롤
    @GetMapping("/getisread")
    @ResponseBody
    public Integer getisread(){
        return memberService.getisread();
    }

    // 보낸 메세지 리스트 호출
    @GetMapping("/getfrommsglist")
    public void geetfrommsglist(HttpServletResponse response){
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(memberService.getfrommsglist());
        }catch(Exception e){
            System.out.println("보낸 메세지 리스트 출력 오류 : "+e);
        }

    }

    // 받은 메세지 리스트 호출
    @GetMapping("/gettomsglist")
    public void geettomsglist(HttpServletResponse response){
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(memberService.gettomsglist());
        }catch(Exception e){
            System.out.println("받은 메세지 리스트 출력 오류 : "+e);
        }
    }

    // 메세지 읽음 처리 메소드
    @PutMapping("/isread")
    @ResponseBody
    public boolean isread(@RequestParam("msgno") int msgno){
        System.out.println(msgno);
        return memberService.isread(msgno);
    }

    // 선택된 메세지 삭제 메소드
    @DeleteMapping("/msgdelete")
    @ResponseBody
    public boolean msgdelete(@RequestBody List<Integer> deletelist){
        return memberService.msgdelete(deletelist);
    }

}
