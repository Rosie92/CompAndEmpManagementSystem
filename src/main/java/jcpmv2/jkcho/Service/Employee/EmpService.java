package jcpmv2.jkcho.Service.Employee;

import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Domain.IPrjParticipationEmpGetData;
import jcpmv2.jkcho.Dto.Emp.*;
import jcpmv2.jkcho.Dto.ListDto;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
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

    public ListDto<EmpTableDataDto> findAllByEcompidOrderByEnamePaging(EmpListSearchDataDto empListSearchDataDto) { /*SearchingDto searchingDto*/
        List<EmpInfo> EmpList = new ArrayList<>();
        List<IPrjParticipationEmpGetData> joinList = new ArrayList<>();
        Long empListCount = 0L;
        if (empListSearchDataDto.getPagingOff().equals("off") && empListSearchDataDto.getParticipationEmpRemove().equals("true")) {
            joinList = iempJpaTryRepository.findAllByCidAndPidOrderByEnamePagingOffAndParticipationEmpRemove(empListSearchDataDto.getPid(), empListSearchDataDto.getCid());
        } else if (empListSearchDataDto.getPagingOff().equals("off")) {
            EmpList = iempJpaTryRepository.findAllBySearchCompidOrderByEnamePagingOff(empListSearchDataDto.getSearchCompid());
        } else {
            EmpList = iempJpaTryRepository.findAllByEcompidOrderByEnamePaging(empListSearchDataDto.getSearchCompid(), PageRequest.of(0 + empListSearchDataDto.getPageNo(), 10));/*searchingDto*/
            empListCount = iempJpaTryRepository.count();
            /*int count = 0;     query where eview=true 로 대체
            int q = 0;
            while(q < EmpList.size()) {
                if (count == 1) {
                    q = 0;
                    count = 0;
                }
                if (EmpList.get(q).getEview() == false) {
                    EmpList.remove(q);
                    if (q == 0) {
                        count = 1;
                    } else {
                        q--;
                    }
                }
                if(EmpList.size() != 1) {
                    q++;
                } else if(EmpList.size() == 1 && EmpList.get(q).getEview() == true) {
                    q++;
                }
            }*/
        }
        if (empListSearchDataDto.getPagingOff().equals("off") && empListSearchDataDto.getParticipationEmpRemove().equals("true")) {
            List<EmpTableDataDto> EmpListData = QsolModelMapper.map(joinList, EmpTableDataDto.class);
            return ListDto.<EmpTableDataDto>builder()
                    .list(EmpListData)
                    .compid(EmpListData.get(0).getEcompid())
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
            count = iempJpaTryRepository.conditionCountByEname(empConditionSearchDataDto.getItem());
        } else if (empConditionSearchDataDto.getCondition().equals("eemail")) {
            EmpList = iempJpaTryRepository.findAllByEemail(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEemail(empConditionSearchDataDto.getItem());
        } else if (empConditionSearchDataDto.getCondition().equals("ephone")) {
            EmpList = iempJpaTryRepository.findAllByEphone(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEphone(empConditionSearchDataDto.getItem());
        } else if (empConditionSearchDataDto.getCondition().equals("eposition")) {
            EmpList = iempJpaTryRepository.findAllByEposition(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEposition(empConditionSearchDataDto.getItem());
        } else if (empConditionSearchDataDto.getCondition().equals("eaffiliation")) {
            EmpList = iempJpaTryRepository.findAllByEaffiliation(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEaffiliation(empConditionSearchDataDto.getItem());
        }
        List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
        return ListDto.<EmpTableDataDto>builder()
                .list(EmpListData)
                .empListCount(count)
                .build();
    }

    @Transactional
    public void create(EmpCidGotViewDataDto empCidGotViewDataDto) {
        Optional<EmpInfo> duplicateCname = iempJpaTryRepository.findByEnameAndEphone(empCidGotViewDataDto.getEname(), empCidGotViewDataDto.getEphone());
        if (duplicateCname.isPresent()) {
            empCidGotViewDataDto.setEname("중복된 사원입니다(이름과 이메일이 중복)");
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
