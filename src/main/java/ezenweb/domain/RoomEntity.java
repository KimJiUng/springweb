package ezenweb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String roomname;
    private String x;
    private String y;
}
