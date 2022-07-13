package ezenweb.service;

import ezenweb.domain.board.BoardEntity;
import ezenweb.domain.board.BoardRepository;
import ezenweb.domain.board.CategoryEntity;
import ezenweb.domain.board.CategoryRepository;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.dto.BoardDto;
import ezenweb.dto.LoginDto;
import ezenweb.dto.MemberDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BoardService {

    // Dao 호출 == Repository 호출
    @Autowired  // 자동 빈 생성 [ 자동생성자 이용한 객체에 메모리 할당 ]
    private BoardRepository boardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberRepository memberRepository;

    // 1. C [인수 : 게시물Dto]
    @Transactional
    public boolean save(BoardDto boardDto){

//        // 1. 세션 호출 [시큐리티 사용시 -> 세션 x -> 인증세션(UserDetails vs DefaultOAuth2User) ]
//        // LoginDto loginDto= (LoginDto)request.getSession().getAttribute("login");
//        // 1. 인증된 세션 호출 [시큐리티내 인증 결과 호출]
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        // 2. 인증 정보 가져오기
//        Object principal = authentication.getPrincipal();  // Principal : 인증 정보
//        // 3. 일반회원 : UserDetails / oauth회원 : DefaultOAuth2User 구분
//            // java 문법 : 자식객체 instanceof 부모클래스명 : 상속여부 확인 키워드
//        String mid = null;
//        if(principal instanceof UserDetails){   // 인증정보의 타입이 UserDetails 이면 [일반회원 검증]
//            mid = ((UserDetails) principal).getUsername();
//            System.out.println("일반 회원으로 글쓰기 ~"+principal.toString());
//        }else if(principal instanceof DefaultOAuth2User){   // 인증정보의 타입이 DefaultOAuth2User 이면 [oauth2회원 검증]
//            System.out.println("oauth회원으로 글쓰기 ~"+principal.toString());
//            Map<String,Object> map =((DefaultOAuth2User) principal).getAttributes();
//
//            if(map.get("response")!=null){  // 1. 네이버일 경우 [Attributes에 response라는 키가 존재하면]
//                Map<String,Object> map2 = (Map<String, Object>) map.get("response");
//                mid = map2.get("email").toString().split("@")[0];   // 아이디만 추출
//            }else{  // 2. 카카오일 경우
//                Map<String,Object> map2 = (Map<String, Object>) map.get("kakao_account");
//                mid = map2.get("email").toString().split("@")[0];   // 아이디만 추출
//            }
//
//        }else{  // 인증 정보가 없을경우
//            return false;
//        }
        String mid = memberService.getloginid();

        if(mid!=null){    // 로그인 되어 있으면
            // 2. 로그인된 회원의 엔티티 찾기
            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if(optionalMember.isPresent()){ // null 아니면
                // 만약에 기존에 있는 카테고리이면
                boolean categorycheck = false;
                int cno =0;
                List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
                for(CategoryEntity entity : categoryEntityList){
                    if(entity.getCname().equals(boardDto.getCategory())){
                        categorycheck = true;
                        cno = entity.getCno();
                    }
                }
                CategoryEntity categoryEntity = null;
                if(categorycheck==false){
                    // 1. 카테고리 생성
                    categoryEntity = CategoryEntity.builder()
                            .cname(boardDto.getCategory())
                            .build();
                    categoryRepository.save(categoryEntity);
                }else{
                    categoryEntity = categoryRepository.findById(cno).get();
                }

                // 3. Dto -> Entity
                BoardEntity boardEntity = boardRepository.save(boardDto.toentity());
                boardEntity.setMemberEntity(optionalMember.get());
                boardEntity.setCategoryEntity(categoryEntity);
                // 카테고리 엔티티에 게시물 연결
                categoryEntity.getBoardEntityList().add(boardEntity);
                // 회원 엔티티에 게시물 연결
                optionalMember.get().getBoardEntityList().add(boardEntity);
                return true;
            }
        }
        return false;




    }

    // 2. R 전체조회 [인수 : x 반환 : 1. JSON 2. MAP ]
    public JSONObject getboardlist(String key, String keyword,int cno, int page){
        JSONArray jsonArray = new JSONArray();
        Page<BoardEntity> boardEntities = null; // 선언만
        // Pageable : 페이지처리 관련 인터페이스
        // PageRequest : 페이징처리 관련 클래스
                // PageRequest.of(page,size) : 페이징처리 설정
                    // page = "현재페이지" 0부터 시작
                    // size = "현재페이지에 보여줄 게시물수"
                    // sort = "정렬기준" [ Sort.Direction.DESC,"정렬필드명"]
                        // sort 문제점 : 정렬 필드명에 _ 인식 불가능
        Pageable pageable = PageRequest.of(page,3, Sort.Direction.DESC,"bno");
        JSONObject object2 = new JSONObject();
        object2.put("totalpage",1);
        object2.put("startbtn",1);
        object2.put("endbtn",1);
        object2.put("jsonarray",jsonArray);
        if(cno==0){
            boardEntities = boardRepository.findBybtitle(keyword,pageable);
        }else{
            // 필드에 따른 검색 기능
            if ( key.equals("btitle")){
                boardEntities = boardRepository.findByBtitle(cno,keyword,pageable);
            }else if(key.equals("bcontent")){
                boardEntities = boardRepository.findByBcontent(cno,keyword,pageable);
            }else if(key.equals("mid")){
                // 입력받은 mid -> [mno] 엔티티 변환
                Optional<MemberEntity> optionalMember = memberRepository.findBymid(keyword);
                if(optionalMember.isPresent()){ // isPresent() : Optional이 null 아닐경우
                    MemberEntity memberEntity = optionalMember.get();
                    boardEntities = boardRepository.findAllByMno(cno,memberEntity,pageable);    // 찾은 회원 엔티티를 인수로 전달
                }else{
                    return object2;
                }
            }else{  // 검색이 없으면
                boardEntities = boardRepository.findByBtitle(cno,keyword,pageable);
            }
        }

        // 페이지에 표시할 총 페이징 버튼 개수
        int btncount = 5;
        // 페이징버튼의 시작 번호 [ (현재페이지/표시할버튼수)*표시할버튼수 + 1 ]
        int startbtn = (page/btncount)*btncount+1;
        // 페이징버튼의 끝 번호  [ 시작버튼 + 표시할버튼수 - 1
        int endbtn = startbtn + btncount-1;
        // 만약 끝번호가 마지막페이지보다 크면 끝번호는 마지막 페이지 번호로 사용
        if(endbtn>boardEntities.getTotalPages()) endbtn = boardEntities.getTotalPages();
        if(endbtn==0) endbtn=1;
        object2.put("totalpage",boardEntities.getTotalPages());
        object2.put("startbtn",startbtn);
        object2.put("endbtn",endbtn);


        for(BoardEntity boardEntity : boardEntities){
            JSONObject object = new JSONObject();
            object.put("bno",boardEntity.getBno());
            object.put("btitle",boardEntity.getBtitle());
            object.put("bcontent",boardEntity.getBcontent());
            object.put("bview",boardEntity.getBview());
            object.put("blike",boardEntity.getBlike());
            object.put("createdate",boardEntity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            object.put("modifiedate",boardEntity.getModifiedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            object.put("mid",boardEntity.getMemberEntity().getMid());
            object.put("cno",boardEntity.getCategoryEntity().getCno());
            object.put("cname",boardEntity.getCategoryEntity().getCname());
            jsonArray.put(object);
        }
        object2.put("jsonarray",jsonArray);
        return object2;
    }

    // 2. R 개별조회 [ 게시물 번호 ]
    @Transactional
    public JSONObject getboard(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        // 조회수 증가 처리 [기준 : ip / 24시간]
        String ip = request.getRemoteAddr();    // 사용자의 ip 가져오기
        // 세션이 비어있으면(24시간 내 조회 기록이 없으면)
        if(request.getSession().getAttribute(ip+bno) ==null){
            // ip 와 bno 합쳐서 세션 부여
            request.getSession().setAttribute(ip+bno,1);
            request.getSession().setMaxInactiveInterval(60*60*24);  // 세션 허용시간 [초단위]
            // 조회수 증가
            boardEntity.setBview(boardEntity.getBview()+1);
        }

        JSONObject object = new JSONObject();
        object.put("bno",boardEntity.getBno());
        object.put("btitle",boardEntity.getBtitle());
        object.put("bcontent",boardEntity.getBcontent());
        object.put("bview",boardEntity.getBview());
        object.put("blike",boardEntity.getBlike());
        object.put("createdate",boardEntity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );
        object.put("modifiedate",boardEntity.getModifiedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        object.put("mid",boardEntity.getMemberEntity().getMid());
        object.put("cno",boardEntity.getCategoryEntity().getCno());
        object.put("cname",boardEntity.getCategoryEntity().getCname());
        return object;
    }

    // 2. R 카테고리 전체 출력
    public JSONArray getcategory(){
        JSONArray jsonArray = new JSONArray();
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        for(CategoryEntity entity : categoryEntityList){
            JSONObject object = new JSONObject();
            object.put("cno",entity.getCno());
            object.put("cname",entity.getCname());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    // 3. U [인수 : 게시물 번호, 수정할 내용들 ]
    @Transactional
    public boolean update(BoardDto boardDto){
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardDto.getBno());
        BoardEntity entity = boardEntity.get();
        entity.setBtitle(boardDto.getBtitle());
        entity.setBcontent(boardDto.getBcontent());
        return true;
    }

    // 4. D [인수 : 게시물 번호 ]
    @Transactional
    public boolean delete(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        boardRepository.delete(boardEntity);
        return true;
    }
    //////////////////////////////// 크롤링 /////////////////////////////////////
    // 라이브러리
        // 1. Connection : 연결된 html 인터페이스
            // 1. Jsoup.connect(연결할 url);
            // 2. conn.get() : 연결된 html 호출
        // 2. Document : html 객체화
        // 3. Element :
        // 4. Elements
            // 1. .getElementsByTag(태그명)
            // 2. .getElementsByClass(클래스명)
            // 3. .getElementsByAttribute(속성명)
            // 4. .getElementById(아이디명)
            // 5. .get(인덱스)
            // 6. .first() : 첫번째 인덱스
            // 7. .attr(속성명) : 해당 속성의 값 호출
            // 8. .text() : html 문서내용 호출
    // 1. 날씨 크롤링
    public JSONObject getweather(){
        // 0. java : jsoup 라이브러리 그레이들 빌드
        // 1. 정보를 가지고 올 URL 작성
        String weatherurl = "https://search.daum.net/search?nil_suggest=btn&w=tot&DA=SBC&q=%EB%82%A0%EC%94%A8";
        // 2. 해당 url을 jsoup으로 연결 [jsoup은 해당 url과 연결]
        Connection conn = Jsoup.connect(weatherurl);
        try{
            // 3. 해당 url 객체로 가져오기
            Document document = conn.get();
            // 4. 특정 태그 호출
            String 지역명 = document.getElementsByClass("tit_info").first().text();
            String 상태 = document.getElementsByClass("txt_weather").first().text();
            Elements elements = document.getElementsByClass("desc_temp");
            String 온도 = elements.get(2).getElementsByClass("txt_temp").first().text();
            String 풍속 = document.getElementsByClass("dl_weather").get(0).text();
            String 습도 = document.getElementsByClass("dl_weather").get(1).text();
            String 미세먼지 = document.getElementsByClass("dl_weather").get(2).text();

            // 5. 크롤링된 정보를 json에 담기 [js에서 사용하기 위해서]
            JSONObject object = new JSONObject();
            object.put("지역명",지역명);
            object.put("상태",상태);
            object.put("온도",온도);
            object.put("풍속",풍속);
            object.put("습도",습도);
            object.put("미세먼지",미세먼지);
            return object;
        }catch(Exception e){
            System.out.println("날씨 크롤링 오류 : "+e);
        }
        return null;
    }

    // 2. 부동산 관련 뉴스 크롤링
    public JSONArray getnews(){
        String newsurl = "https://realestate.daum.net/news/all";
        Connection conn = Jsoup.connect(newsurl);
        try{
            JSONArray jsonArray = new JSONArray();
            Document document = conn.get();
            Elements elements = document.getElementsByClass("list_live");
            Elements litags = elements.first().getElementsByTag("li");
            for(int i=0; i<litags.size(); i++){
                JSONObject object = new JSONObject();
                object.put("news_title",litags.get(i).getElementsByClass("tit").first().text());
                object.put("news_content",litags.get(i).getElementsByClass("desc").first().getElementsByClass("link").first().text());
                object.put("news_info",litags.get(i).getElementsByClass("desc").first().getElementsByClass("info").first().text());
                object.put("news_img",litags.get(i).getElementsByClass("frame_thumb").attr("src"));
                object.put("news_link","https://realestate.daum.net"+litags.get(i).getElementsByClass("link_thumb").attr("href"));
                jsonArray.put(object);
            }
            return jsonArray;

        }catch(Exception e){
            System.out.println("뉴스 크롤링 오류 : "+e);
        }
        return null;
    }

    // 3. 부동산 시세 크롤링
    public void getvalue(){
        System.out.println("부동산 시세 크롤링 실행");
    }
    //////////////////////////////// 크롤링 end /////////////////////////////////////
}
