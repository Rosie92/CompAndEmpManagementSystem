package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.EmpInfo;
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

    Optional<EmpInfo> findByEnameAndEphone(String ename, String ephone);

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

    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.ename like %?1%")
    Long conditionCountByEname(String item);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eemail like %?1%")
    Long conditionCountByEemail(String item);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.ephone like %?1%")
    Long conditionCountByEphone(String item);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eposition like %?1%")
    Long conditionCountByEposition(String item);
    @Query(value = "select count(e) from EmpInfo e where e.eview=true and e.eaffiliation like %?1%")
    Long conditionCountByEaffiliation(String item);
}
