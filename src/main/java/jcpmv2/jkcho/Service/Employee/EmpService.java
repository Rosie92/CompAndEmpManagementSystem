package jcpmv2.jkcho.Service.Employee;

import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Domain.IPrjParticipationEmpGetData;
import jcpmv2.jkcho.Dto.Emp.*;
import jcpmv2.jkcho.Dto.ListDto;
import jcpmv2.jkcho.Error.Model.DuplicateException;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
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
public class EmpService {
    @Autowired
    private IempJpaTryRepository iempJpaTryRepository;

    @Autowired
    private IprjParticipationCompJpaRepository iprjParticipationCompJpaRepository;

    public ListDto<EmpTableDataDto> findAllByEcompidOrderByEnamePaging(EmpListSearchDataDto empListSearchDataDto) { /*SearchingDto searchingDto*/
        List<EmpInfo> EmpList = new ArrayList<>();
        List<IPrjParticipationEmpGetData> joinList = new ArrayList<>();
        Long empListCount = 0L;
        Long empListCountParticiRemove = 0L;
        Long result = 0L;
        if (empListSearchDataDto.getParticipationEmpRemove().equals("true")) {
            joinList = iempJpaTryRepository.findAllByCidAndPidOrderByEnamePagingOffAndParticipationEmpRemove(empListSearchDataDto.getPid(), empListSearchDataDto.getCid(), PageRequest.of(0 + empListSearchDataDto.getPageNo(), 10));
            empListCount = iempJpaTryRepository.defaultEmpListCount(empListSearchDataDto.getCid());
            empListCountParticiRemove = iprjParticipationCompJpaRepository.empListCountParticiRemove(empListSearchDataDto.getPid(), empListSearchDataDto.getCid());
            result = empListCount - empListCountParticiRemove;
        } else {
            EmpList = iempJpaTryRepository.findAllByEcompidOrderByEnamePaging(empListSearchDataDto.getSearchCompid(), PageRequest.of(0 + empListSearchDataDto.getPageNo(), 10));/*searchingDto*/
            empListCount = iempJpaTryRepository.defaultEmpListCount(empListSearchDataDto.getSearchCompid());
        }
        if (empListSearchDataDto.getParticipationEmpRemove().equals("true")) {
            List<EmpTableDataDto> EmpListData = QsolModelMapper.map(joinList, EmpTableDataDto.class);
            return ListDto.<EmpTableDataDto>builder()
                    .list(EmpListData)
                    .compid(EmpListData.get(0).getEcompid())
                    .empListCount(result)
                    .build();
        } else {
            List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
            return ListDto.<EmpTableDataDto>builder()
                    .list(EmpListData)
                    .compid(EmpListData.get(0).getEcompid())
                    .empListCount(empListCount)
                    .build();
        }
    }

    @Transactional
    public ListDto<EmpTableDataDto> emplistConditionSearch(EmpConditionSearchDataDto empConditionSearchDataDto) {
        System.out.println("emp 조건 조회 서비스 시작");
        List<EmpInfo> EmpList = null;
        Long count = null;
        if (empConditionSearchDataDto.getCondition().equals("ename")) {
            EmpList = iempJpaTryRepository.findAllByEname(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEname(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eemail")) {
            EmpList = iempJpaTryRepository.findAllByEemail(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEemail(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("ephone")) {
            EmpList = iempJpaTryRepository.findAllByEphone(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEphone(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eposition")) {
            EmpList = iempJpaTryRepository.findAllByEposition(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEposition(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eaffiliation")) {
            EmpList = iempJpaTryRepository.findAllByEaffiliation(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEaffiliation(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        }
        List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
        return ListDto.<EmpTableDataDto>builder()
                .list(EmpListData)
                .empListCount(count)
                .build();
    }

    @Transactional
    public void create(EmpCidGotViewDataDto empCidGotViewDataDto) {
        Optional<EmpInfo> duplicateCheck = iempJpaTryRepository.findByEnameAndEphoneAndEcompid(empCidGotViewDataDto.getEname(), empCidGotViewDataDto.getEphone(), empCidGotViewDataDto.getSearchCompid());
        if (duplicateCheck.isPresent()) {
            throw new DuplicateException();
        } else {
            EmpInfo empInfo = new EmpInfo();
            empInfo.setEname(empCidGotViewDataDto.getEname());
            empInfo.setEemail(empCidGotViewDataDto.getEemail());
            empInfo.setEphone(empCidGotViewDataDto.getEphone());
            empInfo.setEposition(empCidGotViewDataDto.getEposition());
            empInfo.setEaffiliation(empCidGotViewDataDto.getEaffiliation());
            empInfo.setEcompid(empCidGotViewDataDto.getSearchCompid());
            empInfo.setEview(empCidGotViewDataDto.getEview());
            iempJpaTryRepository.save(empInfo);
        }
    }

    @Transactional
    public ListDto<EmpTableDataDto> empUpdateReady(EmpTableDataDto empTableDataDto) {
        List<EmpInfo> EmpList = iempJpaTryRepository.findAllByEid(empTableDataDto.getEid());
        List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
        return ListDto.<EmpTableDataDto>builder()
                .list(EmpListData)
                .build();
    }

    @Transactional
    public void update(EmpTableDataDto empTableDataDto) {
        Optional<EmpInfo> duplicateCheck = iempJpaTryRepository.findByEnameAndEphoneAndEcompid(empTableDataDto.getEname(), empTableDataDto.getEphone(), empTableDataDto.getEcompid());
        if (duplicateCheck.isPresent()) {
            throw new DuplicateException();
        }
        // Dirty Checking; nas file 참조
        EmpInfo empInfo = iempJpaTryRepository.findById(empTableDataDto.getEid()).orElse(null);
        empInfo.setEname(empTableDataDto.getEname());
        empInfo.setEemail(empTableDataDto.getEemail());
        empInfo.setEphone(empTableDataDto.getEphone());
        empInfo.setEposition(empTableDataDto.getEposition());
        empInfo.setEaffiliation(empTableDataDto.getEaffiliation());
    }

    @Transactional
    public void unrealDelete(Long eid) {
        iempJpaTryRepository.unrealDelete(eid);
    }

    @Transactional
    public void realDelete(Long eid) {
        iempJpaTryRepository.deleteByEid(eid);
    }
}
