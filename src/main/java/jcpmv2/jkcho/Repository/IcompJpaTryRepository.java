package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.CompInfo;
import jcpmv2.jkcho.Domain.IPrjParticipationCompGetName;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcompJpaTryRepository extends JpaRepository<CompInfo, Long> {

    /*@Modifying
    @Query(value = "select c from CompInfo c order by c.cname, c.cboss")*/

    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update CompInfo c set c.cview=false where c.cid=?1")
    void unrealDelete(Long cid);

    void deleteByCid(Long cid);

    List<CompInfo> findAllByCid(Long cid);

    @Query(value = "select c from CompInfo c where c.cview=true order by c.cname, c.cboss")
    List<CompInfo> findWithPagination(Pageable pageable);

    @Query(value = "select c from CompInfo c where c.cview=true and c.cname like %?1% or c.cboss like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnameOrCbossPaging(String item, PageRequest of);
    @Query(value = "select c from CompInfo c where c.cview=true and c.ccall like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCcallPaging(String item, PageRequest of);
    @Query(value = "select c from CompInfo c where c.cview=true and c.cnumber like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnumberPaging(String item, PageRequest of);

    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.cname like %?1% or c.cboss like %?1%")
    Long conditionCountByCnameOrCboss(String item);
    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.ccall like %?1%")
    Long conditionCountByCcall(String item);
    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.cnumber like %?1%")
    Long conditionCountByCnumber(String item);

    @Query(value = "SELECT c.cname as cname, COUNT(t.cid) as count, t.cid as cid FROM PrjParticipationCompInfo t join" +
            " CompInfo c on t.cview=true and c.cid=t.cid and t.eview=true where t.pid=?1 GROUP BY t.cid ORDER BY c.cname")
    List<IPrjParticipationCompGetName> findParticipationComp(Long pid, PageRequest of);//프로젝트에 참여하고있는 Comp목록을 찾는
}