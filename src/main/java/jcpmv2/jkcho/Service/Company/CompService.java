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

    /*class CustomerNotFoundException (message: String) : Exception(message)*/

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


    public ListDto<CompTableDataDto> findAll(PagingDto pagingDto) {
        List<CompInfo> CompList = icompJpaTryRepository.findWithPagination(PageRequest.of(0 + pagingDto.getPageNo(), 10));
        Long listCount = icompJpaTryRepository.count();
        /*int count = 0;  // query = where c_view=ture 로 대체
        int q = 0;
        while(q < CompList.size()) {
            if (count == 1) {
                q = 0;
                count = 0;
            }
            if (CompList.get(q).getCview() == false) {
                CompList.remove(q);
                if (q == 0) {
                    count = 1;
                } else {
                    q--;
                }
            }
            if(CompList.size() != 1) {
                q++;
            } else if(CompList.size() == 1 && CompList.get(q).getCview() == true) {
                q++;
            }
        }*/
        List<CompTableDataDto> CompListData = QsolModelMapper.map(CompList, CompTableDataDto.class);
        return ListDto.<CompTableDataDto>builder()
                .list(CompListData)
                .listCount(listCount)
                .build();
    }

    @Transactional
    public ListDto<CompTableDataDto> listConditionSearch(CompConditionSearchDataDto compConditionSearchDataDto) {
        List<CompInfo> CompList = null;
        Long ConditionCount = null;
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
