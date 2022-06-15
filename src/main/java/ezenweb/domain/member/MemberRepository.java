package ezenweb.domain.member;


import ezenweb.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {
}
