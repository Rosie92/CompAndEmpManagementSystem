package jcpmv2.jkcho.Repository;

import jcpmv2.jkcho.Domain.CompInfo;
import jcpmv2.jkcho.Interface.IPrjParticipationCompGetName;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcompJpaTryRepository extends JpaRepository<CompInfo, Long> {

    /*--------------------------------------SELECT--------------------------------------*/
    // 회사 전체 리스트 검색, 가삭제 되어있지 않는(cview=true) 회사의 정보를 회사명,대표자명 순으로 정렬하여 모두 가져옴
    @Query(value = "select c from CompInfo c where c.cview=true order by c.cname, c.cboss")
    List<CompInfo> findWithPagination(Pageable pageable);

    /*
        회사 조건 리스트 검색(검색 조건: 회사명or대표자명, 검색어: String item)
        가삭제 여부 확인(cview=true), 검색어가 포함된 모든 정보 허용(%like%), 오름차순 정렬(order by cname,cboss)
    */
    @Query(value = "select c from CompInfo c where c.cview=true and c.cname like %?1% or c.cboss like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnameOrCbossPaging(String item, PageRequest of);
    /*
        회사 조건 리스트 검색(검색 조건: 대표전화, 검색어: String item)
        가삭제 여부 확인(cview=true), 검색어가 포함된 모든 정보 허용(%like%), 오름차순 정렬(order by cname,cboss)
    */
    @Query(value = "select c from CompInfo c where c.cview=true and c.ccall like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCcallPaging(String item, PageRequest of);
    /*
        회사 조건 리스트 검색(검색 조건: 법인번호, 검색어: String item)
        가삭제 여부 확인(cview=true), 검색어가 포함된 모든 정보 허용(%like%), 오름차순 정렬(order by cname,cboss)
    */
    @Query(value = "select c from CompInfo c where c.cview=true and c.cnumber like %?1% order by c.cname, c.cboss")
    List<CompInfo> findAllByCnumberPaging(String item, PageRequest of);

    // ---------------------- 회사 조건 리스트 페이징을 위한 count를 구함 ----------------------
    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.cname like %?1% or c.cboss like %?1%")
    Long conditionCountByCnameOrCboss(String item);
    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.ccall like %?1%")
    Long conditionCountByCcall(String item);
    @Query(value = "select count(c) from CompInfo c where c.cview=true and c.cnumber like %?1%")
    Long conditionCountByCnumber(String item);
    // -----------------------------------------------------------------------------------

    //프로젝트에 참여하고 있는 회사들의 목록과 그 회사에서 프로젝트에 참여하고 있는 직원 수를 찾음, 프로젝트참여정보 테이블과 회사 테이블 조인
    @Query(value = "SELECT c.cname as cname, COUNT(t.cid) as count, t.cid as cid FROM PrjParticipationCompInfo t join" +
            " CompInfo c on t.cview=true and c.cid=t.cid and t.eview=true where t.pid=?1 GROUP BY t.cid ORDER BY c.cname")
    List<IPrjParticipationCompGetName> findParticipationComp(Long pid, PageRequest of);

    /*--------------------------------------UPDATE--------------------------------------*/
    // 회사 업데이트 수행 전 회사 정보 가져오기, 회사id(cid)가 일치하는 회사 정보를 모두 가져옴
    List<CompInfo> findAllByCid(Long cid);

    /*--------------------------------------DELETE--------------------------------------*/
    // 회사 가삭제, 회사id(cid)가 일치하는 회사의 cview를 true>false로 변경
    @Modifying // Not supported for DML operations 에러 발생 시 해결 방법
    @Query(value = "update CompInfo c set c.cview=false where c.cid=?1")
    void unrealDelete(Long cid);

    // 회사 실삭제, 회사id(cid)가 일치하는 회사를 DB에서 삭제
    void deleteByCid(Long cid);

}