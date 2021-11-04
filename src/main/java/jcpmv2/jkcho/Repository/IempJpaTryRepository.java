package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.EmpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IempJpaTryRepository extends JpaRepository<EmpInfo, Long> {
    /*@Query(value = "select c.cname, e.ename, e.eemail, e.ephone, e.eposition, e.eaffiliation from CompInfo c, EmpInfo e where c.cid = e.ecompid and e.ecompid=?1 order by e.ename desc")*/
    List<EmpInfo> findAllByEcompidOrderByEname(Long searchCompid);

    Optional<EmpInfo> findByEnameAndEphone(String ename, String ephone);

    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update EmpInfo e set e.ename=?2, e.eemail=?3, e.ephone=?4, e.eposition=?5, e.eaffiliation=?6 where e.eid=?1")
    void update(Long eid, String ename, String eemail, String ephone, String eposition, String eaffiliation);

    @Modifying
    @Query(value = "update EmpInfo e set e.eview=false where e.eid=?1")
    void unrealDelete(Long eid);

    void deleteByEid(Long eid);

    void deleteByEcompid(Long cid);

    List<EmpInfo> findAllByEid(Long eid);

    @Modifying
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.ename like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEname(String item, Long searchCompid);
    @Modifying
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eemail like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEemail(String item, Long searchCompid);
    @Modifying
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.ephone like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEphone(String item, Long searchCompid);
    @Modifying
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eposition like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEposition(String item, Long searchCompid);
    @Modifying
    @Query(value = "select e from EmpInfo e where e.ecompid=?2 and e.eaffiliation like %?1% and e.eview=true order by e.ename")
    List<EmpInfo> findAllByEaffiliation(String item, Long searchCompid);

}
