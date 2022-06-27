package ezenweb.domain.member;


import ezenweb.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

    // 1. 아이디를 이용한 엔티티 검색
    Optional<MemberEntity> findBymid(String mid);

    Optional<MemberEntity> findByMemail(String memail);

}
