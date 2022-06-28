package ezenweb.controller;

import ezenweb.dto.MemberDto;
import ezenweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // 템플릿 영역
@RequestMapping("/member")
public class MemberController {

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

    @DeleteMapping("/mdelete")
    @ResponseBody
    public boolean mdelete(@RequestParam("mpassword") String mpassword){
        return memberService.mdelete(mpassword);
    }


}
