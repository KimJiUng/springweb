package ezenweb.service;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.dto.LoginDto;
import ezenweb.dto.MemberDto;
import ezenweb.dto.OauthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

import org.springframework.mail.javamail.JavaMailSender;

@Service
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
                                    // UserDetailsService : 일반회원
                                        // -----> loadUserByUsername 메소드 구현
                                    // OAuth2UserService<OAuth2UserRequest, OAuth2User> : Oauth2 회원
                                        // -----> loadUser 메소드 구현
    // 1. 로그인 서비스 제공 메소드
    // 2. 패스워드 검증 X [시큐리티 제공]
    // 3. 아이디만 검증 처리

    // 일반회원 로그인
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        // 1. 회원 아이디로 엔티티 찾기
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
        MemberEntity memberEntity = optionalMember.orElse(null);
                                    // Optional 클래스 [null 오류 방지]
                                    // 1. optional.isPresent() : null 아니면
                                    // 2. optional.orElse(데이터) : 만약에 optional 객체가 비어있으면 반환할 데이터
        // 2. 찾은 회원엔티티의 권한[키]을 리스트에 담기
        List<GrantedAuthority> authorityList = new ArrayList<>();
                // GrantedAuthority : 부여된 인증의 클래스
                // List<GrantedAuthority> : 부여된 인증들을 모아두기
        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey() ) );
                // 리스트에 인증된 엔티티의 키를 보관
        System.out.println("권한 키 : "+ memberEntity.getrolekey());
        // UserDetails -> 인증되면 세션 부여
        return new LoginDto(memberEntity, authorityList); // 회원 엔티티, 인증된 리스트 세션 부여

    }

    // OAuth 회원 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 인증[로그인성공] 된
        OAuth2UserService  oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 아이디 [ 네이버 vs 카카오 vs 구글 ] : ouath 구분용으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // 회원정보 요청시 사용되는 json 키 값 호출
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        System.out.println("클라이언트(개발자)가 등록한 이름 : "+registrationId);
        System.out.println("회원정보(JSON) 호출시 사용되는 이름 : "+userNameAttributeName);
        System.out.println("회원정보 : "+oAuth2User.getAttributes());

        OauthDto oauthDto = OauthDto.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());
        System.out.println("oauthDto 확인 : "+oauthDto.toString());
        // dto를 entity로 변환시켜서 DB에 저장

        // 이메일이 DB에 존재하면
        Optional<MemberEntity> optionalMember = memberRepository.findByMemail(oauthDto.getMemail());
        if(!optionalMember.isPresent()){
            MemberEntity memberEntity = oauthDto.toentity();
            memberRepository.save(memberEntity);
        }

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority("ROLE_MEMBER")),
                oAuth2User.getAttributes(),
                userNameAttributeName);    // 인증 세션
    }

    @Autowired
    HttpServletRequest request;

    // 로직/트랜잭션
    // 1. 로그인처리 메소드
//    public boolean login(String mid, String mpassword){
//
//        // 1. 모든 엔티티 호출
//        List<MemberEntity> memberEntityList = memberRepository.findAll();
//
//        // 2. 모든 엔티티 리스트에서 입력받은 데이터와 비교
//        for(MemberEntity entity : memberEntityList){
//            // 3. 아이디와 비밀번호가 동일하면
//            if(entity.getMid().equals(mid) && entity.getMpassword().equals(mpassword)){
//                LoginDto loginDto = LoginDto.builder()
//                        .mno(entity.getMno())
//                        .mid(entity.getMid())
//                        .mname(entity.getMname())
//                        .build();
//                // 세션 객체 호출
//                request.getSession().setAttribute("login",loginDto);
//
//                return true;    // 4. 로그인 성공
//            }
//        }
//        return false;   // 5. 로그인 실패
//    }
    
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

    @Autowired
    private JavaMailSender javaMailSender;  // 자바 메일 전송 인터페이스

    // 메일 전송 메소드
    public void mailsend(String 받는사람이메일,String 제목, StringBuilder 내용){
        // SMTP : 간이 메일 전송 프로토콜 [ 텍스트 외 불가능 ]
        try{
            // 이메일 전송
            MimeMessage message = javaMailSender.createMimeMessage();   // Mime 프로토콜 : 메세지안에 텍스트 외 데이터 담는 프로토콜 [SMTP와 같이 사용됨]
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true,"UTF-8");
            // 1. 보내는 사람
            mimeMessageHelper.setFrom("tgaggr@naver.com","Ezen 부동산");
            // 2. 받는 사람
            mimeMessageHelper.setTo(받는사람이메일);
            // 3. 메일 제목
            mimeMessageHelper.setSubject(제목);
            // 4. 메일 내용
            mimeMessageHelper.setText(내용.toString(),true);
            // 5. 메일 전송
            javaMailSender.send(message);
        }catch(Exception e){
            System.out.println("메일 전송 실패 : "+e);
        }
    }

    // 2. 회원가입처리 메소드
    @Transactional
    public boolean signup(MemberDto memberDto){

        // dto -> entity
        MemberEntity memberEntity = memberDto.toentity();

        // entity 저장
        memberRepository.save(memberEntity);
        // save 여부 판단
        if(memberEntity.getMno()<1){
            return false;   // 회원가입 실패
        }else{
            // 이메일에 들어가는 내용 [html]
            StringBuilder html = new StringBuilder();   // StringBUilder : 문자열 연결 클래스
            html.append("<html><body><h1> EZEN 부동산 회원 이메일 검증 </h1>");
                // 인증 코드 [문자 난수] 만들기
                Random random = new Random();   // 랜덤 객체
                StringBuilder authkey = new StringBuilder();
                for(int i=0; i<12; i++){    // 12자리 문자 난수 생성
                    char randomchar = (char)((random.nextInt(26))+97);   // 97~122   // 소문자 a~z 난수 발생
                    authkey.append(randomchar);  // 생성된 문자 난수들을 하나씩 연결 -> 문자열 만들기
                }
                System.out.println("인증코드 : "+authkey);
                // 인증 코드 전달
                html.append("<a href='http://localhost:8081/member/email/"+authkey+"/"+memberDto.getMid()+"'>이메일검증</a>");
                memberEntity.setOauth(authkey.toString());
            html.append("</body></html>");
            // 회원가입시 인증 메일 보내기
            System.out.println(memberDto.getMemail());
            mailsend(memberDto.getMemail(),"회원가입 메일인증",html);
            return true;    // 회원가입 성공
        }
    }

    // 3. 로그아웃 처리 메소드
//    public void logout(){
//        request.getSession().setAttribute("login",null);    // 해당 세션에 null 대입
//    }

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

    // 회원 탈퇴 메소드
    public boolean mdelete(String mpassword){
        LoginDto loginDto = (LoginDto)request.getSession().getAttribute("login");
        if(loginDto==null){
            return false;
        }
        Optional<MemberEntity> optionalMember = memberRepository.findById(loginDto.getMno());
        if(optionalMember.isPresent()){
            MemberEntity memberEntity = optionalMember.get();
            if(memberEntity.getMpassword().equals(mpassword)){
                memberRepository.delete(memberEntity);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Transactional
    public boolean authsuccess(String authkey,String mid){
        // DB 업데이트
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
        if(optionalMember.isPresent()){
            MemberEntity memberEntity = optionalMember.get();
            memberEntity.setOauth("Local");
            return true;
        }else{
            return false;
        }
    }


}
