package ezenweb.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
@EntityListeners(AuditingEntityListener.class)  // 해당 엔티티를 감지
@MappedSuperclass // 상속받은 자식 클래스내 부모클래스의 필드  생성
@Getter@Setter@ToString
public class BaseTime {

    // JPA : 시간 감지 [레코드 생성시간, 레코드 변화시간]

    @CreatedDate
    private LocalDateTime createdate;

    @LastModifiedDate
    private LocalDateTime modifiedate;

}
