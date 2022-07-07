package ezenweb.domain.member;


import ezenweb.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

    // 1. 아이디를 이용한 엔티티 검색
    Optional<MemberEntity> findBymid(String mid);

    // 2. 이메일을 이용한 엔티티 검색
    Optional< MemberEntity > findBymemail( String email );

    // 3. 아이디와 이메일이 동일한 엔티티 검색
    @Query(value = "select * from member where mname=:mname and memail=:memail", nativeQuery = true)
    Optional<MemberEntity> findid(@Param("mname") String mname, @Param("memail") String memail);

    @Query(value = "select * from member where mid=:mid and memail=:memail", nativeQuery = true)
    Optional<MemberEntity> findpw(@Param("mid") String mid, @Param("memail") String memail);

}
