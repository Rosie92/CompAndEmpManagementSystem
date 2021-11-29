package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.PrjParticipationCompInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IprjParticipationCompJpaRepository extends JpaRepository<PrjParticipationCompInfo, Long> {

    // 프로젝트에 이미 참여한 회사인지 중복 체크할 때 사용
    Optional<PrjParticipationCompInfo> findTopByCidAndPid(Long cid, Long pid);

    void deleteByPid(Long pid);

    @Modifying
    @Query(value = "update PrjParticipationCompInfo t set t.cview=false where t.pid=?1 and t.cid=?2")
    void prjCompUnrealDelete(Long pid, Long cid);

    @Modifying
    @Query(value = "delete from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2")
    void deleteByPidAndCid(Long pid, Long cid);

    @Modifying
    @Query(value = "update PrjParticipationCompInfo t set t.eview=false where t.pid=?1 and t.cid=?2 and t.eid=?3")
    void prjEmpUnrealDelete(Long pid, Long cid, Long eid);

    @Modifying
    @Query(value = "delete from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2 and t.eid=?3")
    void deleteByEid(Long pid, Long cid, Long eid);

    @Query(value = "select count(t) from PrjParticipationCompInfo t where t.pid=?1 and t.cview=true")
    Long getCountData(Long pid);
    @Query(value = "select count(t) from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2 and t.eview=true")
    Long getEmpCountData(Long pid, Long cid);
}
