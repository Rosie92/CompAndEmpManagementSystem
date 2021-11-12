package jcpmv2.jkcho.Service.Employee;

import jcpmv2.jkcho.Domain.CompInfo;
import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Dto.CompDto;
import jcpmv2.jkcho.Dto.EmpDto;
import jcpmv2.jkcho.Dto.ListDto;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
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
public class EmpService {
    @Autowired
    private IempJpaTryRepository iempJpaTryRepository;

    public ListDto<EmpDto> findAllByEcompidOrderByEnamePaging(EmpDto empDto) { /*SearchingDto searchingDto*/
        List<EmpInfo> EmpList = iempJpaTryRepository.findAllByEcompidOrderByEnamePaging(empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));/*searchingDto*/
        Long empListCount = iempJpaTryRepository.count();
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
        if (EmpList.size() == 0) {
            return null;
        } else {
            List<EmpDto> EmpListData = QsolModelMapper.map(EmpList, EmpDto.class);
            return ListDto.<EmpDto>builder()
                    .list(EmpListData)
                    .compid(EmpListData.get(0).getEcompid())
                    .empListCount(empListCount)
                    .build();
        }
    }

    @Transactional
    public ListDto<EmpDto> emplistConditionSearch(EmpDto empDto) {
        System.out.println("emp 조건 조회 서비스 시작");
        List<EmpInfo> EmpList = null;
        Long count = null;
        if (empDto.getCondition().equals("ename")) {
            EmpList = iempJpaTryRepository.findAllByEname(empDto.getItem(), empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEname(empDto.getItem());
        } else if (empDto.getCondition().equals("eemail")) {
            EmpList = iempJpaTryRepository.findAllByEemail(empDto.getItem(), empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEemail(empDto.getItem());
        } else if (empDto.getCondition().equals("ephone")) {
            EmpList = iempJpaTryRepository.findAllByEphone(empDto.getItem(), empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEphone(empDto.getItem());
        } else if (empDto.getCondition().equals("eposition")) {
            EmpList = iempJpaTryRepository.findAllByEposition(empDto.getItem(), empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEposition(empDto.getItem());
        } else if (empDto.getCondition().equals("eaffiliation")) {
            EmpList = iempJpaTryRepository.findAllByEaffiliation(empDto.getItem(), empDto.getSearchCompid(), PageRequest.of(0 + empDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEaffiliation(empDto.getItem());
        }
        List<EmpDto> EmpListData = QsolModelMapper.map(EmpList, EmpDto.class);
        return ListDto.<EmpDto>builder()
                .list(EmpListData)
                .empListCount(count)
                .build();
    }

    @Transactional
    public void create(EmpDto empDto) {
        Optional<EmpInfo> duplicateCname = iempJpaTryRepository.findByEnameAndEphone(empDto.getEname(), empDto.getEphone());
        if (duplicateCname.isPresent()) {
            empDto.setEname("중복된 사원입니다(이름과 이메일이 중복)");
        } else {
            EmpInfo empInfo = new EmpInfo();
            empInfo.setEname(empDto.getEname());
            empInfo.setEemail(empDto.getEemail());
            empInfo.setEphone(empDto.getEphone());
            empInfo.setEposition(empDto.getEposition());
            empInfo.setEaffiliation(empDto.getEaffiliation());
            empInfo.setEcompid(empDto.getSearchCompid());
            empInfo.setEview(empDto.getEview());
            iempJpaTryRepository.save(empInfo);
        }
    }

    @Transactional
    public ListDto<EmpDto> empUpdateReady(EmpDto empDto) {
        List<EmpInfo> EmpList = iempJpaTryRepository.findAllByEid(empDto.getEid());
        List<EmpDto> EmpListData = QsolModelMapper.map(EmpList, EmpDto.class);
        return ListDto.<EmpDto>builder()
                .list(EmpListData)
                .build();
    }

    @Transactional
    public void empUpdateDirtyChecking(EmpDto empDto) {
        // Dirty Checking; nas file 참조
        EmpInfo empInfo = iempJpaTryRepository.findById(empDto.getEid()).orElse(null);
        empInfo.setEname(empDto.getEname());
        empInfo.setEemail(empDto.getEemail());
        empInfo.setEphone(empDto.getEphone());
        empInfo.setEposition(empDto.getEposition());
        empInfo.setEaffiliation(empDto.getEaffiliation());
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
