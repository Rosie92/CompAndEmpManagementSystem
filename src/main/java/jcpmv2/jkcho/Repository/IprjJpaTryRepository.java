package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.PrjInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IprjJpaTryRepository extends JpaRepository<PrjInfo, Long> {
    @Query(value = "select p from PrjInfo p where p.pview=true order by p.pname")
    List<PrjInfo> findWithPagination(Pageable pageable);

    @Query(value = "select p from PrjInfo p where p.pview=true and p.pname like %?1% order by p.pname")
    List<PrjInfo> findAllByCnameOrCbossPaging(String item, PageRequest of);

    @Query(value = "select count(p) from PrjInfo p where p.pview=true and p.pname like %?1%")
    Long conditionCountByCnameOrCboss(String item);

    @Query(value = "select p from PrjInfo p where p.pid=?1")
    List<PrjInfo> findPnameAndPcontent(Long pid);

    @Query(value = "select p from PrjInfo p where p.pid=?1")
    List<PrjInfo> findAllByPId(Long pid);

    void deleteByPid(Long pid);
}
