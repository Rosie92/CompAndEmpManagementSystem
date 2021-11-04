package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.CompInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcompJpaTryRepository extends JpaRepository<CompInfo, Long> {

    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update CompInfo c set c.cview=false where c.cid=?1")
    void unrealDelete(Long cid);

    void deleteByCid(Long cid);

    List<CompInfo> findAllByCid(Long cid);

    @Modifying
    @Query(value = "select c from CompInfo c where c.cview=true and c.cname like %?1% or c.cboss like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnameOrCboss(String item);
    @Modifying
    @Query(value = "select c from CompInfo c where c.cview=true and c.ccall like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCcall(String item);
    @Modifying
    @Query(value = "select c from CompInfo c where c.cview=true and c.cnumber like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnumber(String item);

}