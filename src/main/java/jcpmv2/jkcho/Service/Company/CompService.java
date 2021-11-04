package jcpmv2.jkcho.Service.Company;

import jcpmv2.jkcho.Domain.CompInfo;
import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Mapper.QsolModelMapper;
import jcpmv2.jkcho.Repository.IcompJpaTryRepository;
import jcpmv2.jkcho.Repository.IempJpaTryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public void create(CompDto compDto) {
        CompInfo compInfo = new CompInfo();
        compInfo.setCname(compDto.getCname());
        compInfo.setCboss(compDto.getCboss());
        compInfo.setCcall(compDto.getCcall());
        compInfo.setCnumber(compDto.getCnumber());
        compInfo.setCview(compDto.getCview());
        icompJpaTryRepository.save(compInfo);
    }


    public ListDto<CompDto> findAll() {
        List<CompInfo> CompList = icompJpaTryRepository.findAll(Sort.by(Sort.Direction.ASC, "cname","cboss")/*searchingDto*/);
        /*Long listCount = icompJpaTryRepository.count();*/
        int count = 0;
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
        }
        if (CompList.size() == 0) {
            return null;
        } else {
            List<CompDto> CompListData = QsolModelMapper.map(CompList, CompDto.class);
            return ListDto.<CompDto>builder()
                    .list(CompListData)
                    /*.listCount(listCount)*/
                    .build();
        }
    }

    @Transactional
    public ListDto<CompDto> listConditionSearch(CompDto compDto) {
        List<CompInfo> CompList = null;
        if (compDto.getCondition().equals("cname+cboss")) {
            CompList = icompJpaTryRepository.findAllByCnameOrCboss(compDto.getItem());
        } else if (compDto.getCondition().equals("ccall")) {
            CompList = icompJpaTryRepository.findAllByCcall(compDto.getItem());
        } else if (compDto.getCondition().equals("cnumber")) {
            CompList = icompJpaTryRepository.findAllByCnumber(compDto.getItem());
        }
        List<CompDto> CompListData = QsolModelMapper.map(CompList, CompDto.class);
        return ListDto.<CompDto>builder()
                .list(CompListData)
                .build();
    }

    @Transactional
    public ListDto<CompDto> compUpdateReady(CompDto compDto) {
        List<CompInfo> CompList = icompJpaTryRepository.findAllByCid(compDto.getCid());
        List<CompDto> CompListData = QsolModelMapper.map(CompList, CompDto.class);
        return ListDto.<CompDto>builder()
                .list(CompListData)
                .build();
    }

    @Transactional
    public void compUpdateDirtyChecking(CompDto compDto) {
        // Dirty Checking; nas file 참조
        CompInfo compInfo = icompJpaTryRepository.findById(compDto.getCid()).orElse(null);
        compInfo.setCname(compDto.getCname());
        compInfo.setCboss(compDto.getCboss());
        compInfo.setCcall(compDto.getCcall());
        compInfo.setCnumber(compDto.getCnumber());
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
