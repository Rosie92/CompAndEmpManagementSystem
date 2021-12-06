package jcpmv2.jkcho.Controller.Company;

import jcpmv2.jkcho.Dto.*;
import jcpmv2.jkcho.Dto.Comp.*;
import jcpmv2.jkcho.Error.Model.*;
import jcpmv2.jkcho.Service.Company.CompService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/company/compController")
public class CompController {

    @Autowired
    private CompService compService;

    /*--------------------------------------SELECT--------------------------------------*/
    @GetMapping("/{pageNo}") // 회사 전체 리스트 검색
    public ResponseEntity<ListDto<CompTableDataDto>> findAll(PagingDto pagingDto) {
        // pagingDto에 페이징을 위한 pageNo값
        return ResponseEntity.ok(compService.findAll(pagingDto));
    }

    @PostMapping("/listConditionSearch") // 회사 조건 리스트 검색
    public ResponseEntity<ListDto<CompTableDataDto>> listConditionSearch(@RequestBody @Valid CompConditionSearchDataDto compConditionSearchDataDto) {
        // compConditionSearchDataDto에 조건, 검색어, pageNo
        return ResponseEntity.ok(compService.listConditionSearch(compConditionSearchDataDto));
    }

    /*--------------------------------------CREATE--------------------------------------*/
    @PostMapping() // 회사 새로 등록
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid CompTableDataDto compTableDataDto) {
        // 입력 받아온 회사 정보들의 앞뒤 공백을 제거함(trim)
        compTableDataDto.setCname(compTableDataDto.getCname().trim());
        compTableDataDto.setCboss(compTableDataDto.getCboss().trim());
        compTableDataDto.setCcall(compTableDataDto.getCcall().trim());
        compTableDataDto.setCnumber(compTableDataDto.getCnumber().trim());
        // 입력 받아온 회사 정보들의 유효성을 체크함 (모든 정보 필수 항목)
        if (compTableDataDto.getCname().equals("")) {
            throw new CnameNullException();
        } else if (compTableDataDto.getCboss().equals("")) {
            throw new CbossNullException();
        } else if (compTableDataDto.getCcall().equals("")) {
            throw new CcallNullException();
        } else if (compTableDataDto.getCnumber().equals("")) {
            throw new CnumberNullException();
        }
        compService.create(compTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------UPDATE--------------------------------------*/
    @GetMapping("/updateReady/{cid}") // 회사 업데이트 전, 해당 회사의 정보 가져오기
    public ResponseEntity<ListDto<CompTableDataDto>> compUpdateReady(CompTableDataDto compTableDataDto){
        return ResponseEntity.ok(compService.compUpdateReady(compTableDataDto));
    }

    @PutMapping() // 회사 업데이트 실행
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid CompTableDataDto compTableDataDto) {
        // 입력 받아온 회사 정보들의 앞뒤 공백을 제거함(trim)
        compTableDataDto.setCname(compTableDataDto.getCname().trim());
        compTableDataDto.setCcall(compTableDataDto.getCcall().trim());
        compTableDataDto.setCboss(compTableDataDto.getCboss().trim());
        compTableDataDto.setCnumber(compTableDataDto.getCnumber().trim());
        // 입력 받아온 회사 정보들의 유효성을 체크함 (모든 정보 필수 항목)
        if (compTableDataDto.getCname().equals("")) {
            throw new CnameNullException();
        } else if (compTableDataDto.getCboss().equals("")) {
            throw new CbossNullException();
        } else if (compTableDataDto.getCcall().equals("")) {
            throw new CcallNullException();
        } else if (compTableDataDto.getCnumber().equals("")) {
            throw new CnumberNullException();
        }
        compService.update(compTableDataDto);
        return ResponseEntity.ok().build();
    }

    /*--------------------------------------DELETE--------------------------------------*/
    @PutMapping("/{cid}") // 회사 가삭제 실행
    public ResponseEntity<HttpStatus> unrealDelete(@PathVariable @Valid Long cid) {
        // 가삭제는 실제 D 수행이 아닌 U 이므로 PutMapping
        compService.unrealDelete(cid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cid}") // 회사 실삭제 실행
    public ResponseEntity<HttpStatus> realDelete(@PathVariable @Valid Long cid) {
        // 실삭제는 실제 D 수행이므로 DeleteMapping
        compService.realDelete(cid);
        return ResponseEntity.ok().build();
    }
}
