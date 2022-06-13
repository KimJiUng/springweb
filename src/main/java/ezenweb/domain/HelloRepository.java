package ezenweb.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<HelloEntity,Long> {
                                                    // 엔티티명, 엔티티 pk 자료형
}

// Repository <----> Dao