package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Domain.IPrjParticipationEmpGetData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IempJpaTryRepository extends JpaRepository<EmpInfo, Long> {

    @Query(value = "select e from EmpInfo e where e.eview=true and e.ecompid=?1 order by e.ename")
    List<EmpInfo> findAllByEcompidOrderByEnamePaging(Long searchCompid, PageRequest of);
    @Query(value = "select e from EmpInfo e where e.eview=true and e.ecompid=?1 order by e.ename")
    List<EmpInfo> findAllBySearchCompidOrderByEnamePagingOff(Long searchCompid);
    @Query(value = "select e.eid as eid, e.ename as ename, e.eemail as eemail, e.ephone as ephone, e.eposition as eposition, e.eaffiliation as eaffiliation from " +
            "PrjParticipationCompInfo t join EmpInfo e on e.ecompid=?2 and t.pid=?1 and e.ecompid=t.cid and t.cview=true and t.eview=true and e.eid not in " +
            "(select t.eid from PrjParticipationCompInfo t where t.cid=?2 and t.pid=?1) group by e.eid order by e.ename")
    List<IPrjParticipationEmpGetData> findAllByCidAndPidOrderByEnamePagingOffAndParticipationEmpRemove(Long pid, Long cid);


    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update EmpInfo e set e.eview=false where e.eid=?1")
    void unrealDelete(Long eid);

    void deleteByEid(Long eid);

    void deleteByEcompid(Long cid);

    List<EmpInfo> findAllByEid(Long eid);

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

    @Query(value = "SELECT e.eid as eid, e.ename as ename, e.eemail as eemail, e.ephone as ephone, e.eposition as eposition, e.eaffiliation as eaffiliation " +
            "FROM PrjParticipationCompInfo t join EmpInfo e on t.cid=?2 AND t.pid=?1 AND e.ecompid=t.cid AND e.eid=t.eid AND t.cview=true AND " +
            "t.eview=true ORDER BY e.ename")
    List<IPrjParticipationEmpGetData> findParticipationComp(Long pid, Long cid, PageRequest of);

    Optional<EmpInfo> findByEnameAndEphoneAndEcompid(String ename, String ephone, Long searchCompid);

    @Query(value = "select count(e) from EmpInfo e where e.ecompid=?1")
    Long defaultEmpListCount(Long searchCompid);
}
