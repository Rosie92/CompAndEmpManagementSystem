package jcpmv2.jkcho.Service.Project;

import jcpmv2.jkcho.Domain.*;
import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.CompPrjParticiDataDto;
import jcpmv2.jkcho.Dto.Emp.EmpTableDataDto;
import jcpmv2.jkcho.Dto.Project.*;
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

import java.util.ArrayList;
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

    public ListDto<PrjTableDataDto> findAll(PagingDto pagingDto) {
        List<PrjInfo> PrjList = iprjJpaTryRepository.findWithPagination(PageRequest.of(0 + pagingDto.getPageNo(), 10));
        Long listCount = iprjJpaTryRepository.count();
        if (PrjList.size() == 0) {
            return null;
        } else {
            List<PrjTableDataDto> PrjListData = QsolModelMapper.map(PrjList, PrjTableDataDto.class);
            return ListDto.<PrjTableDataDto>builder()
                    .list(PrjListData)
                    .listCount(listCount)
                    .build();
        }
    }

    @Transactional
    public ListDto<PrjTableDataDto> listConditionSearch(PrjConditionSearchDataDto prjConditionSearchDataDto) {
        List<PrjInfo> PrjList = null;
        Long ConditionCount = null;
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

    @Transactional
    public void create(PrjTableDataDto prjTableDataDto) {
        PrjInfo prjInfo = new PrjInfo();
        prjInfo.setPname(prjTableDataDto.getPname());
        prjInfo.setPcontent(prjTableDataDto.getPcontent());
        prjInfo.setPview(prjTableDataDto.getPview());
        iprjJpaTryRepository.save(prjInfo);
    }


    public ListDto<PrjTableDataDto> findPnameAndPcontent(PrjTableDataDto prjTableDataDto) {
        List<PrjInfo> prjData = iprjJpaTryRepository.findPnameAndPcontent(prjTableDataDto.getPid());
        List<PrjTableDataDto> PrjListData = QsolModelMapper.map(prjData, PrjTableDataDto.class);
        return ListDto.<PrjTableDataDto>builder()
                .list(PrjListData)
                .build();
    }

    public ListDto<CompPrjParticiDataDto> findParticipationComp(PrjIdDataDto prjIdDataDto) {
        List<IPrjParticipationCompGetName> prjCompList = icompJpaTryRepository.findParticipationComp(prjIdDataDto.getPid(), PageRequest.of(0 + prjIdDataDto.getPageNo(), 10));
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
            prjDuplicateCheckDto.setDuplicateCheck("이미 참여한 회사입니다");
        }
    }

    public ListDto<EmpTableDataDto> compParticiEmpSearch(PrjIdDataDto prjIdDataDto) {
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

    public void prjAddCompEmpLastStep(PrjAddCompEmpDataDto prjAddCompEmpDataDto) {
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

    public ListDto<PrjTableDataDto> updatePrjStep(PrjIdDataDto prjIdDataDto) {
        List<PrjInfo> prjList = iprjJpaTryRepository.findAllByPId(prjIdDataDto.getPid());
        List<PrjTableDataDto> prjListData = QsolModelMapper.map(prjList, PrjTableDataDto.class);
        return  ListDto.<PrjTableDataDto>builder()
                .list(prjListData)
                .build();
    }

    /*@Transactional*/
    public void updatePrjTry(PrjTableDataDto prjTableDataDto) {
        // Dirty Checking 활용
        PrjInfo prjInfo = iprjJpaTryRepository.findById(prjTableDataDto.getPid()).orElse(null);
        prjInfo.setPname(prjTableDataDto.getPname());
        prjInfo.setPcontent(prjTableDataDto.getPcontent());
    }

    public void unrealDelete(Long pid) {
        // DirtyChecking 활용
        PrjInfo prjInfo = iprjJpaTryRepository.findById(pid).orElse(null);
        prjInfo.setPview(false);
    }

    @Transactional
    public void realDelete(Long pid) {
        iprjParticipationCompJpaRepository.deleteByPid(pid);
        iprjJpaTryRepository.deleteByPid(pid);
    }

    public void prjCompUnrealDelete(PrjTableDataDto prjTableDataDto) {
        iprjParticipationCompJpaRepository.prjCompUnrealDelete(prjTableDataDto.getPid(), prjTableDataDto.getCid());
    }

    @Transactional
    public void prjCompRealDelete(PrjTableDataDto prjTableDataDto) {
        iprjParticipationCompJpaRepository.deleteByPidAndCid(prjTableDataDto.getPid(), prjTableDataDto.getCid());
    }

    public void prjEmpUnrealDelete(PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        iprjParticipationCompJpaRepository.prjEmpUnrealDelete(prjEidToDeleteDataDto.getPid(), prjEidToDeleteDataDto.getCid(), prjEidToDeleteDataDto.getEid());
    }

    @Transactional
    public void prjEmpRealDelete(PrjEidToDeleteDataDto prjEidToDeleteDataDto) {
        iprjParticipationCompJpaRepository.deleteByEid(prjEidToDeleteDataDto.getPid(), prjEidToDeleteDataDto.getCid(), prjEidToDeleteDataDto.getEid());
    }
}
