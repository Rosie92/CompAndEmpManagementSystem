package jcpmv2.jkcho.Service.Employee;

import jcpmv2.jkcho.Domain.EmpInfo;
import jcpmv2.jkcho.Interface.IPrjParticipationEmpGetData;
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

    /*--------------------------------------SELECT--------------------------------------*/
    public ListDto<EmpTableDataDto> findAllByEcompidOrderByEnamePaging(EmpListSearchDataDto empListSearchDataDto) { // 직원 전체 리스트 검색
        /*
            EmpInfo를 제네릭으로 설정하여 해당 테이블 엔티티의 각 객체에 맞게 데이터를 가져옴
            Repository를 통해 받아온 EmpList안의 객체들은 jpa와 연동된 영속체 객체
            해당 리스트의 객체들을 활용하기 위해 필요한 것만 추출하여 리스트 담아야함
            EmpList에서 필요한 객체들을 골라 EmpListData의 각 객체에 매핑시키는데 코드가 너무 길어지며
            매번 코드마다 타입도 다를것임. 때문에 QsolModelMapper라는 ModelMapper를 작성하여 모든 코드에서 공통적으로 처리할 수 있도록 함
            이 때 영속성 객체가 담겨있는 리스트 EmpList(orJoinList)를 소스로<S>, 타입에 대하여 명시되어 있는 EmpTableDataDto를 타입으로<T> 넘김
            그리하여 EmpTableDataDto를 제네렉으로 갖고있는 리스트 EmplistData에 데이터를 재정립
         */
        List<EmpInfo> EmpList = new ArrayList<>(); // if안에서 변수를 사용할 수 있도록 미리 생성 & 초기화
        List<IPrjParticipationEmpGetData> joinList = new ArrayList<>();
        Long empListCount = 0L;
        Long empListCountParticiRemove = 0L;
        Long result = 0L;
        // 전체 직원 목록을 찾는것과 프로젝트에 참가하고 있는 직원을 제외한 나머지 직원 전체 목록을 찾는 상황을 구분하는 스위치 : ParticipationEmpRemove(true or false)
        if (empListSearchDataDto.getParticipationEmpRemove().equals("true")) { // 프로젝트에 참가하고 있는 직원을 제외한 나머지 직원 전체 목록
            // PageRequest.of( 페이징 처리시 0부터 사이즈 10개씩, JSP에서 pageNo-1을 해준 이유 )
            joinList = iempJpaTryRepository.findAllByCidAndPidOrderByEnamePagingOffAndParticipationEmpRemove(empListSearchDataDto.getPid(), empListSearchDataDto.getCid(), PageRequest.of(0 + empListSearchDataDto.getPageNo(), 10));
            empListCount = iempJpaTryRepository.defaultEmpListCount(empListSearchDataDto.getCid());
            empListCountParticiRemove = iprjParticipationCompJpaRepository.empListCountParticiRemove(empListSearchDataDto.getPid(), empListSearchDataDto.getCid());
            // 전체 직원 count(empListCount)에서 프로젝트에 참가중인 직원 count(empListCountParticiRemove)를 뺄셈하여 미참석중인 직원의 count(result)를 구함
            result = empListCount - empListCountParticiRemove;
        } else { // 모든 직원 목록
            EmpList = iempJpaTryRepository.findAllByEcompidOrderByEnamePaging(empListSearchDataDto.getSearchCompid(), PageRequest.of(0 + empListSearchDataDto.getPageNo(), 10));/*searchingDto*/
            empListCount = iempJpaTryRepository.defaultEmpListCount(empListSearchDataDto.getSearchCompid());
        }
        /*
            ListDto.에 CompTableDataDto를 제네릭으로 설정하여 ListDto의 객체들과 타입 매핑,
            이후 builder를 통해 set
         */
        if (empListSearchDataDto.getParticipationEmpRemove().equals("true")) { // 프로젝
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
    public ListDto<EmpTableDataDto> emplistConditionSearch(EmpConditionSearchDataDto empConditionSearchDataDto) { // 직원 조건 검색
        List<EmpInfo> EmpList = new ArrayList<>(); // if안에서 변수를 사용할 수 있도록 미리 생성 & 초기화
        Long count = 0L;
        if (empConditionSearchDataDto.getCondition().equals("ename")) { // 검색 조건 : 직원명
            EmpList = iempJpaTryRepository.findAllByEname(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEname(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eemail")) { // 검색 조건 : 직원 메일
            EmpList = iempJpaTryRepository.findAllByEemail(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEemail(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("ephone")) { // 검색 조건 : 직원 휴대폰
            EmpList = iempJpaTryRepository.findAllByEphone(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEphone(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eposition")) { // 검색 조건 : 직원 소속
            EmpList = iempJpaTryRepository.findAllByEposition(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEposition(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        } else if (empConditionSearchDataDto.getCondition().equals("eaffiliation")) { // 검색 조건 : 직원 직급
            EmpList = iempJpaTryRepository.findAllByEaffiliation(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid(), PageRequest.of(0 + empConditionSearchDataDto.getPageNo(), 10));
            count = iempJpaTryRepository.conditionCountByEaffiliation(empConditionSearchDataDto.getItem(), empConditionSearchDataDto.getSearchCompid());
        }
        List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
        return ListDto.<EmpTableDataDto>builder()
                .list(EmpListData)
                .empListCount(count)
                .build();
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @Transactional
    public void create(EmpCidGotViewDataDto empCidGotViewDataDto) { // 직원 신규 생성
        /*
            직원 신규 생성 진행 전, 이미 등록된 직원인지 중복 체크(이름과 휴대폰)
            이름과 휴대폰(+회사id)을 기준으로 직원 검색 후 duplicateCheck에 저장
            if로 duplicateCheck.isPresent() 수행 후 중복일 시 Exception을 발생시키고
            중복이 아니라면 생성(set&save) 진행
         */
        Optional<EmpInfo> duplicateCheck = iempJpaTryRepository.findByEnameAndEphoneAndEcompid(empCidGotViewDataDto.getEname(), empCidGotViewDataDto.getEphone(), empCidGotViewDataDto.getSearchCompid());
        if (duplicateCheck.isPresent()) {  // 중복이라면 duplicateCheck.isPresent() : true
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
            /*
            System.out.println("1차 save 진행 완료");
            // 트랜잭션 테스트, 이전 단계에서 save 수행 완료되었지만 아래 코드에서 오류 발생
            empInfo.setEname("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
            iempJpaTryRepository.save(empInfo);
            */
        }
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @Transactional
    public ListDto<EmpTableDataDto> empUpdateReady(EmpTableDataDto empTableDataDto) { // 업데이트 진행 전 해당 직원의 정보 불러오기
        List<EmpInfo> EmpList = iempJpaTryRepository.findAllByEid(empTableDataDto.getEid());
        List<EmpTableDataDto> EmpListData = QsolModelMapper.map(EmpList, EmpTableDataDto.class);
        return ListDto.<EmpTableDataDto>builder()
                .list(EmpListData)
                .build();
    }

    @Transactional
    public void update(EmpTableDataDto empTableDataDto) { // 업데이트 실제 진행
        // 진행 전 다시 한번 중복 체크
        Optional<EmpInfo> duplicateCheck = iempJpaTryRepository.findByEnameAndEphoneAndEcompid(empTableDataDto.getEname(), empTableDataDto.getEphone(), empTableDataDto.getEcompid());
        if (duplicateCheck.isPresent()) {
            throw new DuplicateException();
        }
        // Dirty Checking; nas file 참조
        // 직원id(eid)를 통해 DB와 통신, 영속성으로 연결되며 이 상태에서 정보가 set되면 자동으로 DB에 반영됨
        EmpInfo empInfo = iempJpaTryRepository.findById(empTableDataDto.getEid()).orElse(null);
        empInfo.setEname(empTableDataDto.getEname());
        empInfo.setEemail(empTableDataDto.getEemail());
        empInfo.setEphone(empTableDataDto.getEphone());
        empInfo.setEposition(empTableDataDto.getEposition());
        empInfo.setEaffiliation(empTableDataDto.getEaffiliation());
        /*
        System.out.println("1차update종료");
        // 트랜잭션 테스트, 이전 단계에서 update수행 완료되었지만 아래 코드에서 오류 발생
        empInfo.setEname("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        */
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @Transactional
    public void unrealDelete(Long eid) { // 직원 가삭제. D수행이 아닌 eid를 통해 eview 값을 U함
        iempJpaTryRepository.unrealDelete(eid);
        /*
        System.out.println("가삭제 완료");
        // 트랜잭션 테스트, 가삭제 수행 완료 후 임의 오류 발생
        throw new NullPointerException();
        */
    }

    @Transactional
    public void realDelete(Long eid) { // 직원 실삭제. D수행
        iempJpaTryRepository.deleteByEid(eid);
        /*
        System.out.println("실삭제 완료");
        // 트랜잭션 테스트, 실삭제 수행 완료 후 임의 오류 발생
        throw new NullPointerException();
        */
    }

}
