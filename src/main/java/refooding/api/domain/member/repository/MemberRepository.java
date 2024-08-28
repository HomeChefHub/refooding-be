package refooding.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import refooding.api.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 삭제되지 않은 모든 회원 조회
    List<Member> findAllByDeletedAtIsNull();

    // 삭제되지 않은 회원 ID로 조회
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 회원 중에서 이름으로 검색
    Optional<Member> findByNameAndDeletedAtIsNull(String name);

}
