package ezenweb.domain.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;

    private String mid;

    private String mpassword;

    private String mname;

}
