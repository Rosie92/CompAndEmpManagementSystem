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
    /*--------------------------------------SELECT--------------------------------------*/
    // 프로젝트 전체 목록
    @Query(value = "select p from PrjInfo p where p.pview=true order by p.pname")
    List<PrjInfo> findWithPagination(Pageable pageable);

    // 프로젝트 조건 검색
    @Query(value = "select p from PrjInfo p where p.pview=true and p.pname like %?1% order by p.pname")
    List<PrjInfo> findAllByCnameOrCbossPaging(String item, PageRequest of);

    // 프로젝트 조건 검색 후 페이징을 위한 count
    @Query(value = "select count(p) from PrjInfo p where p.pview=true and p.pname like %?1%")
    Long conditionCountByCnameOrCboss(String item);

    // 프로젝트 상세 테이블 오픈 전 정보 가져오기
    @Query(value = "select p from PrjInfo p where p.pid=?1")
    List<PrjInfo> findPnameAndPcontent(Long pid);

    /*--------------------------------------CREATE--------------------------------------*/
    /*--------------------------------------UPDATE--------------------------------------*/
    // 업데이트 진행 전 해당 프로젝트 정보 가져오기
    @Query(value = "select p from PrjInfo p where p.pid=?1")
    List<PrjInfo> findAllByPId(Long pid);

    /*--------------------------------------DELETE--------------------------------------*/
    // 프로젝트 실삭제
    void deleteByPid(Long pid);
}
