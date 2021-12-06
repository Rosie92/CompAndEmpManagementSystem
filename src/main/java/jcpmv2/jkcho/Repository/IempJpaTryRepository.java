package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Interface.IPrjParticipationEmpGetData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IempJpaTryRepository extends JpaRepository<EmpInfo, Long> {
    /*--------------------------------------SELECT--------------------------------------*/
    // 직원 전체 리스트 검색
    @Query(value = "select e from EmpInfo e where e.eview=true and e.ecompid=?1 order by e.ename")
    List<EmpInfo> findAllByEcompidOrderByEnamePaging(Long searchCompid, PageRequest of);

    // 프로젝트에 참여하고 있는 직원을 제외한 나머지 직원 전체 리스트
    @Query(value = "select e.eid as eid, e.ename as ename, e.eemail as eemail, e.ephone as ephone, e.eposition as eposition, e.eaffiliation as eaffiliation from " +
            "PrjParticipationCompInfo t join EmpInfo e on e.ecompid=?2 and t.pid=?1 and e.ecompid=t.cid and t.cview=true and t.eview=true and e.eid not in " +
            "(select t.eid from PrjParticipationCompInfo t where t.cid=?2 and t.pid=?1) group by e.eid order by e.ename")
    List<IPrjParticipationEmpGetData> findAllByCidAndPidOrderByEnamePagingOffAndParticipationEmpRemove(Long pid, Long cid, PageRequest of);

    // 직원 조건 리스트 검색
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.ename like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEname(String item, Long searchCompid, PageRequest of);
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eemail like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEemail(String item, Long searchCompid, PageRequest of);
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.ephone like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEphone(String item, Long searchCompid, PageRequest of);
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eposition like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEposition(String item, Long searchCompid, PageRequest of);
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eaffiliation like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEaffiliation(String item, Long searchCompid, PageRequest of);

    // 직원 조건 리스트 검색 시 페이징을 위한 count 구하기
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.ename like %?1% and e.ecompid=?2")
    Long conditionCountByEname(String item, Long searchCompid);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eemail like %?1% and e.ecompid=?2")
    Long conditionCountByEemail(String item, Long searchCompid);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.ephone like %?1% and e.ecompid=?2")
    Long conditionCountByEphone(String item, Long searchCompid);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eposition like %?1% and e.ecompid=?2")
    Long conditionCountByEposition(String item, Long searchCompid);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eaffiliation like %?1% and e.ecompid=?2")
    Long conditionCountByEaffiliation(String item, Long searchCompid);

    // 회사에서 해당 프로젝트에 참여하고 있는 직원 목록을 검색
    @Query(value = "SELECT e.eid as eid, e.ename as ename, e.eemail as eemail, e.ephone as ephone, e.eposition as eposition, e.eaffiliation as eaffiliation " +
            "FROM PrjParticipationCompInfo t join EmpInfo e on t.cid=?2 AND t.pid=?1 AND e.ecompid=t.cid AND e.eid=t.eid AND t.cview=true AND " +
            "t.eview=true ORDER BY e.ename")
    List<IPrjParticipationEmpGetData> findParticipationComp(Long pid, Long cid, PageRequest of);

    // 페이징을 위한 count 구하기
    @Query(value = "select count(e) from EmpInfo e where e.ecompid=?1")
    Long defaultEmpListCount(Long Cid);

    /*--------------------------------------CREATE--------------------------------------*/
    // 직원 신규 등록 전 이미 존재하는 직원인지 중복 체크
    Optional<EmpInfo> findByEnameAndEphoneAndEcompid(String ename, String ephone, Long searchCompid);

    /*--------------------------------------UPDATE--------------------------------------*/
    // 업데이트 진행 전, 해당 직원의 정보를 불러옴
    List<EmpInfo> findAllByEid(Long eid);

    /*--------------------------------------DELETE--------------------------------------*/
    // 직원 가삭제
    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update EmpInfo e set e.eview=false where e.eid=?1")
    void unrealDelete(Long eid);

    // 직원 실삭제
    void deleteByEid(Long eid);

    // 해당 회사id를 갖는 모든 직원을 삭제 (회사 삭제 시 실행)
    void deleteByEcompid(Long cid);

}
