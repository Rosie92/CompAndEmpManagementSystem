package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.PrjParticipationCompInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IprjParticipationCompJpaRepository extends JpaRepository<PrjParticipationCompInfo, Long> {

    // 프로젝트에 이미 참여한 회사인지 중복 체크할 때 사용
    Optional<PrjParticipationCompInfo> findTopByCidAndPid(Long cid, Long pid);
}
