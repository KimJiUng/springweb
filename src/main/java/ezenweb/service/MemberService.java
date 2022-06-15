package ezenweb.service;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    // 로직/트랜잭션
    // 1. 로그인처리 메소드
    public boolean login(){
        return false;
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
        MemberEntity memberEntity = MemberEntity.builder()
                .mid(memberDto.getMid())
                .mpassword(memberDto.getMpassword())
                .mname(memberDto.getMname())
                .build();
        // entity 저장
        memberRepository.save(memberEntity);
        return false;
        
    }
}
