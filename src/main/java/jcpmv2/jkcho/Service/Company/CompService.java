package jcpmv2.jkcho.Service.Company;

import jcpmv2.jkcho.Domain.CompInfo;
import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.CompConditionSearchDataDto;
import jcpmv2.jkcho.Dto.Comp.CompTableDataDto;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IcompJpaTryRepository;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CompService {
    @Autowired
    private IcompJpaTryRepository icompJpaTryRepository;
    @Autowired
    private IempJpaTryRepository iempJpaTryRepository;

    /*--------------------------------------SELECT--------------------------------------*/
    public ListDto<CompTableDataDto> findAll(PagingDto pagingDto) { // 회사 전체 리스트 검색
        // PageRequest.of( 페이징 처리시 0부터 사이즈 10개씩, JSP에서 pageNo-1을 해준 이유 )
        /*
            CompInfo를 제네릭으로 설정하여 해당 테이블 엔티티의 각 객체에 맞게 데이터를 가져옴
            Repository를 통해 받아온 CompList안의 객체들은 jpa와 연동된 영속체 객체
            해당 리스트의 객체들을 활용하기 위해 필요한 것만 추출하여 리스트 담아야함
            CompList에서 필요한 객체들을 골라 CompListData의 각 객체에 매핑시키는데 코드가 너무 길어지며
            매번 코드마다 타입도 다를것임. 때문에 QsolModelMapper라는 ModelMapper를 작성하여 모든 코드에서 공통적으로 처리할 수 있도록 함
            이 때 영속성 객체가 담겨있는 리스트 CompList를 소스로<S>, 타입에 대하여 명시되어 있는 CompTableDataDto를 타입으로<T> 넘김
            그리하여 CompTableDataDto를 제네렉으로 갖고있는 리스트 ComplistData에 데이터를 재정립
         */
        List<CompInfo> CompList = icompJpaTryRepository.findWithPagination(PageRequest.of(0 + pagingDto.getPageNo(), 10));
        Long listCount = icompJpaTryRepository.count();
        List<CompTableDataDto> CompListData = QsolModelMapper.map(CompList, CompTableDataDto.class);
        /*
            ListDto.에 CompTableDataDto를 제네릭으로 설정하여 ListDto의 객체들과 타입 매핑,
            이후 builder를 통해 set
         */
        return ListDto.<CompTableDataDto>builder()
                .list(CompListData)
                .listCount(listCount)
                .build();
    }

    @Transactional
    public ListDto<CompTableDataDto> listConditionSearch(CompConditionSearchDataDto compConditionSearchDataDto) {
        List<CompInfo> CompList = null;
        Long ConditionCount = null;
        // 조건 검색 시, 지정된 조건에 따라 검색어를 찾음
        if (compConditionSearchDataDto.getCondition().equals("cname+cboss")) {
            CompList = icompJpaTryRepository.findAllByCnameOrCbossPaging(compConditionSearchDataDto.getItem(), PageRequest.of(0 + compConditionSearchDataDto.getPageNo(), 10));
            ConditionCount = icompJpaTryRepository.conditionCountByCnameOrCboss(compConditionSearchDataDto.getItem());
        } else if (compConditionSearchDataDto.getCondition().equals("ccall")) {
            CompList = icompJpaTryRepository.findAllByCcallPaging(compConditionSearchDataDto.getItem(), PageRequest.of(0 + compConditionSearchDataDto.getPageNo(), 10));
            ConditionCount = icompJpaTryRepository.conditionCountByCcall(compConditionSearchDataDto.getItem());
        } else if (compConditionSearchDataDto.getCondition().equals("cnumber")) {
            CompList = icompJpaTryRepository.findAllByCnumberPaging(compConditionSearchDataDto.getItem(), PageRequest.of(0 + compConditionSearchDataDto.getPageNo(), 10));
            ConditionCount = icompJpaTryRepository.conditionCountByCnumber(compConditionSearchDataDto.getItem());
        }

        List<CompTableDataDto> CompListData = QsolModelMapper.map(CompList, CompTableDataDto.class);
        return ListDto.<CompTableDataDto>builder()
                .list(CompListData)
                .listCount(ConditionCount)
                .build();
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @Transactional
    public void create(CompTableDataDto compTableDataDto) {
        CompInfo compInfo = new CompInfo();
        compInfo.setCname(compTableDataDto.getCname());
        compInfo.setCboss(compTableDataDto.getCboss());
        compInfo.setCcall(compTableDataDto.getCcall());
        compInfo.setCnumber(compTableDataDto.getCnumber());
        compInfo.setCview(compTableDataDto.getCview());
        icompJpaTryRepository.save(compInfo);
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @Transactional
    public ListDto<CompTableDataDto> compUpdateReady(CompTableDataDto compTableDataDto) {
        List<CompInfo> CompList = icompJpaTryRepository.findAllByCid(compTableDataDto.getCid());
        List<CompTableDataDto> CompListData = QsolModelMapper.map(CompList, CompTableDataDto.class);
        return ListDto.<CompTableDataDto>builder()
                .list(CompListData)
                .build();
    }

    @Transactional
    public void update(CompTableDataDto compTableDataDto) {
        // Dirty Checking; nas file 참조
        CompInfo compInfo = icompJpaTryRepository.findById(compTableDataDto.getCid()).orElse(null);
        compInfo.setCname(compTableDataDto.getCname());
        compInfo.setCboss(compTableDataDto.getCboss());
        compInfo.setCcall(compTableDataDto.getCcall());
        compInfo.setCnumber(compTableDataDto.getCnumber());
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @Transactional
    public void unrealDelete(Long cid) {
        icompJpaTryRepository.unrealDelete(cid);
    }

    @Transactional
    public void realDelete(Long cid) {
        iempJpaTryRepository.deleteByEcompid(cid);
        icompJpaTryRepository.deleteByCid(cid);
    }
}
