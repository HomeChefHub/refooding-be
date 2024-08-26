package refooding.api.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import refooding.api.domain.fridge.entity.Fridge;

import java.util.Optional;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
    Optional<Fridge> findFridgeByMemberId(Long memberId);
    @Query("select f from Fridge f " +
            "join fetch f.member m " +
            "where m.deletedDate is null")
    Optional<Fridge> findFridgeWithMemberByMemberId(Long memberId);
}
