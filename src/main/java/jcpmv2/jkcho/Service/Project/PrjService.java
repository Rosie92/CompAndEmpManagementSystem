package jcpmv2.jkcho.Service.Project;

import jcpmv2.jkcho.Domain.*;
import jcpmv2.jkcho.Dto.*;
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

    public ListDto<PrjDto> findAll(PagingDto pagingDto) {
        List<PrjInfo> PrjList = iprjJpaTryRepository.findWithPagination(PageRequest.of(0 + pagingDto.getPageNo(), 10));
        Long listCount = iprjJpaTryRepository.count();
        if (PrjList.size() == 0) {
            return null;
        } else {
            List<PrjDto> PrjListData = QsolModelMapper.map(PrjList, PrjDto.class);
            return ListDto.<PrjDto>builder()
                    .list(PrjListData)
                    .listCount(listCount)
                    .build();
        }
    }

    @Transactional
    public ListDto<PrjDto> listConditionSearch(PrjDto prjDto) {
        List<PrjInfo> PrjList = null;
        Long ConditionCount = null;
        if (prjDto.getCondition().equals("pname")) {
            PrjList = iprjJpaTryRepository.findAllByCnameOrCbossPaging(prjDto.getItem(), PageRequest.of(0 + prjDto.getPageNo(), 10));
            ConditionCount = iprjJpaTryRepository.conditionCountByCnameOrCboss(prjDto.getItem());
        }
        List<PrjDto> PrjListData = QsolModelMapper.map(PrjList, PrjDto.class);
        return ListDto.<PrjDto>builder()
                .list(PrjListData)
                .listCount(ConditionCount)
                .build();
    }

    @Transactional
    public void create(PrjDto prjDto) {
        PrjInfo prjInfo = new PrjInfo();
        prjInfo.setPname(prjDto.getPname());
        prjInfo.setPcontent(prjDto.getPcontent());
        prjInfo.setPview(prjDto.getPview());
        iprjJpaTryRepository.save(prjInfo);
    }


    public ListDto<PrjDto> findPnameAndPcontent(PrjDto prjDto) {
        List<PrjInfo> prjData = iprjJpaTryRepository.findPnameAndPcontent(prjDto.getPid());
        List<PrjDto> PrjListData = QsolModelMapper.map(prjData, PrjDto.class);
        return ListDto.<PrjDto>builder()
                .list(PrjListData)
                .build();
    }

    public ListDto<CompDto> findParticipationComp(PrjDto prjDto) {
        List<IPrjParticipationCompGetName> prjCompList = icompJpaTryRepository.findParticipationComp(prjDto.getPid());
        if (prjCompList.size() == 0) {
            return null;
        } else {
            List<CompDto> prjListData = QsolModelMapper.map(prjCompList, CompDto.class);
            return ListDto.<CompDto>builder()
                    .list(prjListData)
                    /*.compid(PrjListData.get(0).getEcompid())*/
                    .build();
        }
    }

    @Transactional
    public void prjAddCompDeplicateCheck(PrjDto prjDto) {
        Optional<PrjParticipationCompInfo> duplicateCid = iprjParticipationCompJpaRepository.findTopByCidAndPid(prjDto.getCid(), prjDto.getPid());
        if (duplicateCid.isPresent()) {
            prjDto.setDuplicateCheck("이미 참여한 회사입니다");
        }
    }

    public ListDto<EmpDto> compPartiEmpSearch(PrjDto prjDto) {
        List<IPrjParticipationEmpGetData> prjEmpList = iempJpaTryRepository.findParticipationComp(prjDto.getPid(), prjDto.getCid());
        if (prjEmpList.size() == 0) {
            return null;
        } else {
            List<EmpDto> prjListData = QsolModelMapper.map(prjEmpList, EmpDto.class);
            return ListDto.<EmpDto>builder()
                    .list(prjListData)
                    /*.compid(PrjListData.get(0).getEcompid())*/
                    .build();
        }
    }

    public void prjAddCompEmpLastStep(PrjDto prjDto) {
        int count = 0;
        while (count < prjDto.getEid().length) {
            PrjParticipationCompInfo prjParticipationCompInfo = new PrjParticipationCompInfo();
            prjParticipationCompInfo.setPid(prjDto.getPid());
            prjParticipationCompInfo.setCid(prjDto.getCid());
            prjParticipationCompInfo.setEid(prjDto.getEid()[count]);
            prjParticipationCompInfo.setCview(prjDto.getCview());
            prjParticipationCompInfo.setEview(prjDto.getEview());
            iprjParticipationCompJpaRepository.save(prjParticipationCompInfo);
            count++;
        }
    }

    public ListDto<PrjDto> updatePrjStep(PrjDto prjDto) {
        List<PrjInfo> prjList = iprjJpaTryRepository.findAllByPId(prjDto.getPid());
        List<PrjDto> prjListData = QsolModelMapper.map(prjList, PrjDto.class);
        return  ListDto.<PrjDto>builder()
                .list(prjListData)
                .build();
    }

    @Transactional
    public void updatePrjTry(PrjDto prjDto) {
        // Dirty Checking 활용
        PrjInfo prjInfo = iprjJpaTryRepository.findById(prjDto.getPid()).orElse(null);
        prjInfo.setPname(prjDto.getPname());
        prjInfo.setPcontent(prjDto.getPcontent());
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

    public void prjCompUnrealDelete(PrjDto prjDto) {
        iprjParticipationCompJpaRepository.prjCompUnrealDelete(prjDto.getPid(), prjDto.getCid());
    }

    @Transactional
    public void prjCompRealDelete(PrjDto prjDto) {
        iprjParticipationCompJpaRepository.deleteByPidAndCid(prjDto.getPid(), prjDto.getCid());
    }

    public void prjEmpUnrealDelete(EmpDto empDto) {
        iprjParticipationCompJpaRepository.prjEmpUnrealDelete(empDto.getPid(), empDto.getCid(), empDto.getEid());
    }

    @Transactional
    public void prjEmpRealDelete(EmpDto empDto) {
        iprjParticipationCompJpaRepository.deleteByEid(empDto.getPid(), empDto.getCid(), empDto.getEid());
    }
}
