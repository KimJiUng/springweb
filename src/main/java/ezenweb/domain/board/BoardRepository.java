package ezenweb.domain.board;

import ezenweb.domain.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {
    // DAO 역할

    // 1. 검색 메소드
        // 1. .findAll() : 모든 엔티티 호출
        // 2. .findbyId(pk값) : 해당 pk의 엔티티 호출
        // 3. .findby필드명(값) : 해당 필드명에서 값에 해당하는 하나 엔티티 호출 Optional
        // 4. .findAllby필드명(값) : 해당 필드명에서 값에 해당하는 여러개 엔티티 호출 List<엔티티명>
        // 5. 직접 쿼리작성 : @Query(value="쿼리문작성", nativeQuery=true)
                // SQL에 변수 넣기
                    // * 필드명은 변수로 불가능
                    // * @Param 생략 가능
                    // :변수명 , ?인수순서번호
                    // 1. 인수로 @Param("변수명") 자료형 변수명 -> [SQL] : 변수명
                    // 2. 인수로 @Param("변수명") 엔티티 변수명 -> [SQL] : #{#엔티티명.필드명}
    // 1. 제목 검색
        // 1. sql 없이
        //List<BoardEntity> findByBtitle(String keyword);
        // 2. sql 적용
    @Query(value = "select * from board where cno=:cno and btitle like %:keyword%", nativeQuery = true)
    Page<BoardEntity> findByBtitle(@Param("cno") int cno, @Param("keyword") String keyword, Pageable pageable);

    @Query( value = "select * from board where btitle like %:keyword%" , nativeQuery = true )
    Page<BoardEntity> findBybtitle(@Param("keyword")  String keyword,Pageable pageable  );


    // 2. 내용 검색
    @Query(value = "select * from board where cno=:cno and bcontent like %:keyword%", nativeQuery =true)
    Page<BoardEntity> findByBcontent(@Param("cno") int cno, @Param("keyword") String keyword,Pageable pageable);

    // 3. 작성자 검색
    @Query(value = "select * from board where cno=:cno and mno = :#{#memberEntity.mno}", nativeQuery = true)
    Page<BoardEntity> findAllByMno(@Param("cno") int cno, @Param("memberEntity") MemberEntity memberEntity,Pageable pageable);
}
