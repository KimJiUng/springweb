package ezenweb.service;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.domain.message.MessageEntity;
import ezenweb.domain.message.MessageRepository;
import ezenweb.dto.LoginDto;
import ezenweb.dto.MemberDto;
import ezenweb.dto.OauthDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.time.format.DateTimeFormatter;
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
//        // 2. 찾은 회원엔티티의 권한[키]을 리스트에 담기
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//                // GrantedAuthority : 부여된 인증의 클래스
//                // List<GrantedAuthority> : 부여된 인증들을 모아두기
//        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey() ) );
//                // 리스트에 인증된 엔티티의 키를 보관

        // UserDetails -> 인증되면 세션 부여
        return new LoginDto(memberEntity,Collections.singleton(new SimpleGrantedAuthority(memberEntity.getrolekey()))); // 회원 엔티티, 인증된 리스트 세션 부여

    }

    // OAuth 회원 로그인
    @Override
    public OAuth2User loadUser( OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try{
            // 인증[로그인] 결과 정보 요청
            OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = oAuth2UserService.loadUser( userRequest );

            // 클라이언트 아이디 [ 네이버 vs 카카오 vs 구글 ] : oauth 구분용 으로 사용할 변수
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // 회원정보 요청시 사용되는 JSON 키 이름 호출  : 회원정보 호출시 사용되는 키 이름
            String userNameAttributeName = userRequest
                    .getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();

            // oauth2 정보 -> Dto -> entitiy -> db저장
            OauthDto oauthDto = OauthDto.of(  registrationId ,  userNameAttributeName  ,  oAuth2User.getAttributes()  );

            //  1. 이메일로 엔티티호출
            Optional<MemberEntity> optional
                    =  memberRepository.findBymemail( oauthDto.getMemail() );
            // 2. 만약에 엔티티가 없으면
            MemberEntity memberEntity = null;

            if( !optional.isPresent() ){
                memberEntity = oauthDto.toentity();
                memberRepository.save( memberEntity );  // entity 저장
            }else{
                memberEntity = optional.get();
            }

            // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
            // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수~  ]

            return new LoginDto(memberEntity,Collections.singleton(new SimpleGrantedAuthority(memberEntity.getrolekey())));
        }catch (Exception e){e.printStackTrace();}
        return null;
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

    // 5. 회원 탈퇴 메소드
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

    // 6. 아이디 찾기 [이름과 이메일이 동일한 경우 프론트에 표시]
    public String findid(String mname, String memail){
        String findid = null;
        Optional<MemberEntity> optional = memberRepository.findid(mname,memail);
        if(optional.isPresent()){
            findid = optional.get().getMid();
        }
        return findid;
    }

    @Transactional
    // 7. 패스워드 찾기 [아이디,이메일이 동일한 경우 이메일로 임시비밀번호(난수) 전송]
    public boolean findpw(String mid, String memail){
        Optional<MemberEntity> optional = memberRepository.findpw( mid , memail );
        if( optional.isPresent() ){ // 해당 엔티티를 찾았으면
            // 1. 임시비밀번호 난수 생성한다.
            String tempassword = "";         //            StringBuilder temppassword = new StringBuilder();
            for( int i = 0 ; i<12 ; i++ ) {
                Random random = new Random();
                char rchar = (char) (random.nextInt(58) + 65);
                tempassword += rchar;  //                temppassword.append( rchar );
            }
            System.out.println("임시비밀번호 : " + tempassword );
            // 2. 현재 비밀번호를 임시비밀번호로 변경한다.
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();   // 비크립트 방식의 암호화
            optional.get().setMpassword( passwordEncoder.encode( tempassword) ); // 암호화
            // 3. 변경된 비밀번호를 이메일로 전송한다.
            StringBuilder html = new StringBuilder();    // 메일 내용 구현
            html.append("<html><body>");        // html 시작
            html.append("<div>회원님의 임시 비밀번호</div>");
            html.append("<div>"+ tempassword + "</div>");
            html.append("</body></html>");        //html 끝
            // 메일 전송 메소드 호출
            mailsend( optional.get().getMemail(),  "EZEN부동산 회원 임시 비밀번호" ,  html );
            return true;
        }
        // 해당 엔티티를 못찾았으면
        return false;
    }

    public int authmailcheck(String mid){
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
        if(optionalMember.isPresent()){
            if(optionalMember.get().getOauth().equals("Local")){
                return 1;
            }else if(optionalMember.get().getOauth().equals("kakao")){
                return 2;
            }else if(optionalMember.get().getOauth().equals("naver")){
                return 3;
            }
        }
        return 0;
    }

    // 로그인(인증)된 회원의 아이디 꺼내오기 메소드
    public String getloginid(){
        // 1. 인증 객체 호출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 2. 인증 정보 객체 호출
        Object principal = authentication.getPrincipal();
        if(principal.equals("anonymousUser")){  // 로그인 안 된 상태
            return null;
        }else{  // 로그인 된 상태
            LoginDto loginDto = (LoginDto) principal;
            return loginDto.getMid();
        }
    }


    ///////////////////////////////////////// 쪽지 관련 //////////////////////////////////////////

    @Autowired
    private MessageRepository messageRepository;

    // 1. 쪽지 전송 메소드
    @Transactional
    public boolean messagesend(JSONObject object){
        // 1. JSON 정보 호출
        String from = object.getString("from");
        String to = object.getString("to");
        String msg = object.getString("msg");
        // 2. 각 회원들의 엔티티 찾기
        Optional<MemberEntity> fromoptional = memberRepository.findBymid(from);
        if(!fromoptional.isPresent()){return false;}
        Optional<MemberEntity> tooptional = memberRepository.findBymid(to);
        if(!tooptional.isPresent()){return false;}
        MemberEntity fromentity = fromoptional.get();
        MemberEntity toentity = tooptional.get();
        // 3. 메세지 엔티티 생성
        MessageEntity messageEntity = MessageEntity.builder()
                .msg(msg)
                .fromentity(fromentity)
                .toentity(toentity)
                .build();
        // 4. 메세지 세이브
        messageRepository.save(messageEntity);
        // 5. 각 회원에 메세지 fk 주입
        fromentity.getFromentityList().add(messageEntity);
        toentity.getToentityList().add(messageEntity);
        return true;
    }
    // 2. 안읽은 쪽지 개수 카운트 메소드
    public Integer getisread(){
        // 1. 로그인(인증)된 회원의 아이디
        String mid = getloginid();
        if(mid == null){return -1;}
        int mno = memberRepository.findBymid(mid).get().getMno();
        return messageRepository.getisread(mno);
    }

    // 3. 본인이 보낸 메세지 리스트 호출
    public JSONArray getfrommsglist(){
        String mid = getloginid();
        if(mid == null){return null;}
        List<MessageEntity> messageEntityList = memberRepository.findBymid(mid).get().getFromentityList();
        JSONArray jsonArray = new JSONArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(MessageEntity entity : messageEntityList){
            JSONObject object = new JSONObject();
            object.put("msg",entity.getMsg());
            object.put("to",entity.getToentity().getMid());
            object.put("msgno",entity.getMsgno());
            object.put("date",formatter.format(entity.getCreatedate()));
            object.put("isread",entity.isIsread());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    // 4. 본인이 받은 메세지 리스트 호출
    public JSONArray gettomsglist(){
        String mid = getloginid();
        if(mid == null){return null;}
        List<MessageEntity> messageEntityList =
                memberRepository.findBymid(mid).get().getToentityList();
        JSONArray jsonArray = new JSONArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(MessageEntity entity : messageEntityList){
            JSONObject object = new JSONObject();
            object.put("msg",entity.getMsg());
            object.put("from",entity.getFromentity().getMid());
            object.put("msgno",entity.getMsgno());
            object.put("date",formatter.format(entity.getCreatedate()));
            object.put("isread",entity.isIsread());
            jsonArray.put(object);
        }
        return jsonArray;
    }


    // 읽음 처리 메소드 [수정]
    @Transactional
    public boolean isread(int msgno){
        System.out.println("1231231212133212");
        Optional<MessageEntity> optional = messageRepository.findById(msgno);
        if(!optional.isPresent()){ return false;}
        optional.get().setIsread(true);
        return true;
    }

    // 선택된 메세지 삭제 처리 메소드
    @Transactional
    public boolean msgdelete(List<Integer> deletelist){
        if(deletelist.size()==0){return false;}
        for(int i=0; i<deletelist.size(); i++){
            Optional<MessageEntity> optional = messageRepository.findById(deletelist.get(i));
            if(optional.isPresent()){
                messageRepository.delete(optional.get());
            }else{
                return false;
            }
        }
        return true;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////



}
