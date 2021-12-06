package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Interface.IPrjInCompCountListData;
import jcpmv2.jkcho.Domain.PrjParticipationCompInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IprjParticipationCompJpaRepository extends JpaRepository<PrjParticipationCompInfo, Long> {
    /*--------------------------------------SELECT--------------------------------------*/
    // 프로젝트에 이미 참여한 회사인지 중복 체크할 때 사용
    Optional<PrjParticipationCompInfo> findTopByCidAndPid(Long cid, Long pid);

    // 회사에서 해당 프로젝트에 참여하고 있는 직원 목록을 검색 시, 페이징을 위한 count
    @Query(value = "select count(t) from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2 and t.eview=true")
    Long getEmpCountData(Long pid, Long cid);

    // 프로젝트에 참여중인 회사 목록 검색 시, 페이징을 위한 count
    @Query(value = "select count(t.cid) from PrjParticipationCompInfo t where t.pid=?1 and t.cview=true group by t.cid")
    List<IPrjInCompCountListData> getCountData(Long pid);

    // 프로젝트에 참가하고 있는 직원을 제외한 나머지 직원 전체 목록을 검색 시, 페이징을 위한 count
    @Query(value = "select count(t.eid) from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2")
    Long empListCountParticiRemove(Long pid, Long cid);

    /*--------------------------------------CREATE--------------------------------------*/
    /*--------------------------------------UPDATE--------------------------------------*/
    /*--------------------------------------DELETE--------------------------------------*/
    // 프로젝트 정보 실삭제
    void deleteByPid(Long pid);

    // 프로젝트에서 해당 참여 회사를 가삭제
    @Modifying
    @Query(value = "update PrjParticipationCompInfo t set t.cview=false where t.pid=?1 and t.cid=?2")
    void prjCompUnrealDelete(Long pid, Long cid);

    // 프로젝트에서 해당 참여 회사를 실삭제
    @Modifying
    @Query(value = "delete from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2")
    void deleteByPidAndCid(Long pid, Long cid);

    // 프로젝트에 참여하고 있는 회사의 해당 직원을 가삭제
    @Modifying
    @Query(value = "update PrjParticipationCompInfo t set t.eview=false where t.pid=?1 and t.cid=?2 and t.eid=?3")
    void prjEmpUnrealDelete(Long pid, Long cid, Long eid);

    // 프로젝트에 참여하고 있는 회사의 해당 직원을 실삭제
    @Modifying
    @Query(value = "delete from PrjParticipationCompInfo t where t.pid=?1 and t.cid=?2 and t.eid=?3")
    void deleteByEid(Long pid, Long cid, Long eid);

}
