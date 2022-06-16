package ezenweb.service;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.dto.LoginDto;
import ezenweb.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    HttpServletRequest request;

    // 로직/트랜잭션
    // 1. 로그인처리 메소드
    public boolean login(String mid, String mpassword){

        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntityList = memberRepository.findAll();

        // 2. 모든 엔티티 리스트에서 입력받은 데이터와 비교
        for(MemberEntity entity : memberEntityList){
            // 3. 아이디와 비밀번호가 동일하면
            if(entity.getMid().equals(mid) && entity.getMpassword().equals(mpassword)){
                LoginDto loginDto = LoginDto.builder()
                        .mno(entity.getMno())
                        .mid(entity.getMid())
                        .mname(entity.getMname())
                        .build();
                // 세션 객체 호출
                request.getSession().setAttribute("login",loginDto);

                return true;    // 4. 로그인 성공
            }
        }
        return false;   // 5. 로그인 실패
    }
    
    @Autowired
    private MemberRepository memberRepository;

    // 다른 클래스의 메소드나 필ㄹ드 호출 방법!!!
        // * 메모리 할당[객체 만들기]
        // 1. static : java 실행시 우선 할당 -> java 종료시 메모리 초기화
        // 2. 객체생성
            // 1. 클래스명 객체명 = new 클래스명();

            // 2. 객체명.set필드명 = 데이터

            // 3.
            //  @Autowired
            // 클래스명 객체명 ;


    // 2. 회원가입처리 메소드
    public boolean signup(MemberDto memberDto){

        // dto -> entity
        MemberEntity memberEntity = memberDto.toentity();

        // entity 저장
        memberRepository.save(memberEntity);
        // save 여부 판단
        if(memberEntity.getMno()<1){
            return false;
        }else{
            return true;
        }
    }

    // 3. 로그아웃 처리 메소드
    public void logout(){
        request.getSession().setAttribute("login",null);    // 해당 세션에 null 대입
    }

    // 4. 회원 수정 메소드
    @Transactional
    public boolean update(String mname){

        // 세션내 dto 호출
        LoginDto loginDto = (LoginDto)request.getSession().getAttribute("login");
        if(loginDto==null){
            return false;
        }
        MemberEntity memberEntity = memberRepository.findById(loginDto.getMno()).get();
        memberEntity.setMname(mname);
        return true;
    }

}
