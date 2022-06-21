package ezenweb.service;

import ezenweb.domain.board.BoardEntity;
import ezenweb.domain.board.BoardRepository;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.dto.BoardDto;
import ezenweb.dto.LoginDto;
import ezenweb.dto.MemberDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    // Dao 호출 == Repository 호출
    @Autowired  // 자동 빈 생성 [ 자동생성자 이용한 객체에 메모리 할당 ]
    private BoardRepository boardRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberRepository memberRepository;

    // 1. C [인수 : 게시물Dto]
    @Transactional
    public boolean save(BoardDto boardDto){

        // 1. 세션 호출
        LoginDto loginDto= (LoginDto)request.getSession().getAttribute("login");
        if(loginDto!=null){    // 로그인 되어 있으면
            // 2. 로그인된 회원의 엔티티 찾기
            Optional<MemberEntity> optionalMember = memberRepository.findById(loginDto.getMno());
            if(optionalMember.isPresent()){ // null 아니면
                // 3. Dto -> Entity
                BoardEntity boardEntity = boardRepository.save(boardDto.toentity());
                boardEntity.setMemberEntity(optionalMember.get());
                return true;
            }
        }
        return false;




    }

    // 2. R 전체조회 [인수 : x 반환 : 1. JSON 2. MAP ]
    public JSONArray getboardlist(){
        JSONArray jsonArray = new JSONArray();
        List<BoardEntity> blist = boardRepository.findAll();
        for(BoardEntity boardEntity : blist){
            JSONObject object = new JSONObject();
            object.put("bno",boardEntity.getBno());
            object.put("btitle",boardEntity.getBtitle());
            object.put("bcontent",boardEntity.getBcontent());
            object.put("bview",boardEntity.getBview());
            object.put("blike",boardEntity.getBlike());
            object.put("createdate",boardEntity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            object.put("modifiedate",boardEntity.getModifiedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            object.put("mid",boardEntity.getMemberEntity().getMid());
            jsonArray.put(object);
        }
        return jsonArray;
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
        return object;
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


}
