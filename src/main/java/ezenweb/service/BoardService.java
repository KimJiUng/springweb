package ezenweb.service;

import ezenweb.domain.board.BoardEntity;
import ezenweb.domain.board.BoardRepository;
import ezenweb.dto.BoardDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    // Dao 호출 == Repository 호출
    @Autowired  // 자동 빈 생성 [ 자동생성자 이용한 객체에 메모리 할당 ]
    private BoardRepository boardRepository;


    // 1. C [인수 : 게시물Dto]
    @Transactional
    public boolean save(BoardDto boardDto){
        // 1. 특정 로직

        // 2. Dto -> Entity
        int bno = boardRepository.save(boardDto.toentity()).getBno();
        if(bno>=1){ return true;}
        else{return false;}
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
            object.put("createdate",boardEntity.getCreatedate());
            object.put("modifiedate",boardEntity.getModifiedate());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    // 2. R 개별조회 [ 게시물 번호 ]
    public JSONObject getboard(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        JSONObject object = new JSONObject();
        object.put("bno",boardEntity.getBno());
        object.put("btitle",boardEntity.getBtitle());
        object.put("bcontent",boardEntity.getBcontent());
        object.put("bview",boardEntity.getBview());
        object.put("blike",boardEntity.getBlike());
        object.put("createdate",boardEntity.getCreatedate());
        object.put("modifiedate",boardEntity.getModifiedate());
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
