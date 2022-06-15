package ezenweb.domain.room;

import ezenweb.domain.room.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity,Integer> {
}
