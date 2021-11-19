package jcpmv2.jkcho.Service.Project;

import jcpmv2.jkcho.Domain.PrjInfo;
import jcpmv2.jkcho.Domain.PrjParticipationCompInfo;
import jcpmv2.jkcho.Domain.IPrjParticipationCompGetName;
import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IcompJpaTryRepository;
import jcpmv2.jkcho.Repository.IprjJpaTryRepository;
import jcpmv2.jkcho.Repository.IprjParticipationCompJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<IPrjParticipationCompGetName> PrjCompList = icompJpaTryRepository.findParticipationComp(prjDto.getPid());
        if (PrjCompList.size() == 0) {
            return null;
        } else {
            List<CompDto> PrjListData = QsolModelMapper.map(PrjCompList, CompDto.class);
            return ListDto.<CompDto>builder()
                    .list(PrjListData)
                    /*.compid(PrjListData.get(0).getEcompid())*/
                    .build();
        }
    }

    @Transactional
    public void prjAddComp(PrjDto prjDto) {
        PrjParticipationCompInfo prjParticipationCompInfo = new PrjParticipationCompInfo();
        prjParticipationCompInfo.setPid(prjDto.getPid());
        prjParticipationCompInfo.setCid(prjDto.getCid());
        iprjParticipationCompJpaRepository.save(prjParticipationCompInfo);
    }

}
