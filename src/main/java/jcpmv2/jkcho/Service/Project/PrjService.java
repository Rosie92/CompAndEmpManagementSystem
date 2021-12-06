package jcpmv2.jkcho.Service.Project;

import jcpmv2.jkcho.Domain.*;
import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.CompPrjParticiDataDto;
import jcpmv2.jkcho.Dto.Emp.EmpTableDataDto;
import jcpmv2.jkcho.Dto.Project.*;
import jcpmv2.jkcho.Error.Model.DuplicateException;
import jcpmv2.jkcho.Interface.IPrjInCompCountListData;
import jcpmv2.jkcho.Interface.IPrjParticipationCompGetName;
import jcpmv2.jkcho.Interface.IPrjParticipationEmpGetData;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IcompJpaTryRepository;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
import jcpmv2.jkcho.Repository.IprjJpaTryRepository;
import jcpmv2.jkcho.Repository.IprjParticipationCompJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PrjService {
    @Autowired
    private IprjJpaTryRepository iprjJpaTryRepository;
    @Autowired
    private IprjParticipationCompJpaRepository iprjParticipationCompJpaRepository;
    @Autowired
    private IcompJpaTryRepository icompJpaTryRepository;
    @Autowired
    private IempJpaTryRepository iempJpaTryRepository;

    /*--------------------------------------SELECT--------------------------------------*/
    public ListDto<PrjTableDataDto> findAll(PagingDto pagingDto) { // 프로젝트 전체 리스트 검색
        /*
            PrjInfo를 제네릭으로 설정하여 해당 테이블 엔티티의 각 객체에 맞게 데이터를 가져옴
            Repository를 통해 받아온 PrjList안의 객체들은 jpa와 연동된 영속체 객체
            해당 리스트의 객체들을 활용하기 위해 필요한 것만 추출하여 리스트 담아야함
            PrjList에서 필요한 객체들을 골라 PrjListData의 각 객체에 매핑시키는데 코드가 너무 길어지며
            매번 코드마다 타입도 다를것임. 때문에 QsolModelMapper라는 ModelMapper를 작성하여 모든 코드에서 공통적으로 처리할 수 있도록 함
            이 때 영속성 객체가 담겨있는 리스트 PrjList를 소스로<S>, 타입에 대하여 명시되어 있는 PrjTableDataDto를 타입으로<T> 넘김
            그리하여 PrjTableDataDto를 제네렉으로 갖고있는 리스트 PrjlistData에 데이터를 재정립
         */
        // PageRequest.of( 페이징 처리시 0부터 사이즈 10개씩, JSP에서 pageNo-1을 해준 이유 )
        List<PrjInfo> PrjList = iprjJpaTryRepository.findWithPagination(PageRequest.of(0 + pagingDto.getPageNo(), 10));
        Long listCount = iprjJpaTryRepository.count();
        if (PrjList.size() == 0) {
            return null;
        } else {
            List<PrjTableDataDto> PrjListData = QsolModelMapper.map(PrjList, PrjTableDataDto.class);
            /*
            ListDto.에 PrjTableDataDto를 제네릭으로 설정하여 ListDto의 객체들과 타입 매핑,
            이후 builder를 통해 set
         */
            return ListDto.<PrjTableDataDto>builder()
                    .list(PrjListData)
                    .listCount(listCount)
                    .build();
        }
    }

    @Transactional
    public ListDto<PrjTableDataDto> listConditionSearch(PrjConditionSearchDataDto prjConditionSearchDataDto) { // 프로젝트 조건 리스트 검색
        /*
            전체 리스트 검색과 마찬가지로 변수를 생성
            if 안에서 변수를 사용하기 위해 if 밖에서 미리 변수를 만들어 초기화해둠
         */
        List<PrjInfo> PrjList = null;
        Long ConditionCount = null;
        // 조건 검색 시, 지정된 조건에 따라 검색어를 찾음
        if (prjConditionSearchDataDto.getCondition().equals("pname")) {
            PrjList = iprjJpaTryRepository.findAllByCnameOrCbossPaging(prjConditionSearchDataDto.getItem(), PageRequest.of(0 + prjConditionSearchDataDto.getPageNo(), 10));
            ConditionCount = iprjJpaTryRepository.conditionCountByCnameOrCboss(prjConditionSearchDataDto.getItem());
        }
        List<PrjTableDataDto> PrjListData = QsolModelMapper.map(PrjList, PrjTableDataDto.class);
        return ListDto.<PrjTableDataDto>builder()
                .list(PrjListData)
                .listCount(ConditionCount)
                .build();
    }

    public ListDto<PrjTableDataDto> findPnameAndPcontent(PrjTableDataDto prjTableDataDto) { // 프로젝트 상세 모달창 오픈 전, pname+pcontent select
        List<PrjInfo> prjData = iprjJpaTryRepository.findPnameAndPcontent(prjTableDataDto.getPid());
        List<PrjTableDataDto> PrjListData = QsolModelMapper.map(prjData, PrjTableDataDto.class);
        return ListDto.<PrjTableDataDto>builder()
                .list(PrjListData)
                .build();
    }

    public ListDto<CompPrjParticiDataDto> findParticipationComp(PrjIdDataDto prjIdDataDto) { // 프로젝트에 참여중인 회사 목록 검색
        /*
            프로젝트에 참여중인 회사 목록 데이터들을 가져오기 위해 쿼리에서 조인을 사용함
            조인이 사용되었기 때문에 List의 제네릭으로 특정 도메인을 사용하기 어려움
            가져올 객체들을 데이터로 활용하기 위하여 IPrjParticipationCompGetName 인터페이스를 구현하고
            필요 데이터들을 받을 객체들을 생성하여 받아오고 사용함
        */
        List<IPrjParticipationCompGetName> prjCompList = icompJpaTryRepository.findParticipationComp(prjIdDataDto.getPid(), PageRequest.of(0 + prjIdDataDto.getPageNo(), 10));
        // IPrjInCompCountListData 인터페이스 구현 및 사용, 페이징을 위해 프로젝트에 참여중인 회사들을 찾고 size를 구함
        List<IPrjInCompCountListData> count = iprjParticipationCompJpaRepository.getCountData(prjIdDataDto.getPid());
        int listCount2 = count.size();
        if (prjCompList.size() == 0) {
            return null;
        } else {
            List<CompPrjParticiDataDto> prjListData = QsolModelMapper.map(prjCompList, CompPrjParticiDataDto.class);
            return ListDto.<CompPrjParticiDataDto>builder()
                    .list(prjListData)
                    .listCount2(listCount2)
                    /*.compid(PrjListData.get(0).getEcompid())*/
                    .build();
        }
    }

    @Transactional
    public void prjAddCompDeplicateCheck(PrjDuplicateCheckDto prjDuplicateCheckDto) {
        Optional<PrjParticipationCompInfo> duplicateCid = iprjParticipationCompJpaRepository.findTopByCidAndPid(prjDuplicateCheckDto.getCid(), prjDuplicateCheckDto.getPid());
        if (duplicateCid.isPresent()) {
            throw new DuplicateException();
        }
    }

    public ListDto<EmpTableDataDto> compParticiEmpSearch(PrjIdDataDto prjIdDataDto) { // 회사에서 해당 프로젝트에 참여하고 있는 직원 목록을 검색
        List<IPrjParticipationEmpGetData> prjEmpList = iempJpaTryRepository.findParticipationComp(prjIdDataDto.getPid(), prjIdDataDto.getCid(), PageRequest.of(0 + prjIdDataDto.getPageNo(), 10));
        Long listCount = iprjParticipationCompJpaRepository.getEmpCountData(prjIdDataDto.getPid(), prjIdDataDto.getCid());
        if (prjEmpList.size() == 0) {
            return null;
        } else {
            List<EmpTableDataDto> prjListData = QsolModelMapper.map(prjEmpList, EmpTableDataDto.class);
            return ListDto.<EmpTableDataDto>builder()
                    .list(prjListData)
                    .listCount(listCount)
                    /*.compid(PrjListData.get(0).getEcompid())*/
                    .build();
        }
    }


    /*--------------------------------------CREATE--------------------------------------*/
    @Transactional
    public void create(PrjTableDataDto prjTableDataDto) { // 프로젝트 신규 생성
        PrjInfo prjInfo = new PrjInfo();
        prjInfo.setPname(prjTableDataDto.getPname());
        prjInfo.setPcontent(prjTableDataDto.getPcontent());
        prjInfo.setPview(prjTableDataDto.getPview());
        iprjJpaTryRepository.save(prjInfo);
    }

    public void prjAddCompEmpLastStep(PrjAddCompEmpDataDto prjAddCompEmpDataDto) { // 신규 회사 및 직원 & 직원 추가 등록
        int count = 0;
        while (count < prjAddCompEmpDataDto.getEid().length) {
            PrjParticipationCompInfo prjParticipationCompInfo = new PrjParticipationCompInfo();
            prjParticipationCompInfo.setPid(prjAddCompEmpDataDto.getPid());
            prjParticipationCompInfo.setCid(prjAddCompEmpDataDto.getCid());
            prjParticipationCompInfo.setEid(prjAddCompEmpDataDto.getEid()[count]);
            prjParticipationCompInfo.setCview(prjAddCompEmpDataDto.getCview());
            prjParticipationCompInfo.setEview(prjAddCompEmpDataDto.getEview());
            iprjParticipationCompJpaRepository.save(prjParticipationCompInfo);
            count++;
        }
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    public ListDto<PrjTableDataDto> updatePrjStep(PrjIdDataDto prjIdDataDto) { // 업데이트 진행 전, 해당 프로젝트의 정보를 불러옴
        List<PrjInfo> prjList = iprjJpaTryRepository.findAllByPId(prjIdDataDto.getPid());
        List<PrjTableDataDto> prjListData = QsolModelMapper.map(prjList, PrjTableDataDto.class);
        return  ListDto.<PrjTableDataDto>builder()
                .list(prjListData)
                .build();
    }

    /*@Transactional*/
    public void updatePrjTry(PrjTableDataDto prjTableDataDto) { // 프로젝트 정보 업데이트 실시
        // Dirty Checking 활용
        // 프로젝트id(pid)를 통해 DB와 통신, 영속성으로 연결되며 이 상태에서 정보가 set되면 자동으로 DB에 반영됨
        PrjInfo prjInfo = iprjJpaTryRepository.findById(prjTableDataDto.getPid()).orElse(null);
        prjInfo.setPname(prjTableDataDto.getPname());
        prjInfo.setPcontent(prjTableDataDto.getPcontent());
    }

    /*--------------------------------------DELETE--------------------------------------*/
    public void unrealDelete(Long pid) { // 프로젝트 정보 가삭제
        // DirtyChecking 활용
        PrjInfo prjInfo = iprjJpaTryRepository.findById(pid).orElse(null);
        prjInfo.setPview(false);
    }

    @Transactional
    public void realDelete(Long pid) { // 프로젝트 정보 실삭제
        iprjParticipationCompJpaRepository.deleteByPid(pid);
        iprjJpaTryRepository.deleteByPid(pid);
    }

    public void prjCompUnrealDelete(PrjTableDataDto prjTableDataDto) { // 프로젝트에서 해당 참여 회사를 가삭제
        iprjParticipationCompJpaRepository.prjCompUnrealDelete(prjTableDataDto.getPid(), prjTableDataDto.getCid());
    }

    @Transactional
    public void prjCompRealDelete(PrjTableDataDto prjTableDataDto) { // 프로젝트에서 해당 참여 회사를 실삭제
        iprjParticipationCompJpaRepository.deleteByPidAndCid(prjTableDataDto.getPid(), prjTableDataDto.getCid());
    }


    public void prjEmpUnrealDelete(PrjEidToDeleteDataDto prjEidToDeleteDataDto) { // 프로젝트에 참여하고 있는 회사의 해당 직원을 가삭제
        iprjParticipationCompJpaRepository.prjEmpUnrealDelete(prjEidToDeleteDataDto.getPid(), prjEidToDeleteDataDto.getCid(), prjEidToDeleteDataDto.getEid());
    }

    @Transactional
    public void prjEmpRealDelete(PrjEidToDeleteDataDto prjEidToDeleteDataDto) { // 프로젝트에 참여하고 있는 회사의 해당 직원을 실삭제
        iprjParticipationCompJpaRepository.deleteByEid(prjEidToDeleteDataDto.getPid(), prjEidToDeleteDataDto.getCid(), prjEidToDeleteDataDto.getEid());
    }
}
